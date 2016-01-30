package treasure_hunt_webapp.utils;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class ComponentUtil {
	public static Label embedIcon(FontAwesome icon){
		Label label = new Label();
		label.setContentMode(ContentMode.HTML);
		label.setValue(icon.getHtml());
		return label;
	}
	
	public static HorizontalLayout horizBuilder(Component ... com){
		HorizontalLayout main = new HorizontalLayout();
		
		for(Component c : com) main.addComponent(c);
		
		return main;
	}
}
