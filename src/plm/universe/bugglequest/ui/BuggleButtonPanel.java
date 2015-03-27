package plm.universe.bugglequest.ui;

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
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import plm.core.model.Game;
import plm.core.utils.ColorMapper;
import plm.universe.EntityControlPanel;
import plm.universe.bugglequest.AbstractBuggle;
import plm.universe.bugglequest.exception.BuggleWallException;

public class BuggleButtonPanel extends EntityControlPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private JButton fButton;
	private JButton bButton;
	private JButton rButton;
	private JButton lButton;
	private JToggleButton brushButton;
	private JComboBox<Color> brushColorComboBox;
	private JLabel lBrushColor;
	private AbstractBuggle buggle;
	
	public BuggleButtonPanel(Game game) {
		super(game);
		setLayout(new FlowLayout());
		
		initializeButtons();
		initializeColorsBoxes();

		add(createButtonsPanel());
		add(createColorsBoxes());

		getGame().addHumanLangListener(this);
		buggle = (AbstractBuggle)getGame().getSelectedEntity();
		buggle.addObserver(this);
	}

	/**
	 * Initialize fButton, bButton, rButton, lButton and brushButton
	 */
	private void initializeButtons() {
		fButton = new JButton(Game.i18n.tr("forward"));
		fButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					echo(Game.i18n.tr("forward()"));
					((AbstractBuggle)getGame().getSelectedEntity()).forward();
				} catch (BuggleWallException e) {
					showWallHuggingErrorMessageDialog();
					// e.printStackTrace();
					//game.getOutputWriter().log(e);
				}
			}
		});
		fButton.setMnemonic(KeyEvent.VK_UP);

		bButton = new JButton(Game.i18n.tr("backward"));
		bButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					echo(Game.i18n.tr("backward()"));
					((AbstractBuggle)getGame().getSelectedEntity()).backward();
				} catch (BuggleWallException e) {
					showWallHuggingErrorMessageDialog();
					// e.printStackTrace();
					// game.getOutputWriter().log(e);
				}
			}
		});
		bButton.setMnemonic(KeyEvent.VK_DOWN);

		lButton = new JButton(Game.i18n.tr("left"));
		lButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				echo(Game.i18n.tr("left()"));
				((AbstractBuggle)getGame().getSelectedEntity()).left();
			}
		});
		lButton.setMnemonic(KeyEvent.VK_LEFT);

		rButton = new JButton(Game.i18n.tr("right"));
		rButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				echo(Game.i18n.tr("right()"));
				((AbstractBuggle)getGame().getSelectedEntity()).right();
			}
		});
		rButton.setMnemonic(KeyEvent.VK_RIGHT);

		brushButton = new JToggleButton(Game.i18n.tr("mark"));
		brushButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				AbstractBuggle b = (AbstractBuggle)getGame().getSelectedEntity();
				if (b.isBrushDown()) {
					echo(Game.i18n.tr("brushUp()"));
					b.brushUp();
				} else {
					echo(Game.i18n.tr("brushDown()"));
					b.brushDown();
				}
			}
		});
		brushButton.setMnemonic(KeyEvent.VK_SPACE);
		brushButton.setSelected(((AbstractBuggle)(getGame().getSelectedEntity())).isBrushDown());
	}

	/**
	 * Initialize buggleColorComboBox and brushColorComboBox
	 */
	private void initializeColorsBoxes() {
		Color[] colors = {Color.BLUE, Color.BLACK, Color.CYAN, Color.DARK_GRAY,
				  Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, 
				  Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW};
		
		brushColorComboBox=new JComboBox<Color>(colors);
		brushColorComboBox.setRenderer(new BuggleColorCellRenderer());
		brushColorComboBox.setSelectedItem(((AbstractBuggle)getGame().getSelectedEntity()).getBrushColor());
		brushColorComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> cb = (JComboBox<?>) event.getSource();
				Color c = (Color) cb.getSelectedItem();
				cb.setSelectedItem(c);
				echo(Game.i18n.tr("setBrushColor(Color.{0})",ColorMapper.color2name(c)));
				((AbstractBuggle)getGame().getSelectedEntity()).setBrushColor(c);
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
		
		return colorsPanel;
	}
	
	@Override
	public void setEnabledControl(boolean enabled) {
		fButton.setEnabled(enabled);
		bButton.setEnabled(enabled);
		lButton.setEnabled(enabled);
		rButton.setEnabled(enabled);
		brushButton.setEnabled(enabled);
		brushColorComboBox.setEnabled(enabled);
	}
	
	public void showWallHuggingErrorMessageDialog() {
		String message ;
		String title = Game.i18n.tr("Wall hugging error");
		message = Game.i18n.tr("Your buggle has collided with a wall, it hurts a lot ! ='(");
		JOptionPane.showMessageDialog(null, message,title, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		bButton.setText(Game.i18n.tr("backward"));
		fButton.setText(Game.i18n.tr("forward"));
		lButton.setText(Game.i18n.tr("left"));
		rButton.setText(Game.i18n.tr("right"));
		brushButton.setText(Game.i18n.tr("mark"));
		lBrushColor.setText(Game.i18n.tr("Brush Color"));
		//lBuggleColor.setText(i18n.tr("Buggle Color"));
	}

	@Override
	public void update(Observable o, Object arg) {
		int source = (Integer) arg;
        switch (source) {
			case AbstractBuggle.BRUSH_COLOR:
				brushColorComboBox.setSelectedItem(buggle.getBrushColor());
				break;
			case AbstractBuggle.BRUSH_STATE:
				brushButton.setSelected(buggle.isBrushDown());
				break;
		}
	}
	
	@Override
	public void dispose() {
		getGame().removeHumanLangListener(this);
	}
}
