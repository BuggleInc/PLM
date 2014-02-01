package plm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.model.Game;

public class FeedbackDialog extends JDialog {

	private static final long serialVersionUID = 0;
	private static FeedbackDialog instance = null;

	public I18n i18n = I18nFactory.getI18n(getClass(), "org.plm.i18n.Messages", getLocale(), I18nFactory.FALLBACK);
	final JEditorPane feedback = new JEditorPane();

	public static FeedbackDialog getInstance() {
		if (FeedbackDialog.instance == null) {
			FeedbackDialog.instance = new FeedbackDialog();
		}
		FeedbackDialog.instance.feedback.setText(FeedbackDialog.instance.i18n.tr("(your feedback comes here)"));

		return FeedbackDialog.instance;
	}

	private FeedbackDialog() {
		super(MainFrame.getInstance(), "Report your feedback", false);
		this.setTitle(i18n.tr("Report your feedback"));
		initComponent();
	}

	public void initComponent() {

		setLayout(new BorderLayout());
		JEditorPane explain = new JEditorPane("text/html", "");
		explain.setText(i18n.tr(
			"<html><p>Thanks for your feedback on PLM. We deeply need this to make the tool match <br>"
			+ "your needs, so please don't hesitate to report any suggestion, such as typos and <br/>"
			+ "unclear parts in the mission texts, other improvement to the existing exercises<br/>"
			+ "or prospective exercises. We will do our best to integrate your suggestions.</p>"
			+ "<p>Please write here your suggestion (if possible in english or french), with all<br/>"
			+ "necessary details, and then click on 'Send' below.</p>"
			+ "<p><b>Please provide your email address so that we can contact you back</b> but <br/>"
			+ "NEVER DISCLOSE A PASSWORD while reporting issues.</p>"
			+ "<p>Note that some technical information (such as your version of PLM and Java) will <br/>"
			+ "automatically be added to your feedback. None of these automatic information <br/>"
			+ "are personal and you still have to identify yourself if you want to.</p>"
			+ "<p>Alternatively, you can use the <a href='http://github.com/oster/PLM/issues'>github interface</a> for feedback.</p></html>"));
		explain.setBackground(new Color(235, 235, 235));
		explain.setOpaque(true);
		explain.setEditable(false);
		add(explain, BorderLayout.NORTH);

		feedback.setBackground(Color.white);
		feedback.setOpaque(true);
		feedback.setEditable(true);
		JScrollPane jsp = new JScrollPane(feedback);
		jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(jsp, BorderLayout.CENTER);

		feedback.setContentType("text/plain");
		feedback.setText(i18n.tr("(your feedback comes here)"));

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
				issue.setTitle("PLM");
				issue.setBody(Game.getInstance().getCurrentLesson().getId() + "\n"
					+ Game.getInstance().getCurrentLesson().getCurrentExercise().getId() + "\n"
					+ Game.getProgrammingLanguage().getLang() + "\n"
					+ Game.getInstance().getLocale().getDisplayName() + "\n"
					+ "java : " + System.getProperty("java.version") + " (VM: " + System.getProperty("java.vm.name") + "; version: " + System.getProperty("java.vm.version") + ")" + "\n"
					+ "os : " + System.getProperty("os.name") + " (version: " + System.getProperty("os.version") + "; arch: " + System.getProperty("os.arch") + ")" + "\n"
					+ "plm : " + Game.getProperty("plm.major.version", "internal", false) + " (" + Game.getProperty("plm.minor.version", "internal", false) + ")" + "\n"
					+ "text :\n" + feedback.getText()
				);
				IssueService issueService = new IssueService(client);
				try {
					issueService.createIssue(Game.getProperty("plm.github.user"), Game.getProperty("plm.github.repo"), issue);
					JOptionPane.showMessageDialog(sendBtn, i18n.tr("Message send"), i18n.tr("Success"), JOptionPane.INFORMATION_MESSAGE);
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
