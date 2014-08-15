//RemoteBuggle

void run(){
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	int i,side,step;
	for (i = 0; i<7;i++)
	for (side=0;side<4;side++){
		for (step=0;step<4;step++)
			forward(1);
		left();
		for (step=0;step<2;step++)
			forward(1);
		right();
		for (step=0;step<4;step++)
			forward(1);
		right();
		forward(1);
		forward(1);
		left();
		for (step=0;step<4;step++)
			forward(1);
		left();
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
