#include "../../../../target/classes/resources/langages/c/RemoteBuggle.h"

char getIndicationBDR() {
	if (isOverMessage()) {
		char* msg = readMessage();
		char ret = msg[0];
		free(msg);
		return ret;
	} else {
		return ' ';
	}
}

void run(){
	/* BEGIN TEMPLATE */
	/* BEGIN SOLUTION */
	int continu=1;
	char read;
	while(continu){

		read = getIndicationBDR();
		switch (read) {
                  case 'R':
                    right();
                    step();
                    break;
                  case 'L':
                    left();
                    step();
                    break;
                  case 'I':
                    back();
                    step();
                    break;

                  case 'A':
                    step();
                    break;
                  case 'B':
                    forward(2);
                    break;
                  case 'C':
                    forward(3);
                    break;
                  case 'D':
                    forward(4);
                    break;
                  case 'E':
                    forward(5);
                    break;
                  case 'F':
                    forward(6);
                    break;

                  case 'Z':
                    stepBack();
                    break;
                  case 'Y':
                    backward(2);
                    break;
                  case 'X':
                    backward(3);
                    break;
                  case 'W':
                    backward(4);
                    break;
                  case 'V':
                    backward(5);
                    break;
                  case 'U':
                    backward(6);
                    break;
                  default:
                    continu = 0;
                }
        }
        /* END SOLUTION */
        /* END TEMPLATE */
}
