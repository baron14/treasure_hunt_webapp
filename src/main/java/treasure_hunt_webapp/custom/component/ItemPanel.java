package treasure_hunt_webapp.custom.component;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Class for wrapping content in a collapsable panel
 * 
 * @author Jordan
 *
 */
public class ItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private Label headerText;
	private Label iconLabel;
	private Layout content;

	public ItemPanel(String headerText) {
		this.headerText = new Label(headerText);
		this.content = new VerticalLayout();

		this.setSizeFull();
		this.setContent(this.init());
	}

	private Component init() {
		// Create the header components
		iconLabel = new Label();
		iconLabel.setContentMode(ContentMode.HTML);
		iconLabel.setValue(FontAwesome.MINUS_CIRCLE.getHtml());

		VerticalLayout iconLayout = new VerticalLayout(iconLabel);
		iconLayout.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				content.setVisible(!content.isVisible());
				swapIcon();
			}
		});

		// Create root and add children - header and content
		VerticalLayout main = new VerticalLayout(new HorizontalLayout(iconLayout, headerText), content);
		main.setSizeFull();

		return main;
	}
	
	private void swapIcon() {
		iconLabel.setValue(content.isVisible() ? FontAwesome.MINUS_CIRCLE.getHtml() : FontAwesome.PLUS_CIRCLE.getHtml());
	}
	
	public void addItem(Component comp){
		content.addComponent(comp);
	}

	/*
	 * Getters and Setters
	 */

	public Component getContent() {
		return content;
	}
}
