package lessons.lightbot.universe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import net.miginfocom.swing.MigLayout;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.session.ISourceFileListener;
import plm.core.model.session.SourceFile;
import plm.core.ui.IEditorPanel;
import plm.core.ui.ResourcesCache;
import plm.universe.Entity;
import plm.universe.IEntityStackListener;

public class LightBotEditorPanel extends JScrollPane implements IEditorPanel, ISourceFileListener, IEntityStackListener {
  private static final long serialVersionUID = 1L;
  private final Exercise exo;
  private final SourceFile srcFile;
  private final ProgrammingLanguage lang;
  private LightBotProgram program;
  private boolean editing                  = false; // the panel is the one changing the program: its choosers are already up to date
  Map<String, InstructionChooser> choosers = new HashMap<String, InstructionChooser>();
  InstructionChooser selectedChooser       = null;
  Entity tracedEntity;
  private Map<String, Icon> iconsByNames    = new HashMap<String, Icon>();
  private Map<Icon, String> iconNameByIcons = new HashMap<Icon, String>();
  private Icon[] iconList;

  public LightBotEditorPanel(Exercise exo, SourceFile srcFile, ProgrammingLanguage lang)
  {
    super();
    this.exo     = exo;
    this.srcFile = srcFile;
    this.lang    = lang;

    iconList = new Icon[LightBotInstruction.instructionNames.length];
    int i    = 0;
    for (String n : LightBotInstruction.instructionNames) {
      iconList[i] = ResourcesCache.getIcon("img/lightbot_" + n + ".png");
      iconNameByIcons.put(iconList[i], n);
      iconsByNames.put(n, iconList[i]);
      i++;
    }

    Game.getInstance().studentWork.setBodyListener(exo, lang, srcFile.getName(), this);
    sourceFileContentHasChanged();
    tracedEntity = Game.getInstance().getSelectedEntity();
    tracedEntity.addStackListener(this);
  }
  @Override public void clear()
  {
    Game.getInstance().studentWork.removeBodyListener(exo, lang, srcFile.getName());
    if (tracedEntity != null)
      tracedEntity.removeStackListener(this);
  }
  @Override public void sourceFileContentHasChanged()
  {
    if (editing)
      return;
    program = LightBotProgram.parse(exo.getEditorContent(lang, srcFile));
    choosers.clear();

    /* Main function */
    JPanel mainPanel = new JPanel();
    mainPanel.setBorder(BorderFactory.createTitledBorder("Main"));
    mainPanel.setLayout(new MigLayout("wrap 4, fill"));
    for (int i = 0; i < program.getMain().length; i++) {
      InstructionChooser chooser = new InstructionChooser(program.getMain(), i);
      choosers.put("main:" + i, chooser);
      mainPanel.add(chooser, "grow");
    }

    /* Func 1 */
    JPanel func1Panel = new JPanel();
    func1Panel.setBorder(BorderFactory.createTitledBorder("Function 1"));
    func1Panel.setLayout(new MigLayout("wrap 4, fill"));
    for (int i = 0; i < program.getFunc1().length; i++) {
      InstructionChooser chooser = new InstructionChooser(program.getFunc1(), i);
      choosers.put("func1:" + i, chooser);
      func1Panel.add(chooser, "grow");
    }

    /* Func 2 */
    JPanel func2Panel = new JPanel();
    func2Panel.setBorder(BorderFactory.createTitledBorder("Function 2"));
    func2Panel.setLayout(new MigLayout("wrap 4, fill"));
    for (int i = 0; i < program.getFunc2().length; i++) {
      InstructionChooser chooser = new InstructionChooser(program.getFunc2(), i);
      choosers.put("func2:" + i, chooser);
      func2Panel.add(chooser, "grow");
    }
    for (InstructionChooser ic : choosers.values())
      ic.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

    /* Put everything together */
    JPanel global = new JPanel();
    global.setLayout(new MigLayout("wrap 1, fill"));
    global.add(mainPanel, "grow");
    global.add(func1Panel, "grow");
    global.add(func2Panel, "grow");
    setViewportView(global);
    doLayout();
  }

  private class InstructionChooser extends JComboBox<Icon> {
    public InstructionChooser(final LightBotInstruction[] func, final int pos)
    {
      super(iconList);
      setSelectedItem(iconsByNames.get(func[pos].toString()));
      this.addActionListener(new ActionListener() {
        @Override public void actionPerformed(ActionEvent arg0)
        {
          func[pos] = new LightBotInstruction(iconNameByIcons.get((Icon)InstructionChooser.this.getSelectedItem()));
          editing   = true;
          try {
            exo.setEditorContent(lang, srcFile, program.toString());
          } finally {
            editing = false;
          }
        }
      });
    }

    private static final long serialVersionUID = 7308818147531552628L;
  }

  @Override public void entityTraceChanged(Entity e, StackTraceElement[] trace)
  {
    if (selectedChooser != null)
      selectedChooser.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    if (trace != null && trace[0] != null) {
      selectedChooser = choosers.get(trace[0].getMethodName() + ":" + trace[0].getLineNumber());
      if (selectedChooser != null)
        selectedChooser.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }
  }

  @Override public void tracedEntityChanged(Entity e)
  {
    if (tracedEntity != null)
      tracedEntity.removeStackListener(this);
    tracedEntity = e;
    tracedEntity.addStackListener(this);
    entityTraceChanged(e, tracedEntity.getCurrentStack());
  }
}
