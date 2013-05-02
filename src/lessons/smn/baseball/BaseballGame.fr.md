
# Baseball #

Ce monde implémente le jeu du baseball multicolore, dans lequel plusieurs
joueurs veulent retourner à leur base. Les bases sont dans une disposition
circulaire et ont leur propre couleur. Il y a deux joueurs pour toutes les
bases sauf une, qui ne dispose donc que d'un joueur dans son équipe. Il vous
est demandé de déplacer les joueurs jusqu'à la base qui a la même couleur
que la leur. Le seul mouvement autorisé est, pour l'un des deux joueurs
présents sur l'une des deux bases située à côté de la base ayant un seul
joueur de se déplacer et de remplir le trou, laissant un nouveau trou sur la
base qu'il vient de quitter.

La couleur d'un base est donnée par son index.
Le trou a -1 comme couleur.
La base qui a un seul joueur sur le terrain a *getAmountOfBases()-1* comme index

  
  


<pre> Six méthodes vous sont fournies :void move(int baseSrc, int playerSrc)</pre>
Déplace le joueur de la position ` playerLocation` de la base ` baseSrc` au trou.


<pre> int getPlayerColor(int baseIndex, int playerLocation)</pre>
Renvoie la couleur du joueur à la position ` playerLocation` ( 0
ou 1 ) de la base ` baseIndex` .  La couleur -1 correspond au trou.


<pre> int getHoleBase()</pre>
Renvoie l'index de la base où se trouve le trou


<pre> int getHolePositionInBase()</pre>
Renvoie la position de joueur vide dans la base où se situe le trou


<pre> boolean isBaseSorted( int baseIndex )</pre>
Renvoie VRAI si tous les joueurs sur la base ` baseSrc` sont sur
la bonne base.


<pre> int getAmountOfBases()</pre>
Renvoie le nombre de bases dans le terrain

  
  
* * *
  
  

Les prototypes des méthodes que vous pouvez utiliser sont les suivants :


<pre>

private void bringPlayerHome(int baseSrc, int playerSrc, int baseDst, int
playerDst) throws InvalidMoveException

Déplace le joueur de la position ` playerLocation` de la base ` baseSrc` à la position ` playerDst` de la base ` baseDst` .

private int[] findNearestPlayer(int colorWanted, int firstBaseToSearch )
throws InvalidPositionException

Renvoie l'index de la base et la position sur celle-ci du joueur le plus
proche ayant ` colorWanted` comme couleur. La recherche commence à
la base d'index ` firstBaseToSearch` 

private void bringHole(int baseDst, int playerDst , int playerToProtect)
throws InvalidMoveException

Amène le trou à la position ` playerDst` de la base ` baseDst` tout en protégeant la position ` playerToProtect` durant le trajet

</pre>
  
  
* * *
  
  

L'algorithme de la méthode solve est le suivant :


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
  
  

L'algorithme de la méthode bringHole est le suivant :


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
  
  

L'algorithme de la méthode findNearestPlayer est le suivant :


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
  
  

L'algorithme de la méthode bringPlayerHome est le suivant :


<pre> PRECONDITION: the hole is on the base of index baseSrc-1
BEGIN
move( baseSrc,playerSrc)
FOR i FROM baseSrc-1 DOWNTO baseDst+1 BY -1
DO
move(i,1-playerDst)
move(i-1,playerDst)
move(i,playerDst)
END_FOR</pre>

