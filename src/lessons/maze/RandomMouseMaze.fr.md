
## La souris folle ##

La journée de votre buggle commence mal. Il n'a pas eu de chance. Il est
tombé dans un piège.  Aidez le à sortir de ce labyrinthe.

Nous allons profiter du fait que le labyrinthe ne soit pas trop grand pour
écrire l'algorithme le plus bête possible.  Cet algorithme repose sur le
hasard et est très inefficace.

Tant que notre buggle n'a pas trouvé la sortie, il doit progresser de la
façon suivante : Si il se trouve à une jonction, il doit prendre une
décision au hasard (avancer si il le peut, ou bien tourner à droite ou bien
tourner à gauche).  Si ce n'est pas une jonction, il doit avancer si il le
peut, sinon il doit tourner de manière aléatoire.


### Objectif de cet exercice ###

Le corps de la méthode ` L'objectif de cet exercice est d'écrire un algorithme
permettant à votre buggle de sortir du labyrinthe. Pensez à faire prendre le
baggle à votre buggle avant la fin de votre programme.void run()` vous est fourni. Il vous est
demandé d'implémenter les méthodes qui vont aider votre buggle.

Ecrivez la méthode ` void turnRandomly()` que votre buggle utilise
pour tourner au hasard à droite ou à gauche. Vous pouvez vous aidez de la
méthode ` random2()` qui retourne 0 ou 1 de manière aléatoire.

Ecrivez la méthode ` void takeRandomDirection()` pour que votre
buggle prenne une décision (avancer, tourner à droite, tourner à gauche) au
hasard.  Vous pouvez utiliser la méthode ` random3()` qui retourne
de manière aléatoire la valeur 0, 1 ou 2.

Ecrivez la méthode ` boolean atAJunction()` qui indique à votre
buggle si il se trouve à une jonction (autrement dit, si il a la possibilité
de tourner à droite ou à gauche).

À vous de jouer.

