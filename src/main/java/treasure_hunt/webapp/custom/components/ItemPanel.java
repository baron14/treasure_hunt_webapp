package treasure_hunt.webapp.custom.components;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Class for wrapping GUI content in a collapsible panel
 */

public abstract class ItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private Label iconLabel;
	protected VerticalLayout content;

	private VerticalLayout contentWrapper;

	public ItemPanel(String headerText) {
		this.content = new VerticalLayout();
		this.content.setSpacing(true);

		this.setSizeFull();
		this.setStyleName(ValoTheme.PANEL_BORDERLESS);
		this.init(headerText);
	}

	/**
	 * Initialises the GUI components
	 */
	private void init(String headerText) {
		// The footer space that holds the 'Add' button for adding more items to
		// the ItemPanel
		Label addPadding = new Label();
		addPadding.setWidth("15px");
		HorizontalLayout footer = new HorizontalLayout(addPadding, createAdd());

		contentWrapper = new VerticalLayout(content);
		contentWrapper.addStyleName("smallMargin");
		contentWrapper.setMargin(false);

		// The Label that holds the current Icon image - either minimise or
		// maximise - as an HTML element
		iconLabel = new Label();
		iconLabel.setContentMode(ContentMode.HTML);
		iconLabel.setValue(FontAwesome.MINUS_CIRCLE.getHtml());

		// The Icon component that can be clicked on to minimse/maximise the
		// content panel. When clicked, the visibility of the content and the
		// image for the min/max icon is flipped
		VerticalLayout iconLayout = new VerticalLayout(iconLabel);

		// The header component that holds the clickable icon and the header
		// text
		HorizontalLayout header = new HorizontalLayout(iconLayout, new Label(
				headerText));
		header.setWidth("500px");
		header.setMargin(true);
		Panel headerPanel = new Panel(header);
		headerPanel.setSizeUndefined();

		header.addLayoutClickListener(event -> {
			contentWrapper.setVisible(!contentWrapper.isVisible());
			footer.setVisible(!footer.isVisible());
			iconLabel.setValue(contentWrapper.isVisible() ? FontAwesome.MINUS_CIRCLE
					.getHtml() : FontAwesome.PLUS_CIRCLE.getHtml());
		});
		
		VerticalLayout inner = new VerticalLayout(headerPanel, contentWrapper,
				footer);
		inner.setSizeFull();

		Label padding = new Label();
		padding.setWidth("50px");

		// Create the root GUI component and add the header, content and footer
		// components
		HorizontalLayout main = new HorizontalLayout(inner);
		main.setSizeFull();
		main.setExpandRatio(inner, 3f);
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
	 * @param newItemContent
	 */
	public void addItemInner(VerticalLayout newItemContent) {
		// The label with the 'Delete' image as an HTML element
		Label delete = new Label();
		delete.setContentMode(ContentMode.HTML);
		delete.setValue(FontAwesome.MINUS.getHtml());
		VerticalLayout deleteLayout = new VerticalLayout(delete);

		contentWrapper.setMargin(true);
		
		Label padding = new Label();
		padding.setWidth("20px");
		padding.setHeight("20px");

		// The root GUI component that holds the delete and add components
		HorizontalLayout main = new HorizontalLayout(deleteLayout,
				newItemContent);
		main.setSpacing(true);
		if (content.getComponentCount() != 0)
			content.addComponent(padding);
		content.addComponent(main);

		// Add the ClickListener that removes the element when the delete
		// component is clicked
		deleteLayout.addLayoutClickListener(event -> {
			ConfirmDialog.show(UI.getCurrent(), "Delete?",
					"Are you sure you want to delete this item?", "Yes", "No",
					(org.vaadin.dialogs.ConfirmDialog.Listener) dialog -> {
						if (dialog.isConfirmed()) {
							content.removeComponent(main);
							content.removeComponent(padding);
							removeItemOuter(newItemContent.getData());
						}
					});
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
	
	public void removeItemOuter(Object object){		
		removeItem(object);
		if(content.getComponentCount()==0) contentWrapper.setMargin(false);		
	}
}
