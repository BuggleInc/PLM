package jlm.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lecture;
import jlm.core.model.lesson.Lesson;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


public class LessonNavigatorDialog extends JDialog  {

	private static final long serialVersionUID = -1800747039420103759L;
	Lesson lesson;

	public LessonNavigatorDialog(JFrame parent) {
		super();

		lesson = Game.getInstance().getCurrentLesson();
		setTitle("Your progress on lesson "+ lesson.getName());

		graph = lesson.getExercisesGraph();
		initComponents();

		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		setMinimumSize(new Dimension(100,100));
	}

	/**
	 * the graph
	 */
	Graph<Lecture,Integer> graph;

	Factory<DirectedGraph<String,Integer>> graphFactory = 
		new Factory<DirectedGraph<String,Integer>>() {

		public DirectedGraph<String, Integer> create() {
			return new DirectedSparseMultigraph<String,Integer>();
		}
	};

	Factory<Tree<String,Integer>> treeFactory =
		new Factory<Tree<String,Integer>> () {

		public Tree<String, Integer> create() {
			return new DelegateTree<String,Integer>(graphFactory);
		}
	};

	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationViewer<Lecture,Integer> vv;


	String root;

	Layout<Lecture, Integer> layout;
	Lecture selectedLect;
	JSplitPane mainPane;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initComponents() {

		if (mainPane == null) {
			mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			setContentPane(mainPane);
			mainPane.setOneTouchExpandable(true);
			double weight = 0.5;
			mainPane.setResizeWeight(weight);
		} else {
			mainPane.removeAll();
		}
		//mainPane.setDividerLocation((int) (1024 * weight));

		// FIXME: kill dead code
		//		layout = new DAGLayout<LectureExercise,Integer>(graph);
		/*DAGLayout<Exercise, Integer> dagLayout = (DAGLayout<Exercise, Integer>) layout;
		// Sets the force multiplier for this instance. 
		// This value is used to specify how strongly an edge "wants" to be its default length 
		// (higher values indicate a greater attraction for the default length), which affects 
		// how much its endpoints move at each timestep. The default value is 1/3. 
		// 
		// A value of 0 turns off any attempt by the layout to cause edges to conform to the default length.
		// 
		// Negative values cause long edges to get longer and short edges to get shorter; use at your own risk.
		dagLayout.setForceMultiplier(60);
		dagLayout.setStretch(.1);
		dagLayout.setRoot(Game.getInstance().getCurrentLesson().getRootExo());
		 */			
		/*
		FRLayout<LectureExercise, Integer> frLayout = new FRLayout<LectureExercise,Integer>(graph);
		frLayout.setAttractionMultiplier(10);
		frLayout.setRepulsionMultiplier(10);
		frLayout.setMaxIterations(500000);
		layout = frLayout;
		 */
		try {
			layout = new TreeLayout<Lecture, Integer>((Forest<Lecture, Integer>) graph);
		} catch (IllegalArgumentException iae) {
			System.out.println("The dependency graph of this exercise does not seem to be a tree (got '"+iae.getMessage()+"'). Switching to ISOMlayout.");
			layout = new ISOMLayout<Lecture, Integer>(graph);
		}


		vv =  new VisualizationViewer<Lecture,Integer>(layout, new Dimension(600,600));
		vv.setBackground(Color.white);
		vv.setMinimumSize(new Dimension(50, 50));
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());

		//      vv.getRenderContext().setVertexShapeTransformer(new Transformer<Exercise,Shape>() {
		//			@Override
		//			public Shape transform(Exercise exo) {
		//				if (exo.isSuccessfullyPassed()) {
		//					return new StarPolygon(0, 0, /* outer*/15, /* inner R */7, /* #vertexes*/ 5, /* start angle */50);
		//				}
		//				return new Ellipse2D.Double(-10, -10, 20, 20);
		//			}
		//      	
		//      });

		vv.getRenderContext().setVertexIconTransformer(new Transformer<Lecture, Icon>() {
			@Override
			public Icon transform(Lecture lect) {	
				ImageIcon ico = null;				
				if (lect instanceof Exercise) {
					Exercise exo = (Exercise) lect;
					if (exo.isSuccessfullyPassed()) {
						ico = ResourcesCache.getStarIcon(exo.getWorld(0).getView()[0].getIcon(), exo.getWorld(0).getView()[0].getClass().getCanonicalName());					
					} else {
						ico = exo.getWorld(0).getView()[0].getIcon();
					}
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

		// add a listener for ToolTips
		vv.setVertexToolTipTransformer(new ToStringLabeller()); 
		vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));

		final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(graphMouse);

		final GraphZoomScrollPane graphViewPane = new GraphZoomScrollPane(vv);
		mainPane.setLeftComponent(graphViewPane);

		final JPanel exoWorldViews = new JPanel(new MigLayout("insets 0 0 0 0"));

		selectedLect = Game.getInstance().getCurrentLesson().getCurrentExercise();

		final JTextArea exoNameArea = new JTextArea(selectedLect.getName());
		exoNameArea.setEditable(false);

		final JEditorPane missionArea = new JEditorPane("text/html", "");
		missionArea.setEditorKit(new JlmHtmlEditorKit());
		missionArea.setEditable(false);

		JPanel rightPane = new JPanel(new MigLayout("insets 0 0 0 0","[fill]"));
		mainPane.setRightComponent(rightPane);

		JScrollPane missionAreaScrolled = new JScrollPane(missionArea);
		missionAreaScrolled.setPreferredSize(new Dimension(100,500));

		rightPane.add(exoNameArea,"wrap");
		rightPane.add(missionAreaScrolled,"grow, gpy 0, wrap");
		rightPane.add(exoWorldViews,"grow,gpy 200,push,wrap");

		vv.getPickedVertexState().addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				Lecture lect = (Lecture) e.getItem();
				if (vv.getPickedVertexState().isPicked(lect)) {
					if (selectedLect != null && vv.getPickedVertexState().isPicked(selectedLect)) {
						vv.getPickedVertexState().pick(selectedLect,false); // fight multiple selection
					}
					selectedLect = lect;
					missionArea.setText(lect.getMission(Game.getProgrammingLanguage()));
					missionArea.setCaretPosition(0);

					exoWorldViews.removeAll();
					if (lect instanceof Exercise) 
						for (int i=0;i<((Exercise)lect).getAnswerWorld().size();i++) {
							for (WorldView wv: ((Exercise)lect).getWorld(i).getView()) {
								wv.setMinimumSize(new Dimension(50,50));
								wv.setPreferredSize(new Dimension(200,200));
								wv.setToolTipText("World"+wv.getTabName());
								exoWorldViews.add(wv,"grow,push");
							}
							for (WorldView wv: ((Exercise)lect).getAnswerOfWorld(i).getView()) {
								wv.setMinimumSize(new Dimension(50,50));
								wv.setPreferredSize(new Dimension(200,200));
								wv.setToolTipText("Objective"+wv.getTabName());
								exoWorldViews.add(wv,"grow,push,span");
							}
						}

					exoNameArea.setText(lect.getName());
				} else {
					selectedLect = null;
				}
			}

		});
		vv.getPickedVertexState().pick(selectedLect, false);
		vv.getPickedVertexState().pick(selectedLect, true);


		final ScalingControl scaler = new CrossoverScalingControl();
		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1/1.1f, vv.getCenter());
			}
		});

		JPanel scaleGrid = new JPanel(new GridLayout(1,0));
		scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));
		scaleGrid.add(plus);
		scaleGrid.add(minus);

		JButton goBtn = new JButton("Let's go!");
		goBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedLect != null) { 
					Game.getInstance().setCurrentExercise(selectedLect);
					Game.getInstance().setCurrentLesson(lesson);
				}
				dispose();
			}
		});
		JButton closeBtn = new JButton("Abort");
		closeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel controls = new JPanel();
		controls.add(goBtn);
		controls.add(closeBtn);

		rightPane.add(controls, "center");

		doLayout();
		setVisible(true);
		pack();
	}

}

/* FIXME: better location */
class StarPolygon extends Polygon {
	private static final long serialVersionUID = 1L;

	public StarPolygon(int x, int y, int r, int innerR, int vertexCount) {
		this(x, y, r, innerR, vertexCount, 0);
	}
	public StarPolygon(int x, int y, int r, int innerR, int vertexCount, double startAngle) {
		super(getXCoordinates(x, y, r, innerR,  vertexCount, startAngle)
				,getYCoordinates(x, y, r, innerR, vertexCount, startAngle)
				,vertexCount*2);
	}

	protected static int[] getXCoordinates(int x, int y, int r, int innerR, int vertexCount, double startAngle) {
		int res[]=new int[vertexCount*2];
		double addAngle=2*Math.PI/vertexCount;
		double angle=startAngle;
		double innerAngle=startAngle+Math.PI/vertexCount;
		for (int i=0; i<vertexCount; i++) {
			res[i*2]=(int)Math.round(r*Math.cos(angle))+x;
			angle+=addAngle;
			res[i*2+1]=(int)Math.round(innerR*Math.cos(innerAngle))+x;
			innerAngle+=addAngle;
		}
		return res;
	}

	protected static int[] getYCoordinates(int x, int y, int r, int innerR, int vertexCount, double startAngle) {
		int res[]=new int[vertexCount*2];
		double addAngle=2*Math.PI/vertexCount;
		double angle=startAngle;
		double innerAngle=startAngle+Math.PI/vertexCount;
		for (int i=0; i<vertexCount; i++) {
			res[i*2]=(int)Math.round(r*Math.sin(angle))+y;
			angle+=addAngle;
			res[i*2+1]=(int)Math.round(innerR*Math.sin(innerAngle))+y;
			innerAngle+=addAngle;
		}
		return res;
	}
}