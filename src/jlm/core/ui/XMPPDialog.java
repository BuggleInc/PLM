package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jlm.core.model.Game;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

public class XMPPDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private static String XMPP_HOSTNAME = Game.getProperty("jlm.xmpp.hostname");
	private static final int XMPP_PORT = Integer.parseInt(Game.getProperty("jlm.xmpp.port"));
	
	private String USERNAME = Game.getProperty("jlm.xmpp.username");
	private String PASSWORD = Game.getProperty("jlm.xmpp.password");
		
	private String login;
	
	private XMPPConnection xmppConnection;
	private ChatManager chatManager;
	private Chat chat;
		
	private JTextArea display = new JTextArea();
	private JTextField input = new JTextField();

	public XMPPDialog() {
		super(MainFrame.getInstance(), "The JLM XMPP Chat", false);
		
		this.login = System.getenv("USER");
		if (this.login  == null)
			this.login  = System.getenv("USERNAME");
		if (this.login  == null)
			this.login  = "John Doe";	
		initConnection();
		initComponent();
	}

	public void initConnection() {
        // enable smack's debugging
		//System.setProperty("smack.debugEnabled", "true");
        //XMPPConnection.DEBUG_ENABLED = true;
        
        ConnectionConfiguration config = new ConnectionConfiguration(XMPP_HOSTNAME, XMPP_PORT);
        this.xmppConnection = new XMPPConnection(config);
 
        try {
        	this.xmppConnection.connect();
        	this.xmppConnection.login(USERNAME, PASSWORD, this.login);
        	this.xmppConnection.sendPacket(new Presence(Presence.Type.available));
			this.chatManager = xmppConnection.getChatManager();
			
			if (this.xmppConnection.isConnected()) {
		        this.chat = chatManager.createChat(USERNAME, new MessageListener() {
		            @Override
					public void processMessage(Chat chat, Message message) {
		            	String from = message.getFrom().substring(message.getFrom().indexOf("/")+1);
		                display.append(from+">: "+message.getBody()+"\n");
		            }
		        });
			}
        } catch (XMPPException ex) {
        	ex.printStackTrace();
        }
	}
	
	@Override
	public void dispose() {
		if (this.xmppConnection != null) {
			this.xmppConnection.disconnect();
			//this.xmppConnection.disconnect(new Presence(Presence.Type.unavailable));
			this.xmppConnection = null;
		}
		super.dispose();
	}

	public void initComponent() {
		setLayout(new BorderLayout());

		display.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(display);

		add(BorderLayout.CENTER, scrollPane);

		JPanel downPanel = new JPanel();
		downPanel.setLayout(new BorderLayout());
		downPanel.add(BorderLayout.CENTER, input);
		
		add(BorderLayout.SOUTH, downPanel);
		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (xmppConnection!=null && xmppConnection.isConnected() && chat != null && input.getText().length() > 0) {
			        try {
						chat.sendMessage(input.getText());
					} catch (XMPPException e1) {
						e1.printStackTrace();
					}					
					input.setText("");
				}
			}
		});
		input.setEditable(true);

		pack();
		setMinimumSize(new Dimension(500, 300));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(true);

		setLocationRelativeTo(getParent());
	}
}
