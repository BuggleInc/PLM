
# Le monde des Buggles #
Ce monde a été inventé par Lyn Turbak, du Wellesley College. Il est peuplé
de Buggles, petites bêtes qui comprennent des ordres simples, et offre de
nombreuses possibilités d'interaction avec le monde: prendre ou poser des
objets, colorier le sol, se cogner à des murs, etc. 
## Méthodes comprises par les buggles ##


<table border=1>
	<tr>
		<td align="center"> **Bouger** (voir aussi la note sur les exceptions, plus bas) </td>
	</tr>
	<tr>
		<td > **Reculer** </td>
		<td > void turnLeft(); void turnRight() void turnBack() void forward() ou void forward(int) void backward() ou void backward(int) </td>
	</tr>
	<tr>
		<td > **Changer la position** </td>
		<td > int getX(); int getY() void setX(int) void setY(int) void setPos(int,int) </td>
	</tr>
	<tr>
		<td align="center"> **Informations sur la buggle** </td>
	</tr>
	<tr>
		<td > **Changer la couleur** </td>
		<td > Color getColor(); void setColor(Color) </td>
	</tr>
	<tr>
		<td > **Chercher un mur derriere** </td>
		<td > boolean isFacingWall(); boolean isBackingWall() </td>
	</tr>
	<tr>
		<td > **Changer la direction** Les directions valides sont : </td>
		<td > Direction getDirection(); void setDirection(Direction) Direction.NORTH, Direction.EAST, Direction.SOUTH et Direction.WEST </td>
	</tr>
	<tr>
		<td align="center"> **À propos de la brosse** </td>
	</tr>
	<tr>
		<td > **Obtenir la position de la brosse** </td>
		<td > void brushUp(); void brushDown(); boolean isBrushDown(); </td>
	</tr>
	<tr>
		<td > **Obtenir la couleur de la brosse** </td>
		<td > void setBrushColor(Color); Color getBrushColor(); </td>
	</tr>
	<tr>
		<td align="center"> **Interagir avec le monde** </td>
	</tr>
	<tr>
		<td > **Obtenir la couleur du sol** </td>
		<td > Color getGroundColor(); </td>
	</tr>
	<tr>
		<td > **Poser un baggle** (voir la note sur les exceptions) </td>
		<td > boolean isOverBaggle(); boolean isCarryingBaggle(); void pickUpBaggle() void dropBaggle() </td>
	</tr>
	<tr>
		<td > **Effacer le message** </td>
		<td > boolean isOverMessage(); void writeMessage(String); String readMessage() void clearMessage() </td>
	</tr>
</table>


## Note sur les exceptions ##
Les buggles normales lèvent une exception BuggleWallException si on cherche
à leur faire traverser un mur.
Elles lèvent une exception NoBaggleUnderBuggleException si vous cherchez à
prendre un baggle dans une case qui n'en contient pas, ou une exception
AlreadyHaveBaggleException si vous portez déjà un baggel.
Tenter de déposer un baggel sur une case qui en contient déjà lève une
exception AlreadyHaveBaggleException.

Les "SimpleBuggles" (ie, celles utilisées dans les premiers exercices)
affiche un message d'erreur sans que vous ayez à vous soucier de ce qu'est
une exception.

