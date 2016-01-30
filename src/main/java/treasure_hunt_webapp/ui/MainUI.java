package treasure_hunt_webapp.ui;

import java.io.File;

import treasure_hunt_webapp.custom.component.ItemPanel;
import treasure_hunt_webapp.dao.RouteDao;
import treasure_hunt_webapp.models.route.Route;
import treasure_hunt_webapp.models.route.Step;

import com.vaadin.annotations.Theme;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("customTheme")
@SpringUI
public class MainUI extends UI {
	private static final String TEXTFIELD_WIDTH = "500px";

	private static final long serialVersionUID = 1L;

	protected Route route;

	private ItemPanel steps;

	private RouteDao routeDao;

	@Override
	protected void init(VaadinRequest request) {
		try {
			routeDao = new RouteDao(new File("routes.mjorm.xml"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		route = new Route();
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
		content.addComponent(createRouteUI());

		content.addComponent(new Button("Save", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println(route.toString());
				routeDao.create(route);
			}
		}));

		// Main
		VerticalLayout main = new VerticalLayout(header, back);
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

		// main.addComponent(createTextItem("Step Name:", new
		// ObjectProperty<String>(step.getName())));
		// main.addComponent(createTextItem("Text:", new
		// ObjectProperty<String>(step.getTask())));
		// main.addComponent(createTextItem("Treasure:", new
		// ObjectProperty<String>(step.getTreasure())));
		// main.addComponent(createTextItem("Solution:", new
		// ObjectProperty<String>(step.getSolution())));

		return main;
	}
}
