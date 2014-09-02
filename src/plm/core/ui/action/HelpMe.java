package plm.core.ui.action;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import plm.core.model.Game;
import plm.core.ui.ResourcesCache;
import plm.core.utils.FileUtils;

/**
 * Class that handle clicks on HELP button It sends a request to the PLM server
 */
public class HelpMe extends AbstractGameAction {

	private static final long serialVersionUID = 1L;
	private I18n i18n = I18nFactory.getI18n(getClass(), "org.plm.i18n.Messages", FileUtils.getLocale(), I18nFactory.FALLBACK);

	private boolean isRequestingHelp = false;

	private long lastCallID;

	public HelpMe(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		isRequestingHelp = !isRequestingHelp;

		LinkedHashMap obj = new LinkedHashMap();
		obj.put("uuid", "" + Game.getInstance().getUsers().getCurrentUser().getUserUUID()); // ""+ to display the String
		try {
			obj.put("hostname", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException ex) {
			obj.put("hostname", "unknown");
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		obj.put("date", dateFormat.format(cal.getTime()));
		obj.put("action", isRequestingHelp ? "add" : "remove");
		String studentInput = "";
		if(isRequestingHelp) {
			studentInput = (String) JOptionPane.showInputDialog(
					null,
					"Detailed question :",
					"Call for help",
					JOptionPane.PLAIN_MESSAGE);

			//If a string was returned, say so.
			if (!(studentInput != null)) {
				studentInput= ""; 
			} else {
				studentInput = " : " + studentInput;
			}
		}
		obj.put("details", Game.getInstance().getCurrentLesson().getCurrentExercise().getName() + studentInput);
		if (!isRequestingHelp) {
			obj.put("callID", lastCallID + "");
		}
		String payload = JSONValue.toJSONString(obj);
		//System.out.println("JSON string : " + payload);
		String urlStr = Game.getProperty("plm.play.server.url") + "callHelp";
		//String urlStr = "http://localhost:9000/callHelp";

		String line;
		StringBuffer jsonString = new StringBuffer();

		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(payload);
			writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();

			JSONParser parser = new JSONParser();
			Object objResponse = parser.parse(jsonString.toString());
			Map map = (Map) objResponse;

			String status = (String) map.get("status");

			switch (status) {
				case "KO":
					String message = (String) map.get("message");
					System.out.println(message);
					if (isRequestingHelp) {
						Game.getInstance().fireCallForHelpSpy();
					} else {
						Game.getInstance().fireCancelCallForHelpSpy();
					}
					isRequestingHelp = !isRequestingHelp;
					break;
				case "OK":
					if (map.containsKey("callID")) {
						lastCallID = Long.parseLong((String) map.get("callID"));
						System.out.println(i18n.tr("Asking to the teacher for help"));
						Game.getInstance().fireCallForHelpSpy();
					} else {
						System.out.println(i18n.tr("Cancel call for help to the teacher"));
						Game.getInstance().fireCancelCallForHelpSpy();
					}
					((JToggleButton) e.getSource()).setText(isRequestingHelp ? i18n.tr("Cancel call") : i18n.tr("Call for Help"));
					((JToggleButton) e.getSource()).setIcon(ResourcesCache.getIcon("img/btn-alert-" + (isRequestingHelp ? "on" : "off") + ".png"));
					break;
			}

		} catch (IOException | ParseException ex) {
			isRequestingHelp = false;
			((JToggleButton) e.getSource()).setText(isRequestingHelp ? i18n.tr("Cancel call") : i18n.tr("Call for Help"));
			((JToggleButton) e.getSource()).setIcon(ResourcesCache.getIcon("img/btn-alert-" + (isRequestingHelp ? "on" : "off") + ".png"));
			System.out.println(i18n.tr("Cancel call for help to the teacher"));
			Game.getInstance().fireCancelCallForHelpSpy();
			
		}
	}

}
