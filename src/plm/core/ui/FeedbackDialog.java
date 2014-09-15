package plm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;
import plm.core.model.tracking.GitUtils;

public class FeedbackDialog extends JDialog {

	private static final long serialVersionUID = 0;
	private static FeedbackDialog instance = null;

	public I18n i18n = I18nFactory.getI18n(getClass(), "org.plm.i18n.Messages", getLocale(), I18nFactory.FALLBACK);
	final JEditorPane feedback = new JEditorPane();
	final JTextField title = new JTextField();

	public static FeedbackDialog getInstance() {
		if (FeedbackDialog.instance == null) {
			FeedbackDialog.instance = new FeedbackDialog();
		}
		FeedbackDialog.instance.feedback.setText(FeedbackDialog.instance.i18n.tr(
				  "Please write your suggestion here, with all necessary details\n"
				+ "(if possible in English or French).\n\n"
				+ "When you find a typo or a sentence that is hard to understand, \n"
				+ "it really helps to suggest a new wording.\n\n"
				+ "If you encounter a technical bug, please tell us what you did,\n"
				+ "which outcome you were expecting and what happened instead.\n\n"
				+ "  but DO NEVER DISCLOSE A PASSWORD to a bug tracker. Never."
				+ "\n\n--------------------[ Technical Information ]--------------------\n"
				+ "(This can help us fixing your problem, please don't erase)\n"
				) /* The rest is not translated */
				+ "\nLesson: "+Game.getInstance().getCurrentLesson().getId() + "\n"
				+ "Exercise: "+Game.getInstance().getCurrentLesson().getCurrentExercise().getId() + "\n"
				+ "Programming Language: "+Game.getProgrammingLanguage().getLang() + "\n"
				+ "Locale: "+Game.getInstance().getLocale().getDisplayName() + "\n"
				+ "Java version: " + System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") + "; version: " + System.getProperty("java.vm.version") + ")" + "\n"
				+ "OS: " + System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")" + "\n"
				+ "PLM version: " + Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) + ")" + "\n"
				+ "Public user ID: PLM"+GitUtils.sha1(Game.getInstance().getUsers().getCurrentUser().getUserUUIDasString())+ "\n");
		
		FeedbackDialog.instance.title.setText(FeedbackDialog.instance.i18n.tr("Please describe the problem in a few words"));
		FeedbackDialog.instance.pack();
		return FeedbackDialog.instance;
	}

	private FeedbackDialog() {
		super(MainFrame.getInstance(), "Report your feedback", false);
		this.setTitle(i18n.tr("Report your feedback"));
		initComponent();
	}

	public void initComponent() {

		setLayout(new BorderLayout());
		JPanel headerToolbar = new JPanel();
		headerToolbar.add(new Label(i18n.tr("Issue title:")));
		headerToolbar.add(title);
		add(headerToolbar, BorderLayout.NORTH);

		feedback.setBackground(Color.white);
		feedback.setOpaque(true);
		feedback.setEditable(true);
		JScrollPane jsp = new JScrollPane(feedback);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(jsp, BorderLayout.CENTER);

		feedback.setContentType("text/plain");

		final JButton cancelBtn = new JButton(i18n.tr("Cancel"));
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(cancelBtn,
																 i18n.tr("Do you really want to cancel your feedback and lose any edit?"),
																 i18n.tr("are you sure?"),
																 JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});

		final JButton sendBtn = new JButton(i18n.tr("Send feedback"));
		sendBtn.addActionListener(new ActionListener() {
			GitHubClient client = new GitHubClient();

			@Override
			public void actionPerformed(ActionEvent e) {
				client.setOAuth2Token(Game.getProperty("plm.github.oauth"));
				Issue issue = new Issue();
				issue.setTitle(title.getText());
				issue.setBody(feedback.getText());
				IssueService issueService = new IssueService(client);
				try {
					Issue i = issueService.createIssue(Game.getProperty("plm.github.owner"), Game.getProperty("plm.github.repo"), issue);
					JOptionPane.showMessageDialog(sendBtn, i18n.tr(
							  "Thank you for your remark, we will do our best to integrate it.\n"
							+ "Follow our progress at {0}.",i.getHtmlUrl()), i18n.tr("Thanks for your suggestion"), JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} catch (IOException ex) {
					StringBuffer ctn = new StringBuffer(ex.getLocalizedMessage() + "\n");
					for (StackTraceElement elm : ex.getStackTrace()) {
						ctn.append(elm.toString()).append("\n");
					}
					JOptionPane.showMessageDialog(cancelBtn,
												  ctn.toString(),
												  i18n.tr("Error while uploading your feedback"),
												  JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		JPanel toolbar = new JPanel();
		toolbar.add(cancelBtn);
		toolbar.add(sendBtn);
		add(toolbar, BorderLayout.SOUTH);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
		setMinimumSize(new Dimension(200, 600));
		setPreferredSize(new Dimension(500, 800));
		setResizable(true);

		setLocationRelativeTo(getParent());
	}

}
