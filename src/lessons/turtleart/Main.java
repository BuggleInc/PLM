package lessons.turtleart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;

import plm.core.model.Game;
import plm.core.model.lesson.Exercise;
import plm.core.model.lesson.ExerciseTemplated;
import plm.core.model.lesson.Lecture;
import plm.core.model.lesson.Lesson;
import plm.core.ui.ResourcesCache;
import plm.universe.BrokenWorldFileException;
import plm.universe.World;
import plm.universe.turtles.Turtle;
import plm.universe.turtles.TurtleWorld;
import plm.universe.turtles.TurtleWorldView;

public class Main extends Lesson {
	
	@Override
	protected void loadExercises() throws IOException, BrokenWorldFileException {
		addExercise(new TurtleGraphicalExercise(this,"Square",       300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"SmallSquare",  300,300, 50,150));
		addExercise(new TurtleGraphicalExercise(this,"Stairs",       300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"TriangleFlat", 300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"Triangle",     300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"House",        300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"HouseThree",   300,300, 50,150));
		addExercise(new TurtleGraphicalExercise(this,"HouseMany",    300,300, 50,250));
		addExercise(new TurtleGraphicalExercise(this,"Polygon6",     300,300, 81,190));
		addExercise(new TurtleGraphicalExercise(this,"Polygon7",     300,300, 65,190));
		addExercise(new TurtleGraphicalExercise(this,"Polygon20",    300,300, 55,165));
		addExercise(new TurtleGraphicalExercise(this,"Polygon360",   300,300, 35,149));
		addExercise(new TurtleGraphicalExercise(this,"CircleTwo",    300,300, 35,149));
		addExercise(new TurtleGraphicalExercise(this,"CircleYing",   300,300, 35,149));
		addExercise(new TurtleGraphicalExercise(this,"CircleSquare", 300,300, 50,200));
		addExercise(new TurtleGraphicalExercise(this,"CircleTen",    300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(this,"DiskFourth",   300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(this,"DiskFour",     300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(this,"DiskTwo",      300,300, 150,150));
		addExercise(new TurtleGraphicalExercise(this,"Star",         300,300, 150,200));
		addExercise(new TurtleGraphicalExercise(this,"Flower",       300,300, 90, 175));
		addExercise(new TurtleGraphicalExercise(this,"Flower2",      300,300, 150,150));
		
		setCurrentExercise(currentExercise); // recompute the missions
	}

	@Override
	public void setCurrentExercise(Lecture exo) {
		super.setCurrentExercise(exo);
		try {
			Game.waitInitThreads();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Lecture l : exercises())
			l.loadHTMLMission();		
	}
}

class TurtleGraphicalExercise extends ExerciseTemplated {
	
	public TurtleGraphicalExercise(Lesson lesson,String name,
			int worldWidth,int worldHeight,int tx, int ty) {
		
		super(lesson, lesson.getClass().getName()+"."+name);
		
		setName(name);
		tabName = "Source";
		World myWorld = new TurtleWorld(name, worldWidth, worldHeight);
		Turtle t = new Turtle(myWorld, "Hawksbill", tx, ty);
		t.setHeading(-90);
		setup(myWorld);
	}
	
	/* all exercises are packed into that file because the only difference between them is only the world size and turtle coordinates */
	@Override
	public String nameOfCorrectionEntity() { 
		return getLesson().getClass().getCanonicalName().replace(".Main","") + "." + getName() + "Entity";
	}

	@Override
	public void loadHTMLMission() {
		StringBuffer res = new StringBuffer();
		int exoCount = 0;
		for (Lecture l : getLesson().exercises()) {
			int iconSize=100;
			Exercise exo = (Exercise) l;
			boolean isSelected = exo.equals(getLesson().getCurrentExercise());
			boolean isPassed = Game.getInstance().studentWork.getPassed(exo, Game.getProgrammingLanguage());
			
			String path = "TurtleGraphics/"+exo.getId()+(isSelected?"-selected":"")+(isPassed?"-passed":"")+".png";
			
			// Recompute the icon if not cached
			ImageIcon icon = ResourcesCache.getIcon(path,true);
			if (icon == null) {
				BufferedImage bImg = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
				Graphics2D cg = bImg.createGraphics();
				cg.setColor(new Color(1, 1, 1, 0));
				cg.fillRect(0, 0, bImg.getHeight(), bImg.getWidth());		

				TurtleWorld tw;
				do {
					try {
						Game.waitInitThreads();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					tw= (TurtleWorld) exo.getWorlds(WorldKind.ANSWER).get(0);
				} while (! tw.isAnswerWorld());
				
				((TurtleWorldView) tw.getView()).doPaint(cg,iconSize,iconSize,false);
				
				// Add a box around it
				cg.setTransform(new AffineTransform()); // draw in 100x100, not world's dimensions
				cg.setColor(isSelected?Color.RED:Color.lightGray);
				cg.drawRect(1, 1, iconSize-2,iconSize-2);
				
				// Marker if it's passed
				if (isPassed) {
					Image star = ResourcesCache.getIcon("resources/star.png").getImage(); 
					cg.drawImage(star, 3, iconSize-star.getHeight(null)-2, null);
				}
				
				// Cache it
				ResourcesCache.setIcon(path,new ImageIcon(bImg));
			}
			    
			String lessonPart = "lessons."+getLesson().getId().replace("/",".").replace(".Main","");
			String exoPart = exo.getLocalId(); 
			res.append("<a href=\"plm://"+lessonPart+"/"+exoPart+"\">");
			res.append("<img src=\"");
			res.append(path);
			res.append("\"></a> ");
			exoCount++;
			if (exoCount % 5 == 0)
				res.append("<br> ");
		}
		setMission(res.toString());
	}
}
