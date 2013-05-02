
# Monde des tortues #
Ce monde est directement inspiré des travaux du mathématicien Seymour Papert
dans les années 60. Inspiré par le psychologue suisse Jean Piaget, il a
inventé une méthode d'apprentissage de la programmation accessible aux
jeunes enfants nommée LOGO. Le monde est peuplé de tortues qui laissent une
trace là où elles marchent et à qui on peut donner des ordres simples.

Ce monde est une adaptation de LOGO pour la Java Learning Machine.


## Méthodes comprises par les tortues ##


<table border=1>
	<tr>
		<td > </td>
	</tr>
	<tr>
		<td > **Avancer** ` public void forward(double nbPas);` **Reculer** ` public void backward(double nbPas);` </td>
	</tr>
	<tr>
		<td > **Tourner à gauche** ` public void turnLeft(double angle);` (en
degré) **Tourner à droite** ` public void turnRight(double angle);` </td>
	</tr>
	<tr>
		<td > **Lever le stylo** ` public void penUp();` **Baisser le stylo** ` public void penDown();` **Obtenir la position du stylo** ` public boolean isPenDown();` (les tortues ont des stylos, pas des brosses comme les buggles) </td>
	</tr>
	<tr>
		<td > **Obtenir direction** ` public double getHeading();` **Changer direction** ` public void setHeading(double angle);` </td>
	</tr>
	<tr>
		<td > **Obtenir couleur** ` public Color getColor();` **Changer couleur** ` public void setColor(Color color);` </td>
	</tr>
	<tr>
		<td > **Obtenir position** ` public double getX();` ` public
double getY();` **Changer position** ` public void setX(double x);` ` public void setY(double y);` ` public void setPos(double x,
double y);` </td>
	</tr>
</table>

