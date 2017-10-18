package sort.pancake;


import plm.core.model.lesson.ExerciseTemplated;

import plm.core.utils.FileUtils;
import plm.universe.pancake.PancakeEntity;
import plm.universe.pancake.PancakeWorld;

public class BurnedPancake extends ExerciseTemplated {

	public BurnedPancake(FileUtils fileUtils) {
		super("BurnedPancake");
		
		PancakeWorld plate[] = new PancakeWorld[4];
		plate[0]= new PancakeWorld(fileUtils, "5 pancakes",5,true);
		plate[1]= new PancakeWorld(fileUtils, "10 pancakes",10,true);
		plate[2]= new PancakeWorld(fileUtils, "15 pancakes",15,true);
		plate[3]= new PancakeWorld(fileUtils, "30 pancakes",30,true);
		for ( int i = 0 ; i<4;i++)
			new PancakeEntity("Pancake Seller",plate[i]);

		setup(plate);
	}

}
