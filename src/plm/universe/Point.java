package plm.universe;

/** Immutable 2D point / vector */
public class Point {
  double x;
  double y;
  public Point(double x, double y)
  {
    this.x = x;
    this.y = y;
  }
  public double x() { return x; }
  public double y() { return y; }

  public Point plus(Point p) { return new Point(x + p.x, y + p.y); }
  public Point minus(Point p) { return new Point(x - p.x, y - p.y); }
  public Point times(double l) { return new Point(x * l, y * l); }
  public Point dividedBy(double l) { return new Point(x / l, y / l); }
  public Point negate() { return this.times(-1); }

  public double length() { return Math.sqrt(x * x + y * y); }
  public Point normed() { return this.dividedBy(length()); }
  public double dot(Point p) { return x * p.x + y * p.y; }
  public double cross(Point p) { return x * p.y - y * p.x; }
}
