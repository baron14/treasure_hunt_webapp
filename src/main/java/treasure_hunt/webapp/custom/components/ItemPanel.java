package treasure_hunt.webapp.custom.components;

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
 * Class for wrapping GUI content in a collapsible panel
 */

public abstract class ItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private Label iconLabel;
	protected Layout content;

	public ItemPanel(String headerText) {
		this.content = new VerticalLayout();

		this.setSizeFull();
		this.setStyleName(ValoTheme.PANEL_BORDERLESS);
		this.init(headerText);
	}

	/**
	 * Initialises the GUI components
	 */
	private void init(String headerText) {
		// The Label that holds the current Icon image - either minimise or
		// maximise - as an HTML element
		iconLabel = new Label();
		iconLabel.setContentMode(ContentMode.HTML);
		iconLabel.setValue(FontAwesome.MINUS_CIRCLE.getHtml());

		// The Icon component that can be clicked on to minimse/maximise the
		// content panel. When clicked, the visibility of the content and the
		// image for the min/max icon is flipped
		VerticalLayout iconLayout = new VerticalLayout(iconLabel);
		iconLayout.addLayoutClickListener(event -> {
			content.setVisible(!content.isVisible());
			iconLabel.setValue(content.isVisible() ? FontAwesome.MINUS_CIRCLE.getHtml() : FontAwesome.PLUS_CIRCLE.getHtml());
		});

		// The header component that holds the clickable icon and the header
		// text
		HorizontalLayout header = new HorizontalLayout(iconLayout, new Label(headerText));
		header.setSpacing(true);

		// The footer space that holds the 'Add' button for adding more items to
		// the ItemPanel
		HorizontalLayout footer = new HorizontalLayout(createAdd());

		// Create the root GUI component and add the header, content and footer
		// components
		VerticalLayout main = new VerticalLayout(header, content, footer);
		main.setSizeFull();
		this.setContent(main);
	}

	/**
	 * Creates the 'Add' component i.e. the Add image and the ClickListener
	 * 
	 * @return the created 'Add' component
	 */
	private Component createAdd() {
		// The label with the 'Add' image as an HTML element
		Label addLabel = new Label();
		addLabel.setContentMode(ContentMode.HTML);
		addLabel.setValue(FontAwesome.PLUS.getHtml());

		// The root GUI component with a ClickListener
		VerticalLayout main = new VerticalLayout(addLabel);
		main.addLayoutClickListener(event -> addItem());

		return main;
	}

	/**
	 * 
	 * @param addItemLayout
	 */
	public void addItemInner(VerticalLayout addItemLayout) {
		// The label with the 'Delete' image as an HTML element
		Label delete = new Label();
		delete.setContentMode(ContentMode.HTML);
		delete.setValue(FontAwesome.MINUS.getHtml());
		VerticalLayout deleteLayout = new VerticalLayout(delete);

		// The root GUI component that holds the delete and add components
		HorizontalLayout main = new HorizontalLayout(deleteLayout, addItemLayout);
		main.setSpacing(true);
		content.addComponent(main);

		// Add the ClickListener that removes the element when the delete
		// component is clicked
		deleteLayout.addLayoutClickListener(event -> {
			content.removeComponent(main);
			removeItem(addItemLayout.getData());
		});
	}

	/**
	 * An abstract method for adding an existing item (i.e. during loads) to the
	 * ItemPanel.
	 * 
	 * @param item
	 *            - the Object to add to the ItemPanel
	 */
	public abstract void addExistingItem(Object item);

	/**
	 * An abstract method for adding a new item to the ItemPanel
	 */
	public abstract void addItem();

	/**
	 * An abstract method for removing an item from the ItemPanel
	 * 
	 * @param object
	 *            - the Object to remove
	 */
	public abstract void removeItem(Object object);
}
