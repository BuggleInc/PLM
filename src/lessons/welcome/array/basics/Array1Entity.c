#include "../../../../../target/classes/resources/langages/c/RemoteBuggle.h"

void makeLine(Color colors[]);
void mark(Color c);

/* BEGIN TEMPLATE */
void run() {
	/* BEGIN SOLUTION */
	Color* colors = (Color*)malloc(sizeof(Color)*getWorldHeight());
	int i;
	/* read the colors */
	for (i=0;i<getWorldHeight();i++) {
		colors[i]=getGroundColor();
                step();
        }

        /* duplicate the pattern */
        for (i = 1; i < getWorldWidth(); i++) {
          left();
          step();
          right();
          step();
          makeLine(colors);
        }
        free(colors);
}
void makeLine(Color colors[]) {
	int i;
	for (i=0;i<getWorldWidth();i++) {
		mark(colors[i]);
                step();
        }
}
void mark(Color c){
	setBrushColor(c);
	brushDown();
	brushUp();
	/* END SOLUTION */
}
/* END TEMPLATE */
