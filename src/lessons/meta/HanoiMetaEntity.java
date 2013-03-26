package lessons.meta;

import java.lang.reflect.Constructor;

import jlm.universe.Entity;
import jlm.universe.World;
import jlm.universe.hanoi.HanoiEntity;
import jlm.universe.hanoi.HanoiInvalidMove;
import jlm.universe.hanoi.HanoiWorld;

public class HanoiMetaEntity extends HanoiEntity {
	/* If i'm the answer world, cheat and return use the implementation of my ancestor directly */
	boolean isAnswer=false;
	/* if i'm not the answer, ask our servant guy written by the student */
	Entity servant;
	/* We report errors to the exercise directly */
	MetaExercise exo;
	
	public HanoiMetaEntity(String name,World world,MetaExercise exo) {
		super(name,world);
		this.exo = exo;
	}
	@Override
	public Entity copy() {
		HanoiMetaEntity other = new HanoiMetaEntity(name,null,exo);
		other.isAnswer = isAnswer;
		other.servant = servant;
		return other;
	}
	
	/* Set tested code in position */
	protected void setServant(Class<Object> servantClass) {
		try {
			Constructor<Object> c = servantClass.getConstructor(String.class,World.class);
			servant = (Entity) c.newInstance(name,world);
		} catch (Exception e) {
			exo.error("Cannot instantiate your Entity implementation",e);
		}
	}
	
	/* Intercepting all methods of interest */
	public void move(int src, int dst) throws HanoiInvalidMove {
		if (isAnswer) {
			((HanoiWorld) world).move(src,dst);
			stepUI();
		} else {
			((HanoiEntity) servant).move(src,dst);
		}
	}
}
