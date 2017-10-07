//RemoteBuggle

/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void goAndGet() {
	int i = 0;
	while (!isOverBaggle()) {
		i++;
		forward(1);
	}
	pickupBaggle();
	while (i>0) {
		backward(1);
		i--;
	}
	dropBaggle();
}
/* END SOLUTION */
/* END TEMPLATE */


void run(){
	int i=0;
	for (i=0; i<7; i++) {
		goAndGet();
		right();
		forward(1);
		left();
	}
}
