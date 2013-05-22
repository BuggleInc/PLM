package jlm.core.ui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import jlm.core.HumanLangChangesListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import jlm.core.ui.ResourcesCache;


public class SwitchExo extends AbstractGameAction implements HumanLangChangesListener, TreeSelectionListener {

	private static final long serialVersionUID = 5113775865916404566L;
	private JTree tree;

	public SwitchExo(Game game, String text, ImageIcon icon) {
		super(game, text, icon);
		game.addHumanLangListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Lesson l = Game.getInstance().getCurrentLesson();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		for (Lecture lect : l.getRootLectures())  
			root.add(lect.makeNode());
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object o,
					boolean selected, boolean expanded, boolean leaf, int row,
					boolean hasFocus) {
		        super.getTreeCellRendererComponent(tree, o, selected, expanded, leaf, row, hasFocus);
		        Object lect = ((DefaultMutableTreeNode)o).getUserObject();
		        
				ImageIcon icon = null;
				if (lect instanceof Exercise) {
					Exercise exo = (Exercise) lect;
//					if (exo.passed()) { // FIXME 
//						ico = ResourcesCache.getStarIcon(exo.getWorld(0).getView()[0].getIcon(), exo.getWorld(0).getView()[0].getClass().getCanonicalName());
//					} else {
						icon = exo.getWorld(0).getView().getIcon();
//					}
				} else {
					icon = ResourcesCache.getIcon("resources/IconWorld/lesson.png");
					if (lect != null && (! (lect instanceof Lecture))) // null may occur -- ignore but don't fail 
						System.out.println("The exercise chooser contains something that is not a Lecture:"+lect.getClass().getName());
				}
				setIcon(icon);
				return this;
			}
		});
		JScrollPane jsp = new JScrollPane(tree);

		JPanel p = new JPanel();
		p.add(jsp);
		int result = JOptionPane.showConfirmDialog(null, p, "Choose your next exercise",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			if (node != null) {
				Object selection = node.getUserObject();

				if (selection instanceof Lecture) {
					game.setCurrentExercise((Lecture) selection);
				}
				else 
					System.out.println("selection is no lecture: "+selection);
			} else {
				System.out.println("Nothing selected; not switching");
			}
		}
	}

	@Override
	public void currentHumanLanguageHasChanged(Locale newLang) {
		setDescription(i18n.tr("Switch to another exercise"),"");
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

	}
}
