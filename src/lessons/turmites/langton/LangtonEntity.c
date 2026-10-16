#include "../../../../target/classes/resources/langages/c/RemoteTurmite.h"

void tick();

void run(){
  int nbSteps = getParamInt(0);
  int i;
  for (i = 0; i < nbSteps; i++) {
    tick();
    stepDone();
  }
}

/* BEGIN TEMPLATE */
void tick()
{
  /* BEGIN SOLUTION */
  if (getGroundColor() == white) {
    right();

    setBrushColor(black);
    brushDown();
    brushUp();

    forward(1);
  } else {
    left();

    setBrushColor(white);
    brushDown();
    brushUp();

    forward(1);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */
