package plm.universe.bugglequest.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import plm.core.HumanLangChangesListener;
import plm.core.lang.primitives.PrimitivePot;
import plm.core.model.Game;
import plm.core.utils.ColorMapper;
import plm.universe.EntityControlPanel;
import plm.universe.bugglequest.AbstractBuggle;
import plm.universe.bugglequest.exception.BuggleWallException;

public class BuggleButtonPanel extends EntityControlPanel implements Observer, HumanLangChangesListener {

  private static final long serialVersionUID = 1L;
  private JButton fButton;
  private JButton bButton;
  private JButton rButton;
  private JButton lButton;
  private JToggleButton brushButton;
  private JComboBox<Color> brushColorComboBox;
  private JLabel lBrushColor;
  private AbstractBuggle buggle;

  public BuggleButtonPanel()
  {
    setLayout(new FlowLayout());

    initializeButtons();
    initializeColorsBoxes();

    add(createButtonsPanel());
    add(createColorsBoxes());
    currentHumanLanguageHasChanged(null);

    Game that = Game.getInstance();
    that.addHumanLangListener(this);
    buggle = (AbstractBuggle)that.getSelectedEntity();
    if (!that.isBatchExecution())
      buggle.addObserver(this);
  }

  /** Initialize fButton, bButton, rButton, lButton and brushButton */
  private void initializeButtons()
  {
    fButton = primitiveButton("step", KeyEvent.VK_UP, AbstractBuggle::step);
    bButton = primitiveButton("stepBack", KeyEvent.VK_DOWN, AbstractBuggle::stepBack);
    lButton = primitiveButton("left", KeyEvent.VK_LEFT, AbstractBuggle::left);
    rButton = primitiveButton("right", KeyEvent.VK_RIGHT, AbstractBuggle::right);

    brushButton = new JToggleButton();
    brushButton.addActionListener(event -> {
      AbstractBuggle b = (AbstractBuggle)Game.getInstance().getSelectedEntity();
      if (b.isBrushDown()) {
        echo(Game.i18n.trc(PrimitivePot.NAME_CONTEXT, "brushUp") + "()");
        b.brushUp();
      } else {
        echo(Game.i18n.trc(PrimitivePot.NAME_CONTEXT, "brushDown") + "()");
        b.brushDown();
      }
    });
    brushButton.setMnemonic(KeyEvent.VK_SPACE);
    brushButton.setSelected(((AbstractBuggle)(Game.getInstance().getSelectedEntity())).isBrushDown());
  }

  /** A button echoing the call of the primitive before calling it on the selected buggle. Its text is set by currentHumanLanguageHasChanged(). */
  private JButton primitiveButton(String primitive, int mnemonic, Consumer<AbstractBuggle> call)
  {
    JButton button = new JButton();
    button.addActionListener(event -> {
      echo(Game.i18n.trc(PrimitivePot.NAME_CONTEXT, primitive) + "()");
      try {
        call.accept((AbstractBuggle)Game.getInstance().getSelectedEntity());
      } catch (BuggleWallException e) {
        showWallHuggingErrorMessageDialog();
      }
    });
    button.setMnemonic(mnemonic);
    return button;
  }

  /**
   * Initialize buggleColorComboBox and brushColorComboBox
   */
  private void initializeColorsBoxes()
  {
    Color[] colors = {Color.BLUE,    Color.BLACK,  Color.CYAN, Color.DARK_GRAY, Color.GRAY,  Color.GREEN, Color.LIGHT_GRAY,
                      Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED,       Color.WHITE, Color.YELLOW};

    brushColorComboBox = new JComboBox<Color>(colors);
    brushColorComboBox.setRenderer(new BuggleColorCellRenderer());
    brushColorComboBox.setSelectedItem(((AbstractBuggle)Game.getInstance().getSelectedEntity()).getBrushColor());
    brushColorComboBox.addActionListener(event -> {
      Color c = (Color)brushColorComboBox.getSelectedItem();
      echo(Game.i18n.trc(PrimitivePot.NAME_CONTEXT, "setBrushColor") + "(Color." + ColorMapper.color2name(c) + ")");
      ((AbstractBuggle)Game.getInstance().getSelectedEntity()).setBrushColor(c);
    });
  }

  /**
   * Add the five buttons to a JPanel and return the JPanel
   * @return a JPanel where the five buttons have been added
   */
  private JPanel createButtonsPanel()
  {
    JPanel buttonsPanel = new JPanel();

    GridBagLayout gdLayout = new GridBagLayout();
    buttonsPanel.setLayout(gdLayout);

    GridBagConstraints c = new GridBagConstraints();
    c.insets             = new Insets(3, 3, 3, 3);

    c.gridy     = 1;
    c.gridx     = 1;
    c.gridwidth = 1;
    gdLayout.setConstraints(fButton, c);
    buttonsPanel.add(fButton);

    c.gridy = 2;
    c.gridx = 0;
    gdLayout.setConstraints(lButton, c);
    buttonsPanel.add(lButton);

    c.gridy = 2;
    c.gridx = 1;
    c.fill  = GridBagConstraints.HORIZONTAL;
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
  private JPanel createColorsBoxes()
  {
    JPanel colorsPanel = new JPanel();
    colorsPanel.setLayout(new GridLayout(4, 1));

    lBrushColor = new JLabel();
    colorsPanel.add(lBrushColor);
    colorsPanel.add(brushColorComboBox);

    return colorsPanel;
  }

  @Override public void setEnabledControl(boolean enabled)
  {
    fButton.setEnabled(enabled);
    bButton.setEnabled(enabled);
    lButton.setEnabled(enabled);
    rButton.setEnabled(enabled);
    brushButton.setEnabled(enabled);
    brushColorComboBox.setEnabled(enabled);
  }

  private void showWallHuggingErrorMessageDialog()
  {
    JOptionPane.showMessageDialog(null, Game.i18n.tr("Your buggle has collided with a wall, it hurts a lot ! ='("), Game.i18n.tr("Wall hugging error"),
                                  JOptionPane.ERROR_MESSAGE);
  }

  /** Sets the texts of the controls, in the language of Game.i18n. */
  @Override public void currentHumanLanguageHasChanged(Locale newLang)
  {
    bButton.setText(Game.i18n.tr("backward"));
    fButton.setText(Game.i18n.tr("forward"));
    lButton.setText(Game.i18n.tr("left"));
    rButton.setText(Game.i18n.tr("right"));
    brushButton.setText(Game.i18n.tr("mark"));
    lBrushColor.setText(Game.i18n.tr("Brush Color"));
  }

  @Override public void update(Observable o, Object arg)
  {
    int source = (Integer)arg;
    switch (source) {
      case AbstractBuggle.BRUSH_COLOR:
        brushColorComboBox.setSelectedItem(buggle.getBrushColor());
        break;
      case AbstractBuggle.BRUSH_STATE:
        brushButton.setSelected(buggle.isBrushDown());
        break;
    }
  }

  @Override public void dispose() { Game.getInstance().removeHumanLangListener(this); }
}
