#include "RemoteBuggle.h"

char getIndication() {
	if (isOverMessage()) {
		return readMessage()[0];
	} else {
		return ' ';
	}
}

int main(){
	#line 1 "BDR2"
	/* BEGIN SOLUTION */
	int continu=1;
	char read;
	while(continu){

		read = getIndication();
		switch (read) {
			case 'R': right(); forward(1); break;
			case 'L': left();  forward(1); break;
			case 'I': back();  forward(1); break;

			case 'A': forward(1); break;
			case 'B': forward(2); break;
			case 'C': forward(3); break;
			case 'D': forward(4); break;
			case 'E': forward(5); break;
			case 'F': forward(6); break;

			case 'Z': backward(1); break;
			case 'Y': backward(2); break;
			case 'X': backward(3); break;
			case 'W': backward(4); break;
			case 'V': backward(5); break;
			case 'U': backward(6); break;
			default : continu=0;
		}

	}
	/* END SOLUTION */
	return 0;
}
