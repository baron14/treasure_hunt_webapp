package treasure_hunt_webapp.ui;

import treasure_hunt_webapp.custom.component.ItemPanel;
import treasure_hunt_webapp.models.route.Route;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("customTheme")
@SpringUI
public class MainUI extends UI {
	private static final long serialVersionUID = 1L;

	private Route route;

	private ItemPanel steps;

	@Override
	protected void init(VaadinRequest request) {
		route = new Route();
		this.setContent(createUI());
	}

	private Component createUI() {
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
		
		return back;
	}

	private Component createRouteUI() {
		VerticalLayout main = new VerticalLayout();
		main.setSpacing(true);
		main.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		TextField name = new TextField();
		name.setWidth("500px");
		name.setPropertyDataSource(new ObjectProperty<String>(route.getName()));
		Label nameLabel = new Label("Route Name:");
		HorizontalLayout nameLayout = new HorizontalLayout(nameLabel, name);
		nameLayout.setWidth("100%");
		nameLayout.setExpandRatio(nameLabel, 1f);
		nameLayout.setExpandRatio(name, 8f);
		main.addComponent(nameLayout);

		steps = new ItemPanel("Steps") {
			@Override
			public void addItem() {
				content.addComponent(createStepUI());
			}
		};
		main.addComponent(steps);

		return main;
	}

	private Component createStepUI() {
		VerticalLayout main = new VerticalLayout();

		main.addComponent(new Label("AAAAAAAAAAAAAAAAAAAAAAAA"));

		return main;
	}
}
