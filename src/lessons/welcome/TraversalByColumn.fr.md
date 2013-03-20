## Parcours colonne par colonne ##
L'objectif de cette série d'exercices est de faire parcourir le monde à la
buggle.  Elle doit de plus numéroter les différentes cases rencontrées pour
montrer son ordre de parcours.

La boucle principale de la méthode (que vous devez
écrire)  est de la forme :     tant que l'on n'est pas à la position finale
    aller à la prochaine position
    marquer le numéro de case au sol

À la différence des exercices vus jusque là, nous n'allons pas utiliser les
méthodes , et autres, mais
nous allons calculer les coordonnées de la prochaine position de la buggle,
et utiliser la méthode pour la buggle directement à cette position. Par exemple, téléporte la buggle sur la case où x=3 et y=5.

Le premier objectif est donc d'écrire une fonction booléenne indiquant si la
buggle a atteint la position finale ou non, càd si elle est arrivée en bas à
droite du monde.  Vous utiliserez pour cela les méthodes et qui retournent
respectivement la largeur et la hauteur du monde.  Votre test est de
comparer les coordonnées actuelles de votre buggle (que vous pouvez
retrouver avec les méthodes et ) aux
dimensions du monde. Attention, la première ligne et la première colonne sont numérotées 0 et non
1...

Ensuite, il faut écrire le code pour aller à la position suivante. Dans cet
exercice, il faut parcourir le monde colonne par colonne. Donc, si on est
tout en bas d'une colonne, il faut aller en haut de la colonne suivante et
sinon, il faut aller à la case du dessous.

À ce point, vous pouvez lancer votre programme pour vérifier que la buggle
parcours bien le monde dans l'ordre souhaité, et s'arrête bien quand il
faut. Pensez à utiliser le bouton *stop* pour arrêter l'exécution si
votre programme ne se termine pas correctement.

Il est temps d'écrire au sol les numéros de case. Pour cela, vous aurez
besoin d'un compteur initialisé à zéro au début de votre méthode , et incrémenté de un à chaque pas (par exemple avec ). Ensuite, il faut écrire la valeur de ce compteur au
sol à chaque pas, par exemple avec .

Il est sans doute nécessaire d'écrire la valeur de la première ou dernière
case en dehors de la boucle principale, selon que vous utilisez un ou un ...

À vous de jouer...

