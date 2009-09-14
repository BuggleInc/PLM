package universe.bat;

import jlm.lesson.ExerciseTemplated;
import jlm.lesson.Lesson;
import jlm.universe.Entity;
import jlm.universe.World;

public class BatExercise extends ExerciseTemplated {
	public static final boolean INVISIBLE = false;
	public static final boolean VISIBLE = true;
	protected String methName;
	
	public BatExercise(Lesson lesson) {
		super(lesson);
	}

	@Override
	protected void setup(World[] ws) {		
		worldDuplicate(ws);
		computeAnswer();
	}
	protected void setup(World[] myWorlds, String methName, BatEntity e) {
		this.methName=methName;
		for (World w : myWorlds) {
			BatWorld bw = (BatWorld) w;
			String name=methName+"(";
			for (Object o:w.getParameters()) {
				name+=o.toString()+",";
			}
			name=name.substring(0,name.length()-1);
			name+=")";
			bw.setName(name);
			Entity cpy = e.copy();
			w.addEntity(cpy);
		}
		super.setup(myWorlds);
	}
	//@Override
	//public boolean check() {
	//	return false;
	//}
	
}
