#include "../../../target/classes/resources/langages/c/RemoteTurtle.h"

/* Original creation of one of our students */

/* BEGIN TEMPLATE */
void run()
{
  /* BEGIN SOLUTION */
  int angle = 45;
  addSizeHint(155, 102, 155, 122);
  for (int i = 0; i < (360 / angle); i++) {
    right(45.0);
    forward(20.0);
    left(90.0);
    forward(20.0);
    right(45.0);
    forward(20.0);
    right(90.0);
    for (int j = 0; j < 2; j++) {
      forward(20.0);
      left(90.0);
      forward(20.0);
      left(90.0);
      forward(20.0);
    }
    right(90.0);
    forward(20.0);
    right(45.0);
    forward(20.0);
    left(90.0);
    forward(20.0);
    right(45.0);
    right(180.0);
    right(angle);
  }
  hide();
  /* END SOLUTION */
}
/* END TEMPLATE */
