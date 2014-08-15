//RemoteBuggle

/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void move(int nbPas, int f) {

	int i;
	if (f) {
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
