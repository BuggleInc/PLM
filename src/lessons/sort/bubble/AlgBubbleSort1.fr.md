
# Le tri à bulle et variantes #

Vous voici dans le monde des tris. Il vous permet d'expérimenter avec les
différents algorithmes de tri existant.  Consultez l'aide du monde
("Help->About this world") pour une aide sur les primitives utilisables par
vos algorithmes.

Dans ce premier exercice, nous allons voir les plus simples d'entre
eux. L'onglet "Source code" est composé de plusieurs sous-onglets,
correspondant aux différents algorithmes à écrire. Pour réussir l'exercice,
il faut que votre solution réalise le même nombre de lectures et écritures
que la solution codée dans l'objectif.  Attention donc à suivre le
pseudo-code de chaque algorithme à la lettre!


## BubbleSort ##

Le tri à bulles ou tri par propagation consiste à faire remonter
progressivement les plus petits éléments d'une liste, comme les bulles d'air
remontent à la surface d'un liquide. L'algorithme parcourt la liste, et
compare les couples d'éléments successifs. Lorsque deux éléments successifs
ne sont pas dans l'ordre croissant, ils sont échangés. Après chaque parcours
complet de la liste, l'algorithme recommence l'opération. Lorsqu'aucun
échange n'a lieu pendant un parcours, cela signifie que la liste est triée :
l'algorithme peut s'arrêter.  La simplicité de cet algorithme justifie son
étude, mais ses piètres performances (O(n2) en moyenne) font qu'il n'est
quasiment jamais utilisé en pratique.

  
  

Le pseudo-code de l'algorithme du tri à bulles est donc le suivant :


<pre> faire:
Pour tout i dans [0,lgr-2]
Si les cases i et i+1 doivent être inverser, le faire
tant qu'on a inverser des choses lors du dernier parcours</pre>

