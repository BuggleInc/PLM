package plm.core.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;

public class ChooseLectureDialog implements TreeSelectionListener {
	private JTree tree;

	public ChooseLectureDialog() {
		Lesson l = Game.getInstance().getCurrentLesson();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		for (Lecture lect : l.getRootLectures()) 
			root.add(lect.getNode());
		
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
					icon = ResourcesCache.getStarredIcon(exo.getWorld(0).getIcon(), exo);
				} else {
					icon = ResourcesCache.getIcon("img/world_lesson.png");
					if (lect != null && (! (lect instanceof Lecture))) // null may occur -- ignore but don't fail 
						System.out.println("The exercise chooser contains something that is not a Lecture:"+lect.getClass().getName());
				}
				setIcon(icon);
				return this;
			}
		});
		
		/* Build the selection */
		tree.setExpandsSelectedPaths(true);
		TreeNode[] nodes = l.getCurrentExercise().getNode().getPath();
		TreePath path = new TreePath(nodes);
        tree.scrollPathToVisible(path);
		tree.setSelectionPath(path);
		
		JScrollPane jsp = new JScrollPane(tree);

		JPanel p = new JPanel();
		p.add(jsp);
		int result = JOptionPane.showConfirmDialog(MainFrame.getInstance(), p, Game.i18n.tr("Choose your next exercise"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			if (node != null) {
				Object selection = node.getUserObject();

				if (selection instanceof Lecture) 
					Game.getInstance().setCurrentExercise((Lecture) selection);
				else 
					System.out.println("selection is not a lecture: "+selection);
			}
		}

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

	}
}
