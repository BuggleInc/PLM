
# BuggleWorld #
This world was invented by Lyn Turbak, at Wellesley College. It is full of
Buggles, little animals understanding simple orders, and offers numerous
possibilities of interaction with the world: taking or dropping objects,
paint the ground, hit walls, etc. 
## Methods understood by buggles ##


<table border=1>
	<tr>
		<td align="center"> **Moving** (See also the note on exceptions, below) </td>
	</tr>
	<tr>
		<td > **Moving back** </td>
		<td > void turnLeft(); void turnRight() void turnBack() void forward() or void forward(int) void backward() or void backward(int) </td>
	</tr>
	<tr>
		<td > **Set position** </td>
		<td > int getX(); int getY() void setX(int) void setY(int) void setPos(int,int) </td>
	</tr>
	<tr>
		<td align="center"> **Information on the buggle** </td>
	</tr>
	<tr>
		<td > **Set the color** </td>
		<td > Color getColor(); void setColor(Color) </td>
	</tr>
	<tr>
		<td > **Look for a wall backward** </td>
		<td > boolean isFacingWall(); boolean isBackingWall() </td>
	</tr>
	<tr>
		<td > **Set heading** valid directions are: </td>
		<td > Direction getDirection(); void setDirection(Direction) Direction.NORTH, Direction.EAST, Direction.SOUTH and Direction.WEST </td>
	</tr>
	<tr>
		<td align="center"> **About the brush** </td>
	</tr>
	<tr>
		<td > **Get brush position** </td>
		<td > void brushUp(); void brushDown(); boolean isBrushDown(); </td>
	</tr>
	<tr>
		<td > **Get the color of the brush** </td>
		<td > void setBrushColor(Color); Color getBrushColor(); </td>
	</tr>
	<tr>
		<td align="center"> **Interacting with the world** </td>
	</tr>
	<tr>
		<td > **Get the color of the ground** </td>
		<td > Color getGroundColor(); </td>
	</tr>
	<tr>
		<td > **Drop a baggle** (see the note on exceptions) </td>
		<td > boolean isOverBaggle(); boolean isCarryingBaggle(); void pickUpBaggle() void dropBaggle() </td>
	</tr>
	<tr>
		<td > **Erase the message** </td>
		<td > boolean isOverMessage(); void writeMessage(String); String readMessage() void clearMessage() </td>
	</tr>
</table>


## Note on exceptions ##
Regular buggles throw a BuggleWallException exception if you ask them to
traverse a wall.  They throw a NoBaggleUnderBuggleException exception if you
ask them to pickup a baggle from an empty cell, or a
AlreadyHaveBaggleException exception if they already carry a baggle.  Trying
to drop a baggle on a cell already containing one throws an
AlreadyHaveBaggleException exception.

SimpleBuggles (ie, the one used in first exercises) display an error message
on problem so that you don't need to know what an exception is.

