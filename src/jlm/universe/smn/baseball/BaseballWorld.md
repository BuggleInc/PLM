
# Baseball #

The color of a base is given by its index.

The hole has a color of -1.

The base which has only one player on the field has *getAmountOfBases()-1* as index

  
  

<pre> void move(int baseSrc, int playerSrc)</pre>
` Move the player on positionplayerLocation` ` from the basebaseSrc` 
<pre> to the hole.int getPlayerColor(int baseIndex, int playerLocation)</pre>
` Return the color of the player on positionplayerLocation` ` ( 0 or 1 ) from the basebaseIndex` 
<pre> .int getHoleBase()</pre>

<pre> Return the index of the base where the hole is locatedint getHolePositionInBase()</pre>

<pre> Return the player's position in the base where the hole is locatedboolean isBaseSorted( int baseIndex)</pre>
` Return TRUE if all players of the basebaseSrc` 
<pre> are homeint getAmountOfBases()</pre>
Return the amount of bases in the field.

