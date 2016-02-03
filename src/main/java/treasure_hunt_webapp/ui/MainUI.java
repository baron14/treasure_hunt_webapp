package treasure_hunt_webapp.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import treasure_hunt_webapp.custom.component.ItemPanel;
import treasure_hunt_webapp.dao.RouteDao;
import treasure_hunt_webapp.models.route.HeartRate;
import treasure_hunt_webapp.models.route.Point;
import treasure_hunt_webapp.models.route.Question;
import treasure_hunt_webapp.models.route.Route;
import treasure_hunt_webapp.models.route.Step;

@Theme("valo")
@SpringUI
@VaadinServletConfiguration(widgetset = "com.vaadin.tapio.googlemaps.WidgetSet", productionMode = false, ui = MainUI.class)
public class MainUI extends UI {
	private static final String TEXTFIELD_WIDTH = "500px";

	private static final long serialVersionUID = 1L;

	protected Route route;

	private ItemPanel steps;

	private RouteDao routeDao;

	private Route[] savedRoutes;

	@Override
	protected void init(VaadinRequest request) {
		try {
			routeDao = new RouteDao(new File("routes.mjorm.xml"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		route = new Route();

		savedRoutes = routeDao.getRoutes();

		this.setContent(createUI());
	}

	private Component createUI() {
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setHeight("200px");
		header.setStyleName("header");
		header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		HorizontalLayout innerHeader = new HorizontalLayout();
		header.addComponent(innerHeader);
		innerHeader.setStyleName("innerHeader");
		innerHeader.setHeight("90%");
		innerHeader.setWidth("90%");

		VerticalLayout back = new VerticalLayout();
		back.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		back.setStyleName("background");
		back.setSizeUndefined();
		back.setWidth("100%");

		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		back.addComponent(content);
		content.setSizeUndefined();
		content.setWidth("80%");
		content.setStyleName("content");

		Button saveButton = new Button("Save", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println(route.toString());
				routeDao.create(route);
				savedRoutes = routeDao.getRoutes(); //TODO refresh routes list on save
			}
		});
		saveButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		content.addComponent(saveButton);

		Button newButton = new Button("New", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				route = new Route();
				setContent(createUI());
			}
		});
		newButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		content.addComponent(newButton);
		
		ComboBox routesList = new ComboBox("Saved Routes");
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty("route", Route.class, null);
		routesList.setContainerDataSource(container);

		for (Route r : savedRoutes) {
			Object id = routesList.addItem();
			routesList.getItem(id).getItemProperty("route").setValue(r);
			routesList.setItemCaption(id, r.getName());
		}

		Button loadButton = new Button("Load", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (routesList.getValue() != null) {
					route = (Route) routesList.getItem(routesList.getValue()).getItemProperty("route").getValue();
					setContent(createUI());
				}
				else{
					Notification.show("Must select a route to load.");
				}
			}
		});
		loadButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		HorizontalLayout horizLoad = new HorizontalLayout(routesList, loadButton);
		horizLoad.setComponentAlignment(loadButton, Alignment.BOTTOM_LEFT);
		content.addComponent(horizLoad);
		content.addComponent(new Label("<hr />", Label.CONTENT_XHTML));

		Component routeUI = createRouteUI();
		content.addComponent(routeUI);

		content.setExpandRatio(routeUI, 4);
		content.setSpacing(true);

		// Main
		VerticalLayout main = new VerticalLayout(back);
		// main.setSizeFull();
		main.setSizeUndefined();
		main.setWidth("100%");
		main.setExpandRatio(back, 6f);

		return main;
	}

	private Component createRouteUI() {
		VerticalLayout main = new VerticalLayout();
		main.setSpacing(true);
		main.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		TextField name = new TextField();
		name.setValue(route.getName());
		name.setWidth(TEXTFIELD_WIDTH);
		name.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				route.setName(event.getText());
			}
		});

		Label nameLabel = new Label("Route Name:");
		HorizontalLayout nameLayout = new HorizontalLayout(nameLabel, name);
		nameLayout.setWidth("100%");
		nameLayout.setExpandRatio(nameLabel, 1f);
		nameLayout.setExpandRatio(name, 8f);
		main.addComponent(nameLayout);

		steps = new ItemPanel("Steps") {
			@Override
			public void addItem() {
				Step step = new Step();
				step.setName("");
				step.setTask("");
				step.setTreasure("");
				step.setSolution("");

				route.getSteps().add(step);
				this.addItemInner(createStepUI(step));
			}
			
			@Override
			public void addExistingItem(Object item){
				this.addItemInner(createStepUI((Step)item));
			}

			@Override
			public void removeItem(Object object) {
				if (route.getSteps().contains(object)) {
					route.getSteps().remove(object);
				} else {
					throw new RuntimeException("Object " + object + " does not exist.");
				}
			}

		};
		
		for(Step step : route.getSteps()){
			steps.addExistingItem(step);
		}
		
		main.addComponent(steps);

		return main;
	}

	private VerticalLayout createStepUI(Step step) {
		VerticalLayout main = new VerticalLayout();
		main.setData(step);
		main.setSpacing(true);

		GridLayout grid = new GridLayout(2, 1);
		grid.setSpacing(true);
		grid.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		main.addComponent(grid);

		TextField name = new TextField();
		name.setValue(step.getName());
		name.setWidth(TEXTFIELD_WIDTH);
		name.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				step.setName(event.getText());
			}
		});
		grid.addComponents(new Label("Step Name:"), name);

		TextField text = new TextField();
		text.setValue(step.getTask());
		text.setWidth(TEXTFIELD_WIDTH);
		text.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				step.setTask(event.getText());
			}
		});
		grid.addComponents(new Label("Text:"), text);

		TextField treasure = new TextField();
		treasure.setValue(step.getTreasure());
		treasure.setWidth(TEXTFIELD_WIDTH);
		treasure.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				step.setTreasure(event.getText());
			}
		});
		grid.addComponents(new Label("Treasure:"), treasure);

		TextField solution = new TextField();
		solution.setValue(step.getSolution());
		solution.setWidth(TEXTFIELD_WIDTH);
		solution.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				step.setSolution(event.getText());
			}
		});
		grid.addComponents(new Label("Solution:"), solution);

		ItemPanel points = new ItemPanel("Points:") {
			@Override
			public void addItem() {
				Point point = new Point();
				point.setHr(new HeartRate());
				point.setName("");

				step.getPoints().add(point);
				this.addItemInner(createPointUI(point));
			}
			
			@Override
			public void addExistingItem(Object item){
				this.addItemInner(createPointUI((Point)item));
			}

			@Override
			public void removeItem(Object object) {
				if (step.getPoints().contains(object)) {
					step.getPoints().remove(object);
				} else {
					throw new RuntimeException("Object " + object + " does not exist.");
				}
			}
		};
		main.addComponent(points);

		ItemPanel questions = new ItemPanel("Questions:") {
			@Override
			public void addItem() {
				Question question = new Question();
				question.setAnswers(new ArrayList<String>());
				question.setCorrectAnswer("");
				question.setQuestion("");

				step.getQuestions().add(question);

				this.addItemInner(createQuestionUI(question));
			}

			@Override
			public void addExistingItem(Object item){
				this.addItemInner(createQuestionUI((Question)item));
			}
			
			@Override
			public void removeItem(Object object) {
				if (step.getQuestions().contains(object)) {
					step.getQuestions().remove(object);
				} else {
					throw new RuntimeException("Object " + object + " does not exist.");
				}
			}
		};
		main.addComponent(questions);

		return main;
	}

	private VerticalLayout createPointUI(Point point) {
		VerticalLayout root = new VerticalLayout();
		root.setData(point);

		GridLayout main = new GridLayout(2, 1);
		main.setSpacing(true);
		main.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

		TextField name = new TextField();
		name.setValue(point.getName());
		name.setWidth(TEXTFIELD_WIDTH);
		name.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				point.setName(event.getText());
			}
		});
		main.addComponents(new Label("Point Name:"), name);

		// Map
		TextField loc = new TextField();
		loc.setValue(point.getLatitude()+", "+point.getLongitude());
		loc.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				System.out.println("LatLng=" + event.getText());
			}
		});
		Button mapButton = new Button();
		mapButton.setIcon(FontAwesome.MAP_MARKER);
		mapButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Window popup = new Window();
				UI.getCurrent().addWindow(popup);
				popup.setCaption("Select a location...");
				popup.setDraggable(false);
				popup.setModal(true);
				popup.setHeight("200px");
				popup.setWidth("200px");
				popup.center();

				GoogleMap googleMap = new GoogleMap(null, null, null);
				googleMap.setCenter(new LatLon(55.9094, 3.3201));

				VerticalLayout content = new VerticalLayout();
				popup.setContent(content);
				content.setSizeFull();

				content.addComponent(googleMap);

				popup.setVisible(true);
			}
		});
		HorizontalLayout horiz = new HorizontalLayout(loc, mapButton);
		main.addComponents(new Label("LatLng:"), horiz);
		main.setComponentAlignment(horiz, Alignment.MIDDLE_LEFT);

		TextField mrange = new TextField();
		mrange.setValue(Float.toString(point.getHr().getMRange()));
		mrange.setWidth(TEXTFIELD_WIDTH);
		mrange.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				point.setName(event.getText());
			}
		});
		main.addComponents(new Label("M Range:"), mrange);

		TextField srange = new TextField();
		srange.setValue(Float.toString(point.getHr().getSRange()));
		srange.setWidth(TEXTFIELD_WIDTH);
		srange.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				point.setName(event.getText());
			}
		});
		main.addComponents(new Label("S Range:"), srange);

		root.addComponent(main);
		return root;
	}

	private VerticalLayout createQuestionUI(Question question) {
		VerticalLayout main = new VerticalLayout();
		main.setData(question);

		main.addComponent(new Label("Question #:"));

		TextArea questionText = new TextArea();
		questionText.setValue(question.getQuestion());
		questionText.setWidth(TEXTFIELD_WIDTH);
		questionText.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				question.setQuestion(event.getText());
			}
		});
		main.addComponents(questionText);

		ItemPanel answers = new ItemPanel("Answers:") {
			List<CheckBox> checks = new ArrayList<CheckBox>();

			@Override
			public void addItem() {
				this.addItemInner(createAnswerUI(question, checks));
			}
			
			@Override
			public void addExistingItem(Object item){
				this.addItemInner(createAnswerUI(question, checks));
			}

			@Override
			public void removeItem(Object object) {
				CheckBox checkBox = (CheckBox) ((ArrayList) object).get(1);
				if (question.getAnswers().contains(checkBox)) {
					checks.remove(checkBox);
				} else {
					throw new RuntimeException("Object " + checkBox + " does not exist.");
				}

				Object value = ((ArrayList) object).get(0);
				if (question.getAnswers().contains(value)) {
					question.getAnswers().remove(value);
				} else {
					throw new RuntimeException("Object " + value + " does not exist.");
				}
			}
		};
		main.addComponent(answers);

		return main;
	}

	private VerticalLayout createAnswerUI(Question question, List<CheckBox> checks) {
		VerticalLayout main = new VerticalLayout();
		List<Object> data = new ArrayList<Object>();
		data.add("");
		main.setData(data);

		TextArea answer = new TextArea();
		//TODO add existing answers
		answer.setWidth(TEXTFIELD_WIDTH);
		answer.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				Object oldValue = data.get(0); // TODO change to map
				if (question.getAnswers().contains(oldValue)) {
					question.getAnswers().remove(oldValue);
					question.getAnswers().add(event.getText());
					data.remove(0);
					data.add(0, event.getText());
				} else {
					// throw new RuntimeException("Object " + oldValue
					// + " does not exist.");
				}
			}
		});

		CheckBox isCorrect = new CheckBox();
		isCorrect.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if ((boolean) event.getProperty().getValue()) {
					for (CheckBox c : checks) {
						if (c != isCorrect) {
							c.setValue(false);
						}
					}
				}
			}
		});
		checks.add(isCorrect);
		data.add(1, isCorrect);

		HorizontalLayout horiz = new HorizontalLayout(new Label("Question #:"), new Label("isCorrect:"), isCorrect);
		horiz.setSpacing(true);
		main.addComponent(horiz);
		main.addComponent(answer);

		return main;
	}
}
