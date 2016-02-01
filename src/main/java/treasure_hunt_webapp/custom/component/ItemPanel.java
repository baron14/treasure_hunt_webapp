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
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for wrapping content in a collapsable panel
 * 
 * @author Jordan
 *
 */
public abstract class ItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private Label headerText;
	private Label iconLabel;
	protected Layout content;

	public ItemPanel(String headerText) {
		this.headerText = new Label(headerText);
		this.content = new VerticalLayout();

		this.setSizeFull();
		this.setStyleName(ValoTheme.PANEL_BORDERLESS);
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

		HorizontalLayout footer = new HorizontalLayout(createAdd());

		// Create root and add children - header, content and footer
		HorizontalLayout horizontalLayout = new HorizontalLayout(
				iconLayout, headerText);
		horizontalLayout.setSpacing(true);
		VerticalLayout main = new VerticalLayout(horizontalLayout, content, footer);
		main.setSizeFull();
		
		return main;
	}

	private void swapIcon() {
		iconLabel.setValue(content.isVisible() ? FontAwesome.MINUS_CIRCLE
				.getHtml() : FontAwesome.PLUS_CIRCLE.getHtml());
	}

	private Component createAdd() {
		VerticalLayout main = new VerticalLayout();

		Label addLabel = new Label();
		addLabel.setContentMode(ContentMode.HTML);
		addLabel.setValue(FontAwesome.PLUS.getHtml());
		main.addComponent(addLabel);
		main.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				addItemInner();
			}
		});
		return main;
	}

	private void addItemInner(){
		Label delete = new Label();
		delete.setContentMode(ContentMode.HTML);
		delete.setValue(FontAwesome.MINUS.getHtml());
		VerticalLayout deleteLayout = new VerticalLayout(delete);
		
		VerticalLayout addedItem = addItem();
		HorizontalLayout main = new HorizontalLayout(deleteLayout, addedItem);
		main.setSpacing(true);
		
		deleteLayout.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				content.removeComponent(main);
				removeItem(addedItem.getData());		
			}
		});
		
		content.addComponent(main);
	}
	
	public abstract VerticalLayout addItem();

	public abstract void removeItem(Object object);
	
	/*
	 * Getters and Setters
	 */

	public Component getContent() {
		return content;
	}
}
