
# TurtleWorld #
This world is directly inspired from the work of the mathematician Seymour
Papert in the 60's. Inspirated from the swiss psycholog Jean Piaget, he came
up with a learning method called LOGO to teach programming to young
childs. The world is full of turtles which leave a painting where they go
and which respond to simple orders.

This world is an adaptation of LOGO for the Java Learning Machine.


## Methods understood by turtles ##


<table border=1>
	<tr>
		<td > </td>
	</tr>
	<tr>
		<td > **Moving forward** ` public void forward(double stepAmount);` **Moving backward** ` public void backward(double stepAmount);` </td>
	</tr>
	<tr>
		<td > **Turn left** ` public void turnLeft(double angle);` (in
degree) **Turn right** ` public void turnRight(double angle);` </td>
	</tr>
	<tr>
		<td > **Pen up** ` public void penUp();` **Pen down** ` public void penDown();` **Get pen position** ` public boolean isPenDown();` (turtles have pens, not brushes as buggles) </td>
	</tr>
	<tr>
		<td > **Get heading** ` public double getHeading();` **Change heading** ` public void setHeading(double angle);` </td>
	</tr>
	<tr>
		<td > **Get color** ` public Color getColor();` **Set color** ` public void setColor(Color color);` </td>
	</tr>
	<tr>
		<td > **Get position** ` public double getX();` ` public double
getY();` **Set position** ` public void setX(double x);` ` public
void setY(double y);` ` public void setPos(double x, double
y);` </td>
	</tr>
</table>

