#include "RemoteBuggle.h"


char getIndication() {
	if (isOverMessage()) {
		return readMessage()[0];
	} else {
		return ' ';
	}
}


int main(){
	#line 1 "BDR"
	/* BEGIN SOLUTION */
	while (1) {
		char c = getIndication();
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
			return 0;
	}
	/* END SOLUTION */
	return 0;
}
