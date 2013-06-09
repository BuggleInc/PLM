package jlm.universe.bugglequest.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import jlm.core.model.Game;
import jlm.universe.EntityControlPanel;
import jlm.universe.bugglequest.AbstractBuggle;
import jlm.universe.bugglequest.exception.BuggleWallException;

public class BuggleButtonPanel extends EntityControlPanel {

	private static final long serialVersionUID = 1L;
	private JButton fButton;
	private JButton bButton;
	private JButton rButton;
	private JButton lButton;
	private JToggleButton brushButton;
	private JComboBox buggleColorComboBox;
	private JComboBox brushColorComboBox;
	private JLabel lBuggleColor;
	private JLabel lBrushColor;
	
	public BuggleButtonPanel() {
		setLayout(new FlowLayout());
		
		initializeButtons();
		initializeColorsBoxes();

		add(createButtonsPanel());
		add(createColorsBoxes());

		Game.getInstance().addHumanLangListener(this);
	}

	/**
	 * Initialize fButton, bButton, rButton, lButton and brushButton
	 */
	private void initializeButtons() {
		fButton = new JButton(i18n.tr("forward"));
		fButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					((AbstractBuggle)Game.getInstance().getSelectedEntity()).forward();
				} catch (BuggleWallException e) {
					showWallHuggingErrorMessageDialog();
					// e.printStackTrace();
					//game.getOutputWriter().log(e);
				}
			}
		});
		fButton.setMnemonic(KeyEvent.VK_UP);

		bButton = new JButton(i18n.tr("backward"));
		bButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					((AbstractBuggle)Game.getInstance().getSelectedEntity()).backward();
				} catch (BuggleWallException e) {
					showWallHuggingErrorMessageDialog();
					// e.printStackTrace();
					// Game.getInstance().getOutputWriter().log(e);
				}
			}
		});
		bButton.setMnemonic(KeyEvent.VK_DOWN);

		lButton = new JButton(i18n.tr("turn left"));
		lButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				((AbstractBuggle)Game.getInstance().getSelectedEntity()).turnLeft();
			}
		});
		lButton.setMnemonic(KeyEvent.VK_LEFT);

		rButton = new JButton(i18n.tr("turn right"));
		rButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				((AbstractBuggle)Game.getInstance().getSelectedEntity()).turnRight();
			}
		});
		rButton.setMnemonic(KeyEvent.VK_RIGHT);

		brushButton = new JToggleButton(i18n.tr("mark"));
		brushButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				AbstractBuggle b = (AbstractBuggle)Game.getInstance().getSelectedEntity();
				if (b.isBrushDown()) {
					b.brushUp();
				} else {
					b.brushDown();
				}
			}
		});
		brushButton.setMnemonic(KeyEvent.VK_SPACE);
		brushButton.setSelected(((AbstractBuggle)(Game.getInstance().getSelectedEntity())).isBrushDown());
	}

	/**
	 * Initialize buggleColorComboBox and brushColorComboBox
	 */
	private void initializeColorsBoxes() {
		Color[] colors = {Color.BLUE, Color.BLACK, Color.CYAN, Color.DARK_GRAY,
				  Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, 
				  Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW};
		
		brushColorComboBox=new JComboBox (colors);
		brushColorComboBox.setRenderer(new BuggleColorCellRenderer());
		brushColorComboBox.setSelectedItem(((AbstractBuggle)Game.getInstance().getSelectedEntity()).getBrushColor());
		brushColorComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox cb = (JComboBox) event.getSource();
				Color c = (Color) cb.getSelectedItem();
				cb.setSelectedItem(c);
				((AbstractBuggle)Game.getInstance().getSelectedEntity()).setBrushColor(c);
			}
		});
		
		buggleColorComboBox=new JComboBox (colors);
		buggleColorComboBox.setRenderer(new BuggleColorCellRenderer());
		buggleColorComboBox.setSelectedItem(((AbstractBuggle)Game.getInstance().getSelectedEntity()).getColor());
		buggleColorComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox cb = (JComboBox) event.getSource();
				Color c = (Color) cb.getSelectedItem();
				cb.setSelectedItem(c);
				((AbstractBuggle)Game.getInstance().getSelectedEntity()).setColor(c);
			}
		});
	}

	/**
	 * Add the five buttons to a JPanel and return the JPanel
	 * @return a JPanel where the five buttons have been added
	 */
	private JPanel createButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		
		GridBagLayout gdLayout = new GridBagLayout();
		buttonsPanel.setLayout(gdLayout);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 3, 3, 3);

		c.gridy = 1;
		c.gridx = 1;
		c.gridwidth = 1;
		gdLayout.setConstraints(fButton, c);
		buttonsPanel.add(fButton);

		c.gridy = 2;
		c.gridx = 0;
		gdLayout.setConstraints(lButton, c);
		buttonsPanel.add(lButton);

		c.gridy = 2;
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		gdLayout.setConstraints(brushButton, c);
		buttonsPanel.add(brushButton);

		c.gridy = 2;
		c.gridx = 2;
		gdLayout.setConstraints(rButton, c);
		buttonsPanel.add(rButton);

		c.gridy = 3;
		c.gridx = 1;
		gdLayout.setConstraints(bButton, c);
		buttonsPanel.add(bButton);
		
		return buttonsPanel;
	}

	/**
	 * Add the two combo boxes to a JPanel and return the JPanel
	 * @return a JPanel where the two combo boxes have been added
	 */
	private JPanel createColorsBoxes() {
		JPanel colorsPanel = new JPanel();
		colorsPanel.setLayout(new GridLayout(4,1));
		
		lBrushColor = new JLabel("Brush Color");
		colorsPanel.add(lBrushColor);
		colorsPanel.add(brushColorComboBox);
		
		lBuggleColor = new JLabel("Buggle Color");
		colorsPanel.add(lBuggleColor);
		colorsPanel.add(buggleColorComboBox);
		
		return colorsPanel;
	}
	
	@Override
	public void setEnabledControl(boolean enabled) {
		fButton.setEnabled(enabled);
		bButton.setEnabled(enabled);
		lButton.setEnabled(enabled);
		rButton.setEnabled(enabled);
		brushButton.setEnabled(enabled);
		buggleColorComboBox.setEnabled(enabled);
		brushColorComboBox.setEnabled(enabled);
	}
	
	public void showWallHuggingErrorMessageDialog() {
		String message ;
		String title = i18n.tr("Wall hugging error");
		message = i18n.tr("Your buggle has collided with a wall, it hurts a lot ! ='(");
//TODO: remove if validated
//		if ( FileUtils.getLocale().equals("fr"))
//		{
//			message = "Votre buggle est rentr��e dans un mur, ��a fait mal ! ='(";
//		}
//		else
//		{
//			message = "Your buggle has collided with a wall, it hurts a lot ! ='(";
//		}
		JOptionPane.showMessageDialog(null, message,title, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		i18n.setLocale(newLang);
		
		bButton.setText(i18n.tr("backward"));
		fButton.setText(i18n.tr("forward"));
		lButton.setText(i18n.tr("turn left"));
		rButton.setText(i18n.tr("turn right"));
		brushButton.setText(i18n.tr("mark"));
		lBrushColor.setText(i18n.tr("Brush Color"));
		lBuggleColor.setText(i18n.tr("Buggle Color"));
	}
	
}
