package treasure_hunt.webapp.custom.receivers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import treasure_hunt.data.models.route.Question;

public class UploadReciever implements Receiver, SucceededListener {
	private static final long serialVersionUID = 1L;

	private File file;
	private Embedded image;

	private Question question;

	public UploadReciever(Embedded image, Question question) {
		this.image = image;
		this.question = question;
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
		
		try {
			question.setImage(Base64.encodeBase64URLSafeString(FileUtils.readFileToByteArray(file)));
		} catch (IOException e) {
			Notification.show("There was a problem uploading the image correctly.", Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
		System.out.println(question.getImage());
	}
}