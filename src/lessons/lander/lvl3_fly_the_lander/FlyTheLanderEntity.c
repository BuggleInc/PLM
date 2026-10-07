#include "../../../../target/classes/resources/langages/c/RemoteLander.h"

/* BEGIN SOLUTION */
double targetStart;
double targetEnd;
/* END SOLUTION */

void initialize();
void step();

void run()
{
  initialize();
  while (isFlying()) {
    step();
    simulateStep();
  }
}

/* BEGIN TEMPLATE */
void initialize()
{
  /* BEGIN SOLUTION */
  PointArray ground = getGround();
  Point lastPoint   = ground.items[0];
  for (int i = 0; i < ground.count; i++) {
    Point point = ground.items[i];
    if ((point.x != lastPoint.x || point.y != lastPoint.y) && lastPoint.y == point.y) {
      targetStart = lastPoint.x;
      targetEnd   = point.x;
      break;
    }
    lastPoint = point;
  }
  free(ground.items);
  /* END SOLUTION */
}

void step()
{
  /* BEGIN SOLUTION */
  if (getX() < targetStart) {
    setDesiredAngle(-30);
  } else if (getX() > targetEnd) {
    setDesiredAngle(30);
  } else {
    if (getSpeedX() > 5) {
      setDesiredAngle(25);
    } else if (getSpeedX() < -5) {
      setDesiredAngle(-25);
    } else {
      setDesiredAngle(0);
    }
  }
  setDesiredThrust(getSpeedY() < -9 ? 4 : 3);
  /* END SOLUTION */
}
/* END TEMPLATE */
