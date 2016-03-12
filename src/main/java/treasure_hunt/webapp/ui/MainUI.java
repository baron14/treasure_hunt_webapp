package treasure_hunt.webapp.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import treasure_hunt.data.dao.RouteDao;
import treasure_hunt.data.models.route.HeartRate;
import treasure_hunt.data.models.route.Point;
import treasure_hunt.data.models.route.Question;
import treasure_hunt.data.models.route.Route;
import treasure_hunt.data.models.route.Step;
import treasure_hunt.webapp.custom.components.ItemPanel;
import treasure_hunt.webapp.custom.receivers.UploadReciever;

/**
 * The main UI for the treasure hunt web application
 */

@Theme("valo")
@SpringUI
@SuppressWarnings({ "serial", "unchecked" })
public class MainUI extends UI {
	private static final long serialVersionUID = 1L;
	private static final String MJORM_ROUTES_PATH = "routes.mjorm.xml";
	private static final String TEXTFIELD_WIDTH = "500px";

	// DAOs
	private RouteDao routeDao;

	protected Route route;
	private ItemPanel steps;
	private Route[] savedRoutes;

	/**
	 * Initialises the UI and all necessary components
	 * 
	 * @Override
	 */
	protected void init(VaadinRequest request) {
		// Try and initialise the RouteDao using MJORM_ROUTES_PATH
		try {
			routeDao = new RouteDao(new File(MJORM_ROUTES_PATH));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Load all routes from the database using the RouteDao
		savedRoutes = routeDao.getRoutes();

		// Initialise a new Route
		route = new Route();

		// Create the GUI
		this.setContent(createUI());
	}

	/**
	 * Creates the GUI
	 */
	private Component createUI() {
		// The 'Save' button that saves the current working route
		Button saveButton = new Button("Save", (ClickListener) event -> {
			routeDao.create(route);
			savedRoutes = routeDao.getRoutes();
			// TODO refresh routes list on save
			});
		saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		// The 'New' button that creates a new route and refreshes the GUI
		Button newButton = new Button("New", (ClickListener) event -> {
			ConfirmDialog.show(UI.getCurrent(), "New?",
					"Creating a new map will clear any unsaved changes from the current map. Are you sure?", "Yes", "No",
					(org.vaadin.dialogs.ConfirmDialog.Listener) dialog -> {
						if (dialog.isConfirmed()) {
							route = new Route();
							setContent(createUI());
						}
					});
			});
		newButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		// The list of all previously saved routes loaded from the database
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty("route", Route.class, null);
		ComboBox routesList = new ComboBox("Saved Routes", container);

		// Add all routes in the savedRoutes list to the ComboBox
		for (Route r : savedRoutes) {
			Object id = routesList.addItem();
			routesList.getItem(id).getItemProperty("route").setValue(r);
			routesList.setItemCaption(id, r.getName());
		}

		// The 'Load' button that loads the selected route from the ComboBox
		// routesList if a route has been selected and refreshed the GUI.
		// Otherwise, an error message is shown
		Button loadButton = new Button("Load", (ClickListener) event -> {
			if (routesList.getValue() != null) {
				route = (Route) routesList.getItem(routesList.getValue())
						.getItemProperty("route").getValue();
				setContent(createUI());
			} else {
				Notification.show("Must select a route to load.");
			}
		});
		loadButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);

		HorizontalLayout horizLoad = new HorizontalLayout();
		horizLoad.setSpacing(true);
		horizLoad.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		horizLoad.addComponents(newButton, saveButton, routesList, loadButton);

		// The GUI components that detail and display a Route and all of its
		// information
		Component routeUI = createRouteUI();

		// The content component that holds most of the other components
		VerticalLayout content = new VerticalLayout(horizLoad, new Label(
				"<hr />", Label.CONTENT_XHTML), routeUI);
		content.setMargin(true);
		content.setSizeUndefined();
		content.setWidth("80%");
		content.setStyleName("content");
		content.setExpandRatio(routeUI, 4);
		content.setSpacing(true);

		// The background component. Used to allow better positioning of the
		// content component
		VerticalLayout back = new VerticalLayout(content);
		back.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		back.setStyleName("background");
		back.setSizeUndefined();
		back.setWidth("100%");

		// The root GUI element that holds all other elements
		VerticalLayout main = new VerticalLayout(back);
		main.setSizeUndefined();
		main.setWidth("100%");
		main.setExpandRatio(back, 6f);
		return main;
	}

	/**
	 * Creates the GUI that displays a 'Route' and all of its information
	 * 
	 * @return the created GUI
	 */
	private Component createRouteUI() {
		// The components that make up the 'Name' field of the route
		TextField name = new TextField();
		name.setValue(route.getName());
		name.setWidth(TEXTFIELD_WIDTH);
		name.addTextChangeListener(event -> route.setName(event.getText()));

		Label nameLabel = new Label("Route Name:");

		HorizontalLayout nameLayout = new HorizontalLayout(nameLabel, name);
		nameLayout.setWidth("100%");
		nameLayout.setExpandRatio(nameLabel, 1f);
		nameLayout.setExpandRatio(name, 8f);

		// The ItemPanel that holds all 'Step' items for this route
		steps = new ItemPanel("Steps") {
			@Override
			public void addItem() {
				// Initialise a new 'Step' object
				Step step = new Step();
				step.setName("");
				step.setTask("");
				step.setTreasure("");
				step.setSolution("");

				// Add the newly created 'Step' object to the data structure
				route.getSteps().add(step);

				this.addItemInner(createStepUI(step));
			}

			@Override
			public void addExistingItem(Object item) {
				this.addItemInner(createStepUI((Step) item));
			}

			@Override
			public void removeItem(Object object) {
				// Removes the 'Step' object if it is contained in the data
				// structure. Otherwise something is wrong so an exception is
				// thrown
				if (route.getSteps().contains(object)) {
					route.getSteps().remove(object);
				} else {
					throw new RuntimeException("Object " + object
							+ " does not exist.");
				}
			}
		};

		// Adds all existing 'Step' objects from the data structure into the
		// ItemPanel
		for (Step step : route.getSteps()) {
			steps.addExistingItem(step);
		}

		// The root GUI element that holds all other elements
		VerticalLayout main = new VerticalLayout(nameLayout, steps);
		main.setSpacing(true);
		main.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		return main;
	}

	/**
	 * Creates the GUI that displays a 'Step' and all of its information
	 * 
	 * @return the created GUI
	 */
	private VerticalLayout createStepUI(Step step) {
		// The components that make up the 'Name' field of the route
		TextField name = new TextField();
		name.setValue(step.getName());
		name.setWidth(TEXTFIELD_WIDTH);
		name.addTextChangeListener(event -> step.setName(event.getText()));

		TextArea text = new TextArea();
		text.setValue(step.getTask());
		text.setWidth(TEXTFIELD_WIDTH);
		text.addTextChangeListener(event -> step.setTask(event.getText()));

		// The components that make up the 'Treasure' field of the route
		TextArea treasure = new TextArea();
		treasure.setValue(step.getTreasure());
		treasure.setWidth(TEXTFIELD_WIDTH);
		treasure.addTextChangeListener(event -> step.setTreasure(event
				.getText()));

		// The components that make up the 'Solution' field of the route
		TextArea solution = new TextArea();
		solution.setValue(step.getSolution());
		solution.setWidth(TEXTFIELD_WIDTH);
		solution.addTextChangeListener(event -> step.setSolution(event
				.getText()));

		// The ItemPanel that holds all 'Point' items for this route
		ItemPanel points = new ItemPanel("Points:") {
			@Override
			public void addItem() {
				// Initialise a new 'Point' object
				Point point = new Point();
				point.setHr(new HeartRate());
				point.setName("");

				// Add the newly created 'Point' object to the data structure
				step.getPoints().add(point);

				this.addItemInner(createPointUI(point));
			}

			@Override
			public void addExistingItem(Object item) {
				this.addItemInner(createPointUI((Point) item));
			}

			@Override
			public void removeItem(Object object) {
				// Removes the 'Point' object if it is contained in the data
				// structure. Otherwise something is wrong so an exception is
				// thrown
				if (step.getPoints().contains(object)) {
					step.getPoints().remove(object);
				} else {
					throw new RuntimeException("Object " + object
							+ " does not exist.");
				}
			}
		};

		// Adds all existing 'Point' objects from the data structure into the
		// ItemPanel
		for (Point point : step.getPoints()) {
			points.addExistingItem(point);
		}

		// The ItemPanel that holds all 'Question' items for this route
		ItemPanel questions = new ItemPanel("Questions:") {
			@Override
			public void addItem() {
				// Initialise a new 'Question' object
				Question question = new Question();
				question.setAnswers(new ArrayList<String>());
				question.setCorrectAnswer("");
				question.setQuestion("");

				// Add the newly created 'Question' object to the data structure
				step.getQuestions().add(question);

				this.addItemInner(createQuestionUI(question));
			}

			@Override
			public void addExistingItem(Object item) {
				this.addItemInner(createQuestionUI((Question) item));
			}

			@Override
			public void removeItem(Object object) {
				// Removes the 'Question' object if it is contained in the data
				// structure. Otherwise something is wrong so an exception is
				// thrown
				if (step.getQuestions().contains(object)) {
					step.getQuestions().remove(object);
				} else {
					throw new RuntimeException("Object " + object
							+ " does not exist.");
				}
			}
		};

		// Adds all existing 'Question' objects from the data structure into the
		// ItemPanel
		for (Question question : step.getQuestions()) {
			questions.addExistingItem(question);
		}

		// The grid of components
		GridLayout grid = new GridLayout(2, 1);
		grid.setSpacing(true);
		grid.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		grid.addComponents(new Label("Step Name:"), name, new Label("Text:"),
				text, new Label("Treasure:"), treasure, new Label("Solution:"),
				solution);

		// The root GUI element that holds all other elements
		VerticalLayout main = new VerticalLayout(grid, points, questions);
		main.setData(step);
		main.setSpacing(true);
		return main;
	}

	/**
	 * Creates the GUI that displays a 'Point' and all of its information
	 * 
	 * @return the created GUI
	 */
	private VerticalLayout createPointUI(Point point) {
		// The grid that the rest of the components fit into
		GridLayout grid = new GridLayout(2, 1);
		grid.setSpacing(true);
		grid.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

		// The components that make up the 'Name' field of the route
		TextField name = new TextField();
		name.setValue(point.getName());
		name.setWidth(TEXTFIELD_WIDTH);
		name.addTextChangeListener(event -> point.setName(event.getText()));
		grid.addComponents(new Label("Point Name:"), name);

		// The components that make up the 'LngLat' field of the route
		// TODO Finish LngLat
		TextField loc = new TextField();
		loc.setValue(point.getLatitude() + ", " + point.getLongitude());
		loc.addTextChangeListener(event -> System.out.println("LatLng="
				+ event.getText()));

		// The button that when clicked shows the map popup
		Button mapButton = new Button();
		mapButton.setIcon(FontAwesome.MAP_MARKER);
		mapButton.addClickListener(event -> showMapPopup());

		HorizontalLayout horiz = new HorizontalLayout(loc, mapButton);
		grid.addComponents(new Label("LatLng:"), horiz);
		grid.setComponentAlignment(horiz, Alignment.MIDDLE_LEFT);

		// The components that make up the 'MRange' field of the route
		TextField mrange = new TextField();
		mrange.setValue(Float.toString(point.getHr().getMRange()));
		mrange.setWidth(TEXTFIELD_WIDTH);
		mrange.addTextChangeListener(event -> point.setName(event.getText()));
		grid.addComponents(new Label("M Range:"), mrange);

		// The components that make up the 'SRange' field of the route
		TextField srange = new TextField();
		srange.setValue(Float.toString(point.getHr().getSRange()));
		srange.setWidth(TEXTFIELD_WIDTH);
		srange.addTextChangeListener(event -> point.setName(event.getText()));
		grid.addComponents(new Label("S Range:"), srange);

		// The root GUI element that holds all other elements
		VerticalLayout main = new VerticalLayout(grid);
		main.setData(point);
		return main;
	}

	/**
	 * Creates and shows the window popup that has the Google Map functionality
	 */
	private void showMapPopup() {
		// Create the google map
		GoogleMap googleMap = new GoogleMap(null, null, null);
		googleMap.setCenter(new LatLon(55.9094, 3.3201));

		// The Content component that holds everything else
		VerticalLayout content = new VerticalLayout(googleMap);
		content.setSizeFull();

		// Create and configure the Window
		Window popup = new Window("Select a location...", content);
		popup.setDraggable(false);
		popup.setModal(true);
		popup.setHeight("200px");
		popup.setWidth("200px");
		popup.center();
		popup.setVisible(true);

		UI.getCurrent().addWindow(popup);
	}

	/**
	 * Creates the GUI that displays a 'Question' and all of its information
	 * 
	 * @return the created GUI
	 */
	private VerticalLayout createQuestionUI(Question question) {
		// The preview of the uploaded image (if any)
		final Embedded image = new Embedded("Uploaded Image");
		image.setWidth("100px");
		image.setHeight("100px");
		image.setVisible(false);

		// The components that make up the 'isImage' field of the route
		CheckBox isImage = new CheckBox("Has Image?");
		UploadReciever receiver = new UploadReciever(image);
		Upload upload = new Upload(null, receiver);
		upload.addSucceededListener(receiver);

		VerticalLayout imageLayout = new VerticalLayout(upload, image);
		imageLayout.setVisible(false);
		isImage.addValueChangeListener(event -> {
			question.setIsImage((boolean) event.getProperty().getValue());
			imageLayout.setVisible(isImage.getValue());
		});

		// The components that make up the 'Question' field of the route
		TextArea questionText = new TextArea();
		questionText.setValue(question.getQuestion());
		questionText.setWidth(TEXTFIELD_WIDTH);
		questionText.addTextChangeListener(event -> question.setQuestion(event
				.getText()));

		// The list of checkboxes from child answers
		List<CheckBox> checks = new ArrayList<CheckBox>();

		// The ItemPanel that holds all 'Answer' items for this route
		ItemPanel answers = new ItemPanel("Answers:") {
			@Override
			public void addItem() {
				this.addItemInner(createAnswerUI(question, checks, false, ""));
			}

			@Override
			public void addExistingItem(Object item) {
				throw new UnsupportedOperationException(
						"AddExistingItem is not used by answer data.");
			}

			@Override
			public void removeItem(Object object) {
				// Removes the checkbox for the answer to be removed from the
				// list if found. Otherwise something is wrong so an exception
				// is thrown
				CheckBox checkBox = (CheckBox) ((ArrayList<Object>) object)
						.get(1);
				if (question.getAnswers().contains(checkBox)) {
					checks.remove(checkBox);
				} else {
					throw new RuntimeException("Object " + checkBox
							+ " does not exist.");
				}

				// Removes the 'data' object if it is contained in the data
				// structure. Otherwise something is wrong so an exception is
				// thrown
				Object value = ((ArrayList<Object>) object).get(0);
				if (question.getAnswers().contains(value)) {
					question.getAnswers().remove(value);
				} else {
					throw new RuntimeException("Object " + value
							+ " does not exist.");
				}
			}
		};

		// Different structure for answers than other ItemPanel data elements
		String correctAnswer = question.getCorrectAnswer();
		for (String answer : question.getAnswers()) {
			answers.addItemInner(createAnswerUI(question, checks,
					correctAnswer == answer, answer));
		}

		// The root GUI element that holds all other elements
		VerticalLayout main = new VerticalLayout(questionText, isImage,
				imageLayout, answers);
		main.setSpacing(true);
		main.setData(question);
		return main;
	}

	/**
	 * Creates the GUI that displays a 'Answer' and all of its information
	 * 
	 * @return the created GUI
	 */
	private VerticalLayout createAnswerUI(Question question,
			List<CheckBox> checks, boolean correctAnswer, String text) {
		// Initialise the array of data
		List<Object> data = new ArrayList<Object>();
		data.add("");

		// The components that make up the 'Answer' field of the route
		TextArea answer = new TextArea();
		answer.setValue(text);
		answer.setWidth(TEXTFIELD_WIDTH);
		answer.addTextChangeListener(event -> {
			Object oldValue = data.get(0); // TODO change to map
			if (question.getAnswers().contains(oldValue)) {
				question.getAnswers().remove(oldValue);
				question.getAnswers().add(event.getText());
				data.remove(0);
				data.add(0, event.getText());
			}
		});

		// The components that make up the 'isCorrect' field of the route
		CheckBox isCorrect = new CheckBox();
		isCorrect.setValue(correctAnswer);
		isCorrect.addValueChangeListener(event -> {
			if ((boolean) event.getProperty().getValue()) {
				for (CheckBox c : checks) {
					if (c != isCorrect) {
						c.setValue(false);
					}
				}
			}
		});
		checks.add(isCorrect);
		data.add(1, isCorrect);

		// Aligns and positions the question and checkbox components properly
		HorizontalLayout horiz = new HorizontalLayout(new Label("isCorrect:"),
				isCorrect);
		horiz.setSpacing(true);

		// The root GUI element that holds all other elements
		VerticalLayout main = new VerticalLayout(horiz, answer);
		main.setData(data);
		return main;
	}
}
