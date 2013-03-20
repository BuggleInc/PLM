## Instructions ##
Félicitations ! Vous venez d'écrire votre premier programme ! Vous avez
compris l'idée maintenant : programmer, c'est simplement donner des
instructions à l'ordinateur, qui les applique aveuglément. La plus grande
difficulté est d'expliquer quoi faire à quelqu'un d'aussi bête que
l'ordinateur...

Le programme le plus simple est formé d'une suite d'ordres simples donnés à
la
machine. C'est assez comparable à une recette de cuisine où l'on dit . Dans
les programmes, de tels instructions  sont appellées fonctions ou
méthodes, et vous devez les doter de parenthèses comme dans     nomDeLaMethode()

Java veut que les instructions soient séparées par des points-virgules (;).
L'exemple ci-dessus de recette s'écrirait donc à peu près ainsi:

    casserLesOeufs();
    ajouterDuSel();
    melangerLeTout();
    faireCuire();

Python veut que les instructions soient séparées soit par des
points-virgules
(;), soit par des retours à la ligne.
L'exemple ci-dessus de recette s'écrirait donc à peu près ainsi:

    casserLesOeufs()
    ajouterDuSel()
    melangerLeTout()
    faireCuire()

It could also be written in the following way, but it's generally considered
as a bad practice to group several instructions on the same line since it
greatly hinders the readability.

    casserLesOeufs(); ajouterDuSel(); melangerLeTout(); faireCuire();

Bien entendu, ces méthodes n'existent pas en Java, mais il serait possible
de
les définir par vous même (nous verrons plus tard comment définir vos
propres méthodes).

Bien entendu, ces méthodes n'existent pas en Python, mais il serait possible
de
les définir par vous même (nous verrons plus tard comment définir vos
propres méthodes).

Pour l'instant, nous allons utiliser les instructions de la buggle. Il
y a une méthode pour chaque bouton du contrôle interactif.  Pour faire la
même
chose que le bouton *forward* (faire avancer la buggle d'un pas), il
faut
écrire dans l'éditeur :     forward();
    forward()
De même, pour faire l'équivalent des boutons *backward* , *turn left* et *turn right* , il faut utiliser respectivement :     backward();
    turnLeft();
    turnRight();
    backward()
    turnLeft()
    turnRight()
Le bouton *mark* est un peu particulier, puisqu'il correspond à deux
méthodes : la première lève le crayon, tandis que la seconde le baisse.     brushDown();
    brushUp();
    brushDown()
    brushUp()

La buggle offre d'autres méthodes, présentées dans le menu "Help/about this
world". Elles seront introduites au fur et à mesure des besoins.

### Objectif de cet exercice ###

