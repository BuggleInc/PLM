package lessons.welcome.bdr;

import java.util.Stack;

import plm.universe.bugglequest.SimpleBuggle;

class BDR2Entity extends SimpleBuggle {
	override def run() { 
		/* BEGIN SOLUTION */
		var moreMusic = true;

		while (moreMusic) {
			readMessage() match {
			  case "R" => right(); stepForward();
			  case "L" => left();  stepForward();
			  case "I" => back();  stepForward();

			  case "A" => forward(1);
			  case "B" => forward(2);
			  case "C" => forward(3);
			  case "D" => forward(4);
			  case "E" => forward(5);
			  case "F" => forward(6);

			  case "Z" => backward(1);
			  case "Y" => backward(2);
			  case "X" => backward(3);
			  case "W" => backward(4);
			  case "V" => backward(5);
			  case "U" => backward(6);
			  
			  case _ =>  moreMusic = false
			}
		}
		/* END SOLUTION */
	}
}
