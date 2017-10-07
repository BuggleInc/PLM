//RemoteBuggle


void run(){
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	while (1) {
		char c = getIndicationBdr();
		if (c == 'R') {
			right();
			forward(1);
		} else if (c == 'L') {
			left();
			forward(1);
		} else if (c == 'I') {
			back();
			forward(1);
		} else if (c == 'A')
			forward(1);
		else if (c == 'B')
			forward(2);
		else if (c == 'C')
			forward(3);
		else if (c == 'Z')
			backward(1);
		else if (c == 'Y')
			backward(2);
		else if (c == 'X')
			backward(3);
		else
			return ;
	}
	/* END SOLUTION */
	/* END TEMPLATE */
}
