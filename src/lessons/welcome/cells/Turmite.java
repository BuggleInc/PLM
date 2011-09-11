package lessons.welcome.cells;

import java.awt.Color;

import jlm.core.model.lesson.ExerciseTemplated;
import jlm.core.model.lesson.Lesson;
import jlm.universe.Direction;
import jlm.universe.bugglequest.Buggle;
import jlm.universe.bugglequest.BuggleWorld;

public class Turmite extends ExerciseTemplated {
	final static int NOTURN = 1;
	final static int LEFT   = 2;
	final static int BACK   = 4;
	final static int RIGHT  = 8;
	
	BuggleWorld createWorld(int nbSteps, int[][][]rule, int width, int height, int buggleX, int buggleY) {
		BuggleWorld bw = new BuggleWorld(rule+" ("+nbSteps+" steps)", width, height);
				
		bw.setParameter(new Object[] {
			nbSteps,
			rule
		});
		
		new Buggle(bw,"ant",buggleX,buggleY,Direction.NORTH,Color.red,Color.red);
		
		bw.setDelay(1);
		return bw;
	}
	
	public Turmite(Lesson lesson) {
		super(lesson);
		tabName = "Turmite";

		BuggleWorld[] myWorlds = new BuggleWorld[] {
				 createWorld(8342, new int[][][] {{{1, 2, 0}, {1, 2, 1}}, {{0, 1, 0}, {0, 1, 1}}}, 78, 72, 70, 33),
				 createWorld(10100, new int[][][]  {{{1, 1, 1}, {1, 8, 0}}, {{1, 2, 1}, {0, 1, 0}}}, 68, 72, 32, 33),
				 createWorld(4800, new int[][][]  {{{0, 2, 1}, {0, 8, 0}}, {{1, 8, 1}, {1, 1, 0}}}, 65, 65, 5, 55), //6
		};

		setup(myWorlds);

	}
}

/*
EdPeggJrTurmiteLibrary = [
# source: http://demonstrations.wolfram.com/Turmites/
# Translated into his later notation: 1=noturn, 2=right, 4=u-turn, 8=left
# (old notation was: 0=noturn,1=right,-1=left)
# (all these are 2-color patterns)
"{{{1, 2, 0}, {0, 8, 0}}}",  # 1: Langton's ant
"{{{1, 2, 0}, {0, 1, 0}}}",  # 2: binary counter
"{{{0, 8, 1}, {1, 2, 1}}, {{1, 1, 0}, {1, 1, 1}}}", # 3: (filled triangle)
"{{{0, 1, 1}, {0, 8, 1}}, {{1, 2, 0}, {0, 1, 1}}}", # 4: spiral in a box
"{{{0, 2, 1}, {0, 8, 0}}, {{1, 8, 1}, {0, 2, 0}}}", # 5: stripe-filled spiral
"{{{0, 2, 1}, {0, 8, 0}}, {{1, 8, 1}, {1, 1, 0}}}", # 6: stepped pyramid
"{{{0, 2, 1}, {0, 1, 1}}, {{1, 2, 1}, {1, 8, 0}}}", # 7: contoured island
"{{{0, 2, 1}, {0, 2, 1}}, {{1, 1, 0}, {0, 2, 1}}}", # 8: woven placemat
"{{{0, 2, 1}, {1, 2, 1}}, {{1, 8, 1}, {1, 8, 0}}}", # 9: snowflake-ish
"{{{1, 8, 0}, {0, 1, 1}}, {{0, 8, 0}, {0, 8, 1}}}", # 10: slow city builder
"{{{1, 8, 0}, {1, 2, 1}}, {{0, 2, 0}, {0, 8, 1}}}", # 11: framed computer art
"{{{1, 8, 0}, {1, 2, 1}}, {{0, 2, 1}, {1, 8, 0}}}", # 12: balloon bursting (makes a spreading highway)
"{{{1, 8, 1}, {0, 8, 0}}, {{1, 1, 0}, {0, 1, 0}}}", # 13: makes a horizontal highway
"{{{1, 8, 1}, {0, 8, 0}}, {{1, 2, 1}, {1, 2, 0}}}", # 14: makes a 45 degree highway
"{{{1, 8, 1}, {0, 8, 1}}, {{1, 2, 1}, {0, 8, 0}}}", # 15: makes a 45 degree highway
"{{{1, 8, 1}, {0, 1, 0}}, {{1, 1, 0}, {1, 2, 0}}}", # 16: spiral in a filled box
"{{{1, 8, 1}, {0, 2, 0}}, {{0, 8, 0}, {0, 8, 0}}}", # 17: glaciers
"{{{1, 8, 1}, {1, 8, 1}}, {{1, 2, 1}, {0, 1, 0}}}", # 18: golden rectangle!
"{{{1, 8, 1}, {1, 2, 0}}, {{0, 8, 0}, {0, 8, 0}}}", # 19: fizzy spill
"{{{1, 8, 1}, {1, 2, 1}}, {{1, 1, 0}, {0, 1, 1}}}", # 20: nested cabinets
"{{{1, 1, 1}, {0, 8, 1}}, {{1, 2, 0}, {1, 1, 1}}}", # 21: (cross)
"{{{1, 1, 1}, {0, 1, 0}}, {{0, 2, 0}, {1, 8, 0}}}", # 22: saw-tipped growth
"{{{1, 1, 1}, {0, 1, 1}}, {{1, 2, 1}, {0, 1, 0}}}", # 23: curves in blocks growth
"{{{1, 1, 1}, {0, 2, 0}}, {{0, 8, 0}, {0, 8, 0}}}", # 24: textured growth
"{{{1, 1, 1}, {0, 2, 1}}, {{1, 8, 0}, {1, 2, 0}}}", # 25: (diamond growth)
"{{{1, 1, 1}, {1, 8, 0}}, {{1, 2, 1}, {0, 1, 0}}}", # 26: coiled rope
"{{{1, 2, 0}, {0, 8, 1}}, {{1, 8, 0}, {0, 1, 1}}}", # 27: (growth)
"{{{1, 2, 0}, {0, 8, 1}}, {{1, 8, 0}, {0, 2, 1}}}", # 28: (square spiral)
"{{{1, 2, 0}, {1, 2, 1}}, {{0, 1, 0}, {0, 1, 1}}}", # 29: loopy growth with holes
"{{{1, 2, 1}, {0, 8, 1}}, {{1, 1, 0}, {0, 1, 0}}}", # 30: Lanton's Ant drawn with squares
"{{{1, 2, 1}, {0, 2, 0}}, {{0, 8, 1}, {1, 8, 0}}}", # 31: growth with curves and blocks
"{{{1, 2, 1}, {0, 2, 0}}, {{0, 1, 0}, {1, 2, 1}}}", # 32: distracted spiral builder
"{{{1, 2, 1}, {0, 2, 1}}, {{1, 1, 0}, {1, 1, 1}}}", # 33: cauliflower stalk (45 deg highway)
"{{{1, 2, 1}, {1, 8, 1}}, {{1, 2, 1}, {0, 2, 0}}}", # 34: worm trails (eventually turns cyclic!)
"{{{1, 2, 1}, {1, 1, 0}}, {{1, 1, 0}, {0, 1, 1}}}", # 35: eventually makes a two-way highway!
"{{{1, 2, 1}, {1, 2, 0}}, {{0, 1, 0}, {0, 1, 0}}}", # 36: almost symmetric mould bloom
"{{{1, 2, 1}, {1, 2, 0}}, {{0, 2, 0}, {1, 1, 1}}}", # 37: makes a 1 in 2 gradient highway
"{{{1, 2, 1}, {1, 2, 1}}, {{1, 8, 1}, {0, 2, 0}}}", # 38: immediately makes a 1 in 3 highway
"{{{0, 2, 1}, {1, 2, 1}}, {{0, 8, 2}, {0, 8, 0}}, {{1, 2, 2}, {1, 8, 0}}}", # 39: squares and diagonals growth
"{{{1, 8, 1}, {0, 1, 0}}, {{0, 2, 2}, {1, 8, 0}}, {{1, 2, 1}, {1, 1, 0}}}", # 40: streak at approx. an 8.1 in 1 gradient
"{{{1, 8, 1}, {0, 1, 2}}, {{0, 2, 2}, {1, 1, 1}}, {{1, 2, 1}, {1, 1, 0}}}", # 41: streak at approx. a 1.14 in 1 gradient
"{{{1, 8, 1}, {1, 8, 1}}, {{1, 1, 0}, {0, 1, 2}}, {{0, 8, 1}, {1, 1, 1}}}", # 42: maze-like growth
"{{{1, 8, 2}, {0, 2, 0}}, {{1, 8, 0}, {0, 2, 0}}, {{0, 8, 0}, {0, 8, 1}}}", # 43: growth by cornices 
"{{{1, 2, 0}, {0, 2, 2}}, {{0, 8, 0}, {0, 2, 0}}, {{0, 1, 1}, {1, 8, 0}}}", # 44: makes a 1 in 7 highway
"{{{1, 2, 1}, {0, 8, 0}}, {{1, 2, 2}, {0, 1, 0}}, {{1, 8, 0}, {0, 8, 0}}}", # 45: makes a 4 in 1 highway
# source: http://www.mathpuzzle.com/Turmite5.nb
# via: http://www.mathpuzzle.com/26Mar03.html
# "I wondered what would happen if a turmite could split as an action... say Left and Right.  
#  In addition, I added the rule that when two turmites met, they annihilated each other.
#  Some interesting patterns came out from my initial study. My main interest is finding 
#  turmites that will grow for a long time, then self-annihilate."
"{{{1, 8, 0}, {1, 2, 1}}, {{0, 10, 0}, {0, 8, 1}}}",  # 46: stops at 11 gens
"{{{1, 8, 0}, {1, 2, 1}}, {{1, 10, 0}, {1, 8, 1}}}",  # 47: stops at 12 gens
"{{{1, 15, 0}, {0, 2, 1}}, {{0, 10, 0}, {0, 8, 1}}}", # 48: snowflake-like growth
"{{{1, 2, 0}, {0, 15, 0}}}",                          # 49: blob growth
"{{{1, 3, 0}, {0, 3, 0}}}",                           # 50: (spiral) - like SDSR-Loop on a macro level
"{{{1, 3, 0}, {0, 1, 0}}}",                           # 51: (spiral)
"{{{1, 10, 0}, {0, 1, 0}}}",                          # 52: snowflake-like growth
"{{{1, 10, 1}, {0, 1, 1}}, {{0, 2, 0}, {0, 0, 0}}}",  # 53: interesting square growth
"{{{1, 10, 1}, {0, 5, 1}}, {{1, 2, 0}, {0, 8, 0}}}",  # 54: interesting square growth
"{{{1, 2, 0}, {0, 1, 1}}, {{1, 2, 0}, {0, 6, 0}}}",   # 55: stops at 14 gens
"{{{1, 2, 0}, {1, 1, 1}}, {{0, 2, 0}, {0, 11, 0}}}",  # 56: grows with many highways
"{{{1, 2, 1}, {0, 2, 1}}, {{1, 15, 0}, {1, 8, 0}}}",  # 57: divided square growth
"{{{1, 2, 0}, {2, 8, 2}, {1, 1, 1}}, {{1, 1, 2}, {0, 2, 1}, {2, 8, 1}}, {{0, 11, 0}, {1, 1, 1}, {0, 2, 2}}}", # 58: wedge-shaped semi-chaotic growth
"{{{1, 2, 0}, {2, 8, 2}, {2, 1, 1}}, {{1, 1, 2}, {1, 1, 1}, {1, 8, 1}}, {{2, 10, 0}, {2, 8, 1}, {2, 8, 2}}}"  # 59: moss-like growth with internal raindrops
]
# N.B. the images in the demonstration project http://demonstrations.wolfram.com/Turmites/
# are mirrored - e.g. Langton's ant turns left instead of right.
# (Also example #45 isn't easily accessed in the demonstration, you need to open both 'advanced' controls,
#  then type 45 in the top text edit box, then click in the bottom one.)
*/