package treasure_hunt_webapp.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class MainUI extends UI{
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
		this.setContent(createUI());
	}

	private Component createUI() {
		return new VerticalLayout(new TextField("TextField"), new Label("Label"), new ComboBox("ComboBox"));
	}
	
}
