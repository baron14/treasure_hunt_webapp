package treasure_hunt_webapp.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import treasure_hunt_webapp.custom.component.ItemPanel;
import treasure_hunt_webapp.dao.RouteDao;
import treasure_hunt_webapp.models.route.HeartRate;
import treasure_hunt_webapp.models.route.Point;
import treasure_hunt_webapp.models.route.Question;
import treasure_hunt_webapp.models.route.Route;
import treasure_hunt_webapp.models.route.Step;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("customTheme")
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
		back.setSizeFull();

		VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		back.addComponent(content);
		content.setWidth("80%");
		content.setHeight("100%");
		content.setStyleName("content");

		content.addComponent(new Button("Save", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println(route.toString());
				routeDao.create(route);
			}
		}));

		ComboBox routesList = new ComboBox("Saved Routes");
		for (Route r : savedRoutes) {
			Object id = routesList.addItem();
			routesList.setItemCaption(id, r.getName());
		}
		routesList.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Route loadingRoute = savedRoutes[(Integer)event.getProperty().getValue()-1];
				System.out.println(loadingRoute);
			}
		});
		content.addComponent(routesList);

		Component routeUI = createRouteUI();
		content.addComponent(routeUI);

		content.setExpandRatio(routeUI, 4);
		content.setSpacing(true);

		// Main
		VerticalLayout main = new VerticalLayout(back);
		main.setSizeFull();
		main.setExpandRatio(back, 6f);

		return main;
	}

	private Component createRouteUI() {
		VerticalLayout main = new VerticalLayout();
		main.setSpacing(true);
		main.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		TextField name = new TextField();
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
			public VerticalLayout addItem() {
				Step step = new Step();
				step.setName("");
				step.setTask("");
				step.setTreasure("");
				step.setSolution("");

				route.getSteps().add(step);
				return createStepUI(step);
			}

			@Override
			public void removeItem(Object object) {
				if (route.getSteps().contains(object)) {
					route.getSteps().remove(object);
				} else {
					throw new RuntimeException("Object " + object
							+ " does not exist.");
				}
			}

		};
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
		name.setWidth(TEXTFIELD_WIDTH);
		name.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				step.setName(event.getText());
			}
		});
		grid.addComponents(new Label("Step Name:"), name);

		TextField text = new TextField();
		text.setWidth(TEXTFIELD_WIDTH);
		text.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				step.setTask(event.getText());
			}
		});
		grid.addComponents(new Label("Text:"), text);

		TextField treasure = new TextField();
		treasure.setWidth(TEXTFIELD_WIDTH);
		treasure.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				step.setTreasure(event.getText());
			}
		});
		grid.addComponents(new Label("Treasure:"), treasure);

		TextField solution = new TextField();
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
			public VerticalLayout addItem() {
				Point point = new Point();
				point.setHr(new HeartRate());
				point.setName("");

				step.getPoints().add(point);
				return createPointUI(point);
			}

			@Override
			public void removeItem(Object object) {
				if (step.getPoints().contains(object)) {
					step.getPoints().remove(object);
				} else {
					throw new RuntimeException("Object " + object
							+ " does not exist.");
				}
			}
		};
		main.addComponent(points);

		ItemPanel questions = new ItemPanel("Questions:") {
			@Override
			public VerticalLayout addItem() {
				Question question = new Question();
				question.setAnswers(new ArrayList<String>());
				question.setCorrectAnswer("");
				question.setQuestion("");

				step.getQuestions().add(question);

				return createQuestionUI(question);
			}

			@Override
			public void removeItem(Object object) {
				if (step.getQuestions().contains(object)) {
					step.getQuestions().remove(object);
				} else {
					throw new RuntimeException("Object " + object
							+ " does not exist.");
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
		mrange.setWidth(TEXTFIELD_WIDTH);
		mrange.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				point.setName(event.getText());
			}
		});
		main.addComponents(new Label("M Range:"), mrange);

		TextField srange = new TextField();
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
		VerticalLayout root = new VerticalLayout();
		root.setData(question);

		GridLayout main = new GridLayout(2, 1);
		main.setSpacing(true);
		main.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

		TextField questionText = new TextField();
		questionText.setWidth(TEXTFIELD_WIDTH);
		questionText.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				question.setQuestion(event.getText());
			}
		});
		main.addComponents(new Label("Question:"), questionText);

		ItemPanel answers = new ItemPanel("Answers:") {
			List<CheckBox> checks = new ArrayList<CheckBox>();

			@Override
			public VerticalLayout addItem() {
				return createAnswerUI(question, checks);
			}

			@Override
			public void removeItem(Object object) {
				CheckBox checkBox = (CheckBox) ((ArrayList) object).get(1);
				if (question.getAnswers().contains(checkBox)) {
					checks.remove(checkBox);
				} else {
					throw new RuntimeException("Object " + checkBox
							+ " does not exist.");
				}

				Object value = ((ArrayList) object).get(0);
				if (question.getAnswers().contains(value)) {
					question.getAnswers().remove(value);
				} else {
					throw new RuntimeException("Object " + value
							+ " does not exist.");
				}
			}
		};
		main.addComponent(answers);

		root.addComponent(main);
		return root;
	}

	private VerticalLayout createAnswerUI(Question question,
			List<CheckBox> checks) {
		VerticalLayout main = new VerticalLayout();
		List<Object> data = new ArrayList<Object>();
		data.add("");
		main.setData(data);

		TextArea answer = new TextArea();
		answer.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChange(TextChangeEvent event) {
				Object oldValue = data.get(0);
				if (question.getAnswers().contains(oldValue)) {
					question.getAnswers().remove(oldValue);
					question.getAnswers().add(event.getText());
					data.remove(0);
					data.add(0, event.getText());
				} else {
					throw new RuntimeException("Object " + oldValue
							+ " does not exist.");
				}
			}
		});

		CheckBox isCorrect = new CheckBox("isCorrect:");
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

		main.addComponent(new HorizontalLayout(new Label("Question #:"),
				isCorrect));
		main.addComponent(answer);

		return main;
	}
}
