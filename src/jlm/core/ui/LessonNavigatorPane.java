package jlm.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


public class LessonNavigatorPane extends JPanel implements GameListener {

	private static final long serialVersionUID = -1800747039420103759L;
	private Lesson lesson;
	Layout<Lecture, Integer> layout;
	/**
	 * the graph
	 */
	Graph<Lecture,Integer> graph;

	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationViewer<Lecture,Integer> vv;

	private Lecture selectedLect;

	public LessonNavigatorPane() {
		super();
		lesson = Game.getInstance().getCurrentLesson();
		graph = lesson.getExercisesGraph();
		initComponent();
	}

	private void initComponent() {
		setLayout(new MigLayout("insets 0","[grow]","[grow]"));
		
		try {
			layout = new TreeLayout<Lecture, Integer>((Forest<Lecture, Integer>) graph);
		} catch (IllegalArgumentException iae) {
			System.out.println("The dependency graph of this exercise does not seem to be a tree (got '"+iae.getMessage()+"'). Switching to ISOMlayout.");
			layout = new ISOMLayout<Lecture, Integer>(graph);
		}
//		vv = new VisualizationViewer<Lecture,Integer>(layout, new Dimension(150,500));
		vv = new VisualizationViewer<Lecture,Integer>(layout);
		vv.setBackground(Color.white);
		vv.setMinimumSize(new Dimension(50, 50));
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
		vv.getRenderContext().setVertexIconTransformer(new Transformer<Lecture, Icon>() {
			@Override
			public Icon transform(Lecture lect) {
				ImageIcon ico = null;
				if (lect instanceof Exercise) {
					Exercise exo = (Exercise) lect;
//					if (false) { // FIXME 
//						ico = ResourcesCache.getStarIcon(exo.getWorld(0).getView()[0].getIcon(), exo.getWorld(0).getView()[0].getClass().getCanonicalName());
//					} else {
						ico = exo.getWorld(0).getView()[0].getIcon();
//					}
				} else {
					ico = ResourcesCache.getIcon("resources/IconWorld/lesson.png");
				}
				if (vv.getPickedVertexState().isPicked(lect)) {
					// FIXME: dirty (not cached!)
					BufferedImage combined = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
					Graphics g = combined.getGraphics();
					g.drawImage(ico.getImage(), 0, 0, null);
					g.setColor(Color.red);
					g.drawRect(0, 0, 31, 31);
					g.drawRect(1, 1, 30, 30);
					ico = new ImageIcon(combined);
				}

				return ico;
			}});
		vv.setVertexToolTipTransformer(new ToStringLabeller<Lecture>());
		vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));

		final DefaultModalGraphMouse<?, ?> graphMouse = new DefaultModalGraphMouse<Object, Object>();
		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(graphMouse);
		final GraphZoomScrollPane graphViewPane = new GraphZoomScrollPane(vv);
		this.add(graphViewPane,"wrap, growx");
		
		vv.getPickedVertexState().addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Lecture lect = (Lecture) e.getItem();
				if (vv.getPickedVertexState().isPicked(lect)) {
				if (selectedLect != null && vv.getPickedVertexState().isPicked(selectedLect)) {
				vv.getPickedVertexState().pick(selectedLect,false); // fight multiple selection
				}
				selectedLect = lect;
				Game.getInstance().setCurrentExercise(selectedLect);
				Game.getInstance().setCurrentLesson(lesson);
			}
			}
			
		});
	}

	@Override
	public void currentLessonHasChanged() { /* DON'T CARE */	}

	@Override
	public void currentExerciseHasChanged(Lecture lect) {
		lesson = Game.getInstance().getCurrentLesson();
		graph = lesson.getExercisesGraph();
		try {
			layout = new TreeLayout<Lecture, Integer>((Forest<Lecture, Integer>) graph);
		} catch (IllegalArgumentException iae) {
			System.out.println("The dependency graph of this exercise does not seem to be a tree (got '"+iae.getMessage()+"'). Switching to ISOMlayout.");
			layout = new ISOMLayout<Lecture, Integer>(graph);
		}
		vv.setGraphLayout(layout);
		vv.repaint();
		repaint();
		selectedLect = lect;
	}

	@Override
	public void selectedWorldHasChanged(World newWorld) { /* DON'T CARE */}

	@Override
	public void selectedEntityHasChanged() { /* DON'T CARE */	}

	@Override
	public void selectedWorldWasUpdated() { /* DON'T CARE */}

}