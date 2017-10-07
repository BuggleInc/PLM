package sort.pancake;

import plm.core.model.lesson.ExerciseTemplated;

import plm.universe.pancake.PancakeEntity;
import plm.universe.pancake.PancakeWorld;

public class BasicPancake extends ExerciseTemplated {
	
	public BasicPancake() {
		super("BasicPancake", "BasicPancake");
	
		PancakeWorld plate[] = new PancakeWorld[4];
		plate[0]= new PancakeWorld("5 pancakes",5,false);
		plate[1]= new PancakeWorld("10 pancakes",10,false);
		plate[2]= new PancakeWorld("15 pancakes",15,false);
		plate[3]= new PancakeWorld("30 pancakes",30,false);
		for ( int i = 0 ; i<4;i++)
			new PancakeEntity("Pancake Seller",plate[i]);

		setup(plate);
	}

}
