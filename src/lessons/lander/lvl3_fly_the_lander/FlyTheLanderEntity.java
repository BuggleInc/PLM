package lessons.lander.lvl3_fly_the_lander;

import java.util.List;

import plm.universe.lander.LanderEntity;
import plm.universe.lander.Point;

public class FlyTheLanderEntity extends LanderEntity {
	/* BEGIN TEMPLATE */
	/* BEGIN HIDDEN */
	double targetStart;
	double targetEnd;
	/* END HIDDEN */
	public void initialize() {
		/* BEGIN HIDDEN */
		List<Point> ground = getGround();
		Point lastPoint = ground.get(0);
		for (Point point: ground) {
			if (point!=lastPoint && lastPoint.y() == point.y()) {
				targetStart = lastPoint.x();
				targetEnd = point.x();
				return;
			}
			lastPoint = point;
		}
		/* END HIDDEN */
	}

	public void step() {
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
}
