
# Monde des crêpes #

Ce monde implémente le problème du crêpier psychorigide, qui souhaite trier
ses crêpes de la plus grande à la plus petite, et avec la face brûlée cachée
( donc vers le bas, afin que l'on ne puisse pas la voir ).

Votre mission si vous l'acceptez est de trier un tas de crêpe pour le rendre
heureux.
Puisque qu'il est évidemment interdit -- pour des questions d'hygiène -- de
poser une crêpe sur la table, la seule action autorisée est de retourner une
ou plusieures crêpes depuis le haut de la pile.
Une crêpe est définie par son rayon et par sa position dans la pile, en
partant du haut.
Ces attributs sont des entiers positifs.

  
  


<pre> Seulement quatre fonctions vous sont fournies :int getStackSize()</pre>
Renvoie la taille du tas de crêpes, en d'autres mots, le nombre de crêpes
qui le compose.


<pre> int getPancakeRadius(int pancakeNumber)</pre>
Renvoie le rayon de la crêpe ` pancakeNumber` .


<pre> boolean isPancakeUpsideDown(int pancakeNumber)</pre>
Renvoie vrai si le côté brûlé de la crêpe ` pancakeNumber` est
vers le haut, faux sinon.


<pre> void flip(int numberOfPancakes)</pre>

` Retourne lesnumberOfPancakes` premières crêpes de la pile, en
partant du sommet de celle-ci.

