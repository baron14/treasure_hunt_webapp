package treasure_hunt.webapp.custom.receivers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class UploadReciever implements Receiver, SucceededListener {
	private static final long serialVersionUID = 1L;

	private File file;
	private Embedded image;

	public UploadReciever(Embedded image) {
		this.image = image;
	}

	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		FileOutputStream fos = null;
		try {
			// Open the file for writing.
			file = new File("/" + filename);
			fos = new FileOutputStream(file);
		} catch (final java.io.FileNotFoundException e) {
			new Notification("Could not open file: ", e.getMessage(), Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
			return null;
		}
		return fos; // Return the output stream to write to
	}

	public void uploadSucceeded(SucceededEvent event) {
		// Set the image source of the given Image object to be the newly
		// uploaded file and set visible to true
		image.setVisible(true);
		image.setSource(new FileResource(file));
	}
}