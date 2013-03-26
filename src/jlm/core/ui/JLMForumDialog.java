package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;



public class JLMForumDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private String server="irc.debian.org";
	private String chan="#jlmlovers";

	JTextArea display = new JTextArea();
	JTextField input = new JTextField();
	JButton reconnect;
	String nick = System.getProperty("user.name");
	IRCConnection connection;
	Listener listener;

	public JLMForumDialog() {
		super(MainFrame.getInstance(), "The JLM Forum", false);
		initComponent();
		listener = new Listener(display);
	}


	public void initComponent() {
		setLayout(new BorderLayout());

		display.setEditable(false);
		display.append("(connecting to the server...)\n");

		JScrollPane scrollPane = new JScrollPane(display);

		add(BorderLayout.CENTER, scrollPane);

		JPanel downPanel = new JPanel();
		downPanel.setLayout(new BorderLayout());
		downPanel.add(BorderLayout.CENTER, input);
		
		reconnect = new JButton("Reconnect");
		reconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener!=null)
					listener.getConnected();
				else 
					listener = new Listener(display);
				display.append("(reconnecting)\n");
			}
		});
		reconnect.setEnabled(false);
			
		
		downPanel.add(BorderLayout.EAST, reconnect);
		
		add(BorderLayout.SOUTH, downPanel);
		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (connection!=null && input.getText().length() >0) {
					connection.doPrivmsg(chan, input.getText());
					display.append(nick+": "+input.getText()+"\n");
					input.setText("");
				}
			}
		});
		input.setEditable(false);

		pack();
		setMinimumSize(new Dimension(500, 300));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(true);

		setLocationRelativeTo(getParent());
	}


	public class Listener extends Thread implements IRCEventListener {
		JTextArea display;
		public Listener(JTextArea display) {
			getConnected();
			start();
			
			this.display = display;
		}
		private void getConnected() {
			connection = new IRCConnection(server, new int[] { 6667 }, ""/*pass*/, nick, nick, nick);
			connection.addIRCEventListener(this);
			connection.setEncoding("UTF-8");
			connection.setPong(true);
			connection.setDaemon(false);
			connection.setColors(false);
			setDaemon(true);
			try {
				connection.connect();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(display,
						"Cannot connect to "+server+":\n"+e.getMessage(),
						"Network Error",
						JOptionPane.ERROR_MESSAGE);
				connection=null;
			}
		}
		
		private void print(String msg) {
			display.append(msg+"\n");
		}

		public void onRegistered() {
			print("Connected");
			display.append("(joining the channel)\n");
			connection.send("JOIN "+chan);
			input.setEditable(true);
		}

		public void onDisconnected() {
			print("(Disconnected)");
			reconnect.setEnabled(true);
			input.setEditable(false);
		}

		public void onError(String msg) {
			print("Error: "+ msg);
		}

		public void onError(int num, String msg) {
			if (num==433) {
				nick=nick+"_";
				print("Nickname already in use. Change to "+nick+" and reconnecting.");
				getConnected();
			} else {
				print("Error #"+ num +": "+ msg);
			}
		}

		public void onInvite(String chan, IRCUser u, String nickPass) {
			print(u.getNick() +" invites "+ nickPass);
		}

		public void onJoin(String chan, IRCUser u) {
			print( u.getNick() +" joins");
		}

		public void onKick(String chan, IRCUser u, String nickPass, String msg) {
			print( u.getNick() +" kicks "+ nickPass);
		}

		public void onMode(IRCUser u, String nickPass, String mode) {
			//print("Mode: "+ u.getNick() +" sets modes "+ mode +" "+ nickPass);
		}

		public void onMode(String chan, IRCUser u, IRCModeParser mp) {
			//print(chan +"> "+ u.getNick() +" sets mode: "+ mp.getLine());
		}

		public void onNick(IRCUser u, String nickNew) {
			print("Nick: "+ u.getNick() +" is now known as "+ nickNew);
		}

		public void onNotice(String target, IRCUser u, String msg) {
			if (!target.equals("AUTH"))
				print(target +"> "+ u.getNick() +" (notice): "+ msg);
		}

		public void onPart(String chan, IRCUser u, String msg) {
			print(chan +"> "+ u.getNick() +" parts");
		}

		public void onPrivmsg(String chan, IRCUser u, String msg) {
			print(u.getNick() +": "+ msg);
		}

		public void onQuit(IRCUser u, String msg) {
			print("Quit: "+ u.getNick());
		}

		public void onReply(int num, String value, String msg) {
			// Do not display replies
			//print("Reply #"+ num +": "+ value +" "+ msg);
		}

		public void onTopic(String chan, IRCUser u, String topic) {
			print( u.getNick() +" changes topic into: "+ topic);
		}

		public void onPing(String p) {

		}

		public void unknown(String a, String b, String c, String d) {
			print("UNKNOWN: "+ a +" b "+ c +" "+ d);
		}
	}

}
