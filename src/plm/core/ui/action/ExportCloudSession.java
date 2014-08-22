package plm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import plm.core.model.Game;
import plm.core.model.session.ZipSessionKit;
import plm.core.utils.FileUtils;

public class ExportCloudSession extends AbstractGameAction {

	private static final long serialVersionUID = 111337614106936502L;
	private Component parent;

	public ExportCloudSession(Game game, String text, ImageIcon icon, Component parent) {
		super(game, text, icon);
		this.parent = parent;
	}
	
	private void zipSessionPackage(File srcDir, File packageFile) throws IOException {
		ZipOutputStream zos = null;

		try {
			zos = new ZipOutputStream(new FileOutputStream(packageFile));
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.setLevel(Deflater.BEST_COMPRESSION);

			final int BUFFER_SIZE = 1024 * 100;
			byte[] buffer = new byte[BUFFER_SIZE];
			for (final File aFile : srcDir.listFiles()) {
				FileInputStream fis = new FileInputStream(aFile);
				zos.putNextEntry(new ZipEntry(aFile.getName()));
				int bytes;
				while ((bytes = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, bytes);
				}
				zos.closeEntry();
				fis.close();
			}
		} finally {
			if (zos != null)
				zos.close();
		}
	}

	private String uploadSessionPackage(File tmpFile) throws IOException {
		HttpClient client = new DefaultHttpClient();
		try {
			client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			String sessionCloudProviderURL = Game.getProperty(
					Game.SESSION_CLOUD_PROVIDER_URL, "none", false);
			if (sessionCloudProviderURL == null || sessionCloudProviderURL.equals("none"))
				throw new IOException(Game.i18n.tr("No session cloud provider provided (property {0} is {1}). Please update your property file.",
						Game.SESSION_CLOUD_PROVIDER_URL,sessionCloudProviderURL));
			HttpPost post = new HttpPost(sessionCloudProviderURL + "/upload.php");
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			entity.addPart("session_file", new FileBody(tmpFile, "application/zip"));
			// entity.addPart("login", new StringBody("anonymous".toString(), "text/plain", Charset.forName("UTF-8")));
			post.setEntity(entity);

			String response = null;
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				response = EntityUtils.toString(res.getEntity(), "UTF-8");
			}

			if (response != null) {
				JSONObject o = (JSONObject) JSONValue.parse(response);
				if (((Boolean) o.get("success")).booleanValue()) {
					String fileID = (String) ((JSONObject) o.get("payload")).get("code");
					return fileID;
				} else {
					String errorMessage = (String) o.get("error");
					throw new IOException(errorMessage);
				}
			} else {
				throw new IOException(res.getStatusLine().getReasonPhrase());
			}
		} finally {
			client.getConnectionManager().shutdown();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		File tmpDir = null;
		File tmpFile = null;

		try {
			tmpDir = FileUtils.createTemporaryDirectory();			
			ZipSessionKit kit = new ZipSessionKit(this.game);
			kit.storeAll(tmpDir);

			tmpFile = FileUtils.createTemporaryFilename();
			zipSessionPackage(tmpDir, tmpFile);
			String fileID = uploadSessionPackage(tmpFile);

			JOptionPane.showMessageDialog(this.parent,
					Game.i18n.tr("<html>Please remember the following code:<br/><h1>{0}</h1><br/>You will need it to import your session.</html>",fileID),
					Game.i18n.tr("Export success"),
					JOptionPane.INFORMATION_MESSAGE
					);			
		} catch (Exception ex) {
			//TODO: ex.printStackTrace() should be log in debug mode
			JOptionPane.showMessageDialog(this.parent,
					Game.i18n.tr("<html>Export to cloud failed.<br/>{0}<br/>Caution! Your session is not saved yet.</html>",ex.getMessage()),
				    Game.i18n.tr("Export error"),
				    JOptionPane.ERROR_MESSAGE);	
		} finally {
			if (tmpDir != null)
				tmpDir.delete();		
			if (tmpFile != null)
				tmpFile.delete();			
		}
	}	
}
