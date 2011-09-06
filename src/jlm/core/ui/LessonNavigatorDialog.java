package jlm.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

import jlm.core.GameListener;
import jlm.core.model.Game;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.Lesson;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
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


public class LessonNavigatorDialog extends JFrame implements GameListener {

	private static final long serialVersionUID = -1800747039420103759L;
	
	public LessonNavigatorDialog(JFrame parent) {
		super();
		
		currentLessonHasChanged();
		Game.getInstance().addGameListener(this);

		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
	}

	/* elements of the GameListener interface that we do not care about */
	public void currentExerciseHasChanged() {}
	public void lessonsChanged() {}
	public void selectedEntityHasChanged() {}
	public void selectedWorldHasChanged() {}
	public void selectedWorldWasUpdated() {}

	
    /**
     * the graph
     */
    Graph<Exercise,Integer> graph;
    
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
    VisualizationViewer<Exercise,Integer> vv;
    
    
    String root;
    
    Layout<Exercise, Integer> layout;
    Exercise selectedExo;
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

		layout = new DAGLayout<Exercise,Integer>(graph);
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
		FRLayout<Exercise, Integer> frLayout = new FRLayout<Exercise,Integer>(graph);
		frLayout.setAttractionMultiplier(10);
		frLayout.setRepulsionMultiplier(10);
		frLayout.setMaxIterations(500000);
		layout = frLayout;
		
		try {
			layout = new TreeLayout<Exercise, Integer>((Forest<Exercise, Integer>) graph);
		} catch (IllegalArgumentException iae) {
			System.out.println("The dependency graph of this exercise does not seem to be a tree (got '"+iae.getMessage()+"'). Switching to ISOMlayout.");
			layout = new ISOMLayout<Exercise, Integer>(graph);
		}
		
		
        vv =  new VisualizationViewer<Exercise,Integer>(layout, new Dimension(600,600));
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexShapeTransformer(new Transformer<Exercise,Shape>() {

			@Override
			public Shape transform(Exercise exo) {
				if (exo.isSuccessfullyPassed()) {
					return new StarPolygon(0, 0, /* outer*/15, /* inner R */7, /* #vertexes*/ 5, /* start angle */50);
				}
				return new Ellipse2D.Double(-10, -10, 20, 20);
			}
        	
        });
        
        vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Exercise, Paint>() {
			
			@Override
			public Paint transform(Exercise exo) {
				if (vv.getPickedVertexState().isPicked(exo))
					return Color.BLUE;
				if (exo.isSuccessfullyPassed()) 
					return Color.ORANGE;
				if (exo.getLesson().isAccessible(exo))
					return Color.GREEN;
				return Color.RED;
			}
		});
        
        
        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller()); 
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
  
        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(graphMouse);
        
        final GraphZoomScrollPane graphViewPane = new GraphZoomScrollPane(vv);
        mainPane.setLeftComponent(graphViewPane);
                
        final JPanel exoWorldViews = new JPanel(new MigLayout("insets 0 0 0 0"));
        
        selectedExo = Game.getInstance().getCurrentLesson().getCurrentExercise();
        
        final JTextArea exoNameArea = new JTextArea(selectedExo.name);
        exoNameArea.setEditable(false);
        
    	final JEditorPane missionArea = new JEditorPane("text/html", "");
		missionArea.setEditable(false);
		missionArea.setEditorKit(new JlmHtmlEditorKit());
		StyleSheet styles = ((HTMLDocument) missionArea.getDocument()).getStyleSheet();
		styles.importStyleSheet(getClass().getResource("/lessons/screen.css"));			
        
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
				Exercise exo = (Exercise) e.getItem();
				if (vv.getPickedVertexState().isPicked(exo)) {
					if (selectedExo != null && vv.getPickedVertexState().isPicked(selectedExo)) {
						vv.getPickedVertexState().pick(selectedExo,false); // fight multiple selection
					}
					selectedExo = exo;
					missionArea.setText(exo.getMission());
					missionArea.setCaretPosition(0);
					
					exoWorldViews.removeAll();
					for (int i=0;i<exo.getAnswerWorld().size();i++) {
						for (WorldView wv: exo.getWorld(i).getView()) {
							wv.setMinimumSize(new Dimension(50,50));
							wv.setPreferredSize(new Dimension(200,200));
							wv.setToolTipText("World"+wv.getTabName());
							exoWorldViews.add(wv,"grow,push");
						}
						for (WorldView wv: exo.getAnswerOfWorld(i).getView()) {
							wv.setMinimumSize(new Dimension(50,50));
							wv.setPreferredSize(new Dimension(200,200));
							wv.setToolTipText("Objective"+wv.getTabName());
							exoWorldViews.add(wv,"grow,push,span");
						}
					}
					
					exoNameArea.setText(exo.name);
				} else {
					selectedExo = null;
				}
			}
		});
        vv.getPickedVertexState().pick(selectedExo, false);
        vv.getPickedVertexState().pick(selectedExo, true);

        
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
				if (selectedExo != null) { 
					Game.getInstance().setCurrentExercise(selectedExo);
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
    
	@Override
	public void currentLessonHasChanged() {
		Lesson lesson = Game.getInstance().getCurrentLesson();
		setTitle("Your progress on lesson "+ lesson.getName());

		graph = lesson.getExercisesGraph();
		initComponents();
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