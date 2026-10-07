#include "../../../../target/classes/resources/langages/c/RemoteLander.h"

typedef struct {
  Point start;
  Point end;
} Segment;

Segment getLandingZone();

void run()
{
  Segment landingZone = getLandingZone();
  double targetStart  = landingZone.start.x;
  double targetEnd    = landingZone.end.x;

  while (isFlying()) {
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
    simulateStep();
  }
}

/* BEGIN TEMPLATE */
Segment getLandingZone()
{
  /* BEGIN SOLUTION */
  PointArray ground = getGround();
  Segment seg       = {{0, 0}, {0, 0}};
  for (int i = 0; i + 1 < ground.count; i++)
    if (ground.items[i].y == ground.items[i + 1].y) {
      seg.start = ground.items[i];
      seg.end   = ground.items[i + 1];
      break;
    }

  free(ground.items);
  return seg;
  /* END SOLUTION */
}
/* END TEMPLATE */
