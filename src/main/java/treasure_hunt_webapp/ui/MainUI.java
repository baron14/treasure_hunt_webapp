package treasure_hunt_webapp.ui;

import treasure_hunt_webapp.custom.component.ItemPanel;
import treasure_hunt_webapp.models.route.Route;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
		return createRouteUI();
	}

	private Component createRouteUI() {
		VerticalLayout main = new VerticalLayout();
		main.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		TextField name = new TextField();
		name.setPropertyDataSource(new ObjectProperty<String>(route.getName()));
		main.addComponent(new HorizontalLayout(new Label("Route Name:"), name));

		steps = new ItemPanel("Steps");
		VerticalLayout wrapper = new VerticalLayout(steps, createAdd());
		main.addComponent(wrapper);
		
		return main;
	}
	
	private Component createAdd(){
		VerticalLayout main = new VerticalLayout();
		
		Label addLabel = new Label();
		addLabel.setContentMode(ContentMode.HTML);
		addLabel.setValue(FontAwesome.PLUS.getHtml());
		main.addComponent(addLabel);
		main.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				steps.addItem(createStepUI());
			}
		});
		return main;
	}
	
	private Component createStepUI(){
		VerticalLayout main = new VerticalLayout();
		
		main.addComponent(new Label("AAAAAAAAAAAAAAAAAAAAAAAA"));
		
		return main;
	}
}
