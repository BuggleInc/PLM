package jlm.universe.bat;

import java.util.List;

import jlm.core.model.lesson.ExerciseTemplatingEntity;
import jlm.core.model.lesson.Lesson;
import jlm.universe.World;

public abstract class BatExercise extends ExerciseTemplatingEntity {
	public static final boolean INVISIBLE = false;
	public static final boolean VISIBLE = true;
	
	public BatExercise(Lesson lesson) {
		super(lesson);
		entityName = getClass().getCanonicalName()+".Entity";
	}

	protected void setup(World[] ws, String entName) {
		for (World w : ws) {
			BatWorld bw = (BatWorld) w;
			String name=entName+"(";
			
			for (Object o:w.getParameters()) {
				if (o instanceof String[]) {
					name+="{";
					String[]a = (String[]) o;
					for (String i:a) {
						name+=i+",";
					}
					name=name.substring(0,name.length()-1)+"},";					
				} else if (o.getClass().isArray()){
					name+="{";
					if (o.getClass().getComponentType().equals(Integer.TYPE)) {
						int[]a = (int[]) o;
						for (int i:a) {
							name+=i+",";
						}
					} else {
						throw new RuntimeException("Unhandled internal type");
					}
					name=name.substring(0,name.length()-1)+"},";
				} else {
					name+=o.toString()+",";
				}
			}
			name=name.substring(0,name.length()-1);
			name+=")";
			bw.setName(name);
			w.addEntity(new BatEntity());
		}
		super.setup(ws,entName,
				"import universe.bat.BatEntity; "+
		        "import universe.bat.BatWorld; "+
		        "import jlm.universe.World; "+
		        "public class "+entName+" extends BatEntity { ");
	}

	@Override
	public void runDemo(List<Thread> runnerVect){
		/* No demo in bat exercises */
	}
	//@Override
	//public boolean check() {
	//	return false;
	//}
	
}
