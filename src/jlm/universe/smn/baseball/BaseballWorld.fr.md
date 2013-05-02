
# Baseball #

La couleur d'un base est donnée par son index.
Le trou a -1 comme couleur.
La base qui a un seul joueur sur le terrain a *getAmountOfBases()-1* comme index

  
  

<pre> void move(int baseSrc, int playerSrc)</pre>
` Déplace le joueur de la positionplayerLocation` ` de la basebaseSrc` 
<pre> au trou.int getPlayerColor(int baseIndex, int playerLocation)</pre>
` Renvoie la couleur du joueur de la positionplayerLocation` ` ( 0
ou 1 ) de la basebaseIndex` 
<pre> .int getHoleBase()</pre>

<pre> Renvoie l'index de la base où se trouve le trouint getHolePositionInBase()</pre>

<pre> Renvoie la position de joueur vide dans la base où se situe le trouboolean isBaseSorted( int baseIndex )</pre>
` Renvoie VRAI si tous les joueurs sur la basebaseSrc` 
<pre> sont sur
la bonne base.int getAmountOfBases()</pre>
Renvoie le nombre de bases dans le terrain

