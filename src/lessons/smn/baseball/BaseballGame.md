
# Baseball #

This world implements the baseball game, in which several players want to go back to their bases. The bases make a circle and have their own colors. There are two players for one base, except one, which has only one player in its team.

You are asked to make the players go on the bases which colors correspond to theirs. The only allowed move is for one of the two players standing on one of the two bases which are directly next to the base with only one player to move and fill the hole, leaving a new one on the base it just left.

The color of a base is given by its index.

The hole has a color of -1.

The base which has only one player on the field has *getAmountOfBases()-1* as index

  
  


<pre> There are six methods provided :void move(int baseSrc, int playerSrc)</pre>
Move the player on position ` playerLocation` from the base ` baseSrc` to the hole.


<pre> int getPlayerColor(int baseIndex, int playerLocation)</pre>
Return the color of the player on position ` playerLocation` ( 0 or 1 ) from the base ` baseIndex` .
The color -1 is the hole.


<pre> int getHoleBase()</pre>
Return the index of the base where the hole is located


<pre> int getHolePositionInBase()</pre>
Return the player's position in the base where the hole is located


<pre> boolean isBaseSorted( int baseIndex)</pre>
Return TRUE if all players of the base ` baseSrc` are home


<pre> int getAmountOfBases()</pre>
Return the amount of bases in the field.

  
  
* * *
  
  

The prototypes of the methods you may use are the followings :


<pre>

private void bringPlayerHome(int baseSrc, int playerSrc, int baseDst, int playerDst) throws InvalidMoveException

Move the player in position ` playerSrc` of the base ` baseSrc` to the position ` playerDst` from the base ` baseDst` 

private int[] findNearestPlayer(int colorWanted, int firstBaseToSearch ) throws InvalidPositionException

Return the base's number and the position of the nearest player which has ` colorWanted` as color. It begins the search at the base ` firstBaseToSearch` 

private void bringHole(int baseDst, int playerDst , int playerToProtect) throws InvalidMoveException

Bring the hole to the position ` playerDst` from the base ` baseDst` while protecting the position ` playerToProtect` during the travel

</pre>
  
  
* * *
  
  

The algorithm of the solve method is the following:


<pre> BEGIN
colorWanted- getBaseColor(baseIndex)
IF getPlayerColor(baseIndex, 0) != colorWanted
THEN
IF getPlayerColor(baseIndex,1) == colorWanted
THEN
bringHole(baseIndex, 0, 1)
move(baseIndex, 1)
ELSE
wantedPlayerLocation- findNearestPlayer(colorWanted, baseIndex+1 )
bringHole( wantedPlayerLocation[0]-1 , 0,wantedPlayerLocation[1] )
bringPlayerHome(wantedPlayerLocation[0], wantedPlayerLocation[1], baseIndex,0)
END_IF
END_IF
IF getPlayerColor(baseIndex, 1) != colorWanted
THEN
wantedPlayerLocation- this.findNearestPlayer(colorWanted, baseIndex+1)
bringHole( wantedPlayerLocation[0]-1 , 1 , wantedPlayerLocation[1] )
bringPlayerHome( wantedPlayerLocation[0] , wantedPlayerLocation[1] , baseIndex , 1 )
END_IF
END</pre>
  
  
* * *
  
  

The algorithm of the bringHole method is the following:


<pre> BEGIN
holeBaseLocation[0]- getHoleBase()
holeBaseLocation[1]- getHolePositionInBase()
IF baseDst > holeLocation[0]
THEN
FOR i FROM holeLocation[0]+1 TO baseDst BY 1
DO
move(i, playerDst)
END_FOR
ELSE
IF baseDstholeLocation[0]
THEN
FOR i FROM holeLocation[0]-1 DOWNTO baseDst+1 BY -1
DO
move(i,1-playerToProtect)
END_FOR
move(baseDst,playerDst);
ELSE
IF baseDst == holeLocation[0] AND playerDst != holeLocation[1]
THEN
move(baseDst,playerDst);
END_IF
END_IF
END_IF
END</pre>
  
  
* * *
  
  

The algorithm of the findNearestPlayer method is the following :


<pre> BEGIN
nbBases- getAmountOfBases()
found- false;
i- firstBaseToSearch
WHILE inbBases AND !found
DO
FOR j FROM 0 TO 1 BY 1
DO
IF getPlayerColor(i, j)== colorWanted AND !found
THEN
location[0]- i;
location[1]- j;
found- true;
END_IF
END_FOR
i- i+1
END_WHILE
RETURN location
END</pre>
  
  
* * *
  
  

The algorithm of the bringPlayerHome method is the following :


<pre> PRECONDITION: the hole is on the base of index baseSrc-1
BEGIN
move( baseSrc,playerSrc)
FOR i FROM baseSrc-1 DOWNTO baseDst+1 BY -1
DO
move(i,1-playerDst)
move(i-1,playerDst)
move(i,playerDst)
END_FOR</pre>

