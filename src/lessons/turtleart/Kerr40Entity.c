#include "../../../target/classes/resources/langages/c/RemoteTurtle.h"

/* Item 40 of billkerr2.blogspot.fr/2009/08/40-maths-shapes-challenges.html */

/* BEGIN TEMPLATE */
void run()
{
  /* BEGIN SOLUTION */
  int s = 30;
  addSizeHint(150, 140, 150 + s, 140);
  for (int i = 0; i < 8; i++) {
    forward(s);
    left(90);
    forward(s);
    right(45);
    forward(s);
    left(90);
    forward(s);
    right(90);
    forward(s);
    right(90);
    forward(s);
    left(45);
    forward(s);
    right(90);
    forward(s);
    left(45);
    forward(s);
    right(90);
    forward(s);
    left(45);
    forward(s);
    right(90);
    forward(s);
    right(90);
    forward(s);
    left(90);
    forward(s);
    right(45);
    forward(s);
    left(90);
    forward(s);
  }
  /* END SOLUTION */
}
/* END TEMPLATE */
