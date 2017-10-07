//RemoteBuggle

/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void move(int nbPas, int goForward) {

	int i;
	if (goForward) {
		for (i=0; i<nbPas; i++)
			forward(1);
	} else {
		for (i=0; i<nbPas; i++)
			backward(1);
	}
}
/* END SOLUTION */
/* END TEMPLATE */

void run(){
	if(getDirection() == NORTH){
		move(getY(),1);
	}else{
		move(getY(),0);
	}
}
