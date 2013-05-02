
## Instructions ##
Félicitations ! Vous venez d'écrire votre premier programme ! Vous avez
compris l'idée maintenant : programmer, c'est simplement donner des
instructions à l'ordinateur, qui les applique aveuglément. La plus grande
difficulté est d'expliquer quoi faire à quelqu'un d'aussi bête que
l'ordinateur...

Le programme le plus simple est formé d'une suite d'ordres simples donnés à
la
machine. C'est assez comparable à une recette de cuisine où l'on dit *cassez les oeufs puis ajoutez du sel puis mélangez le tout puis faites cuire* . Dans
les programmes, de tels instructions  sont appellées fonctions ou
méthodes, et vous devez les doter de parenthèses comme dans 
<pre> nomDeLaMethode()</pre>

<java>

Java veut que les instructions soient séparées par des points-virgules (;).
L'exemple ci-dessus de recette s'écrirait donc à peu près ainsi:

</java> 
<java> 
<pre> casserLesOeufs();
ajouterDuSel();
melangerLeTout();
faireCuire();</pre>
</java> 
<python>

Python veut que les instructions soient séparées soit par des
points-virgules
(;), soit par des retours à la ligne.
L'exemple ci-dessus de recette s'écrirait donc à peu près ainsi:

</python> 
<python> 
<pre> casserLesOeufs()
ajouterDuSel()
melangerLeTout()
faireCuire()</pre>
</python> 
<python>

Il serait également possible de l'écrire sous la forme suivante, mais placer
plusieurs instructions sur la même ligne est généralement considéré comme
une
très mauvaise habitude car cela complique grandement la relecture du code
après
coup.

</python> 
<python> 
<pre> casserLesOeufs(); ajouterDuSel(); melangerLeTout(); faireCuire();</pre>
</python> 
<java>

Bien entendu, ces méthodes n'existent pas en Java, mais il serait possible
de
les définir par vous même (nous verrons plus tard comment définir vos
propres méthodes).

</java> 
<python>

Bien entendu, ces méthodes n'existent pas en Python, mais il serait possible
de
les définir par vous même (nous verrons plus tard comment définir vos
propres méthodes).

</python> 

Pour l'instant, nous allons utiliser les instructions de la buggle. Il
y a une méthode pour chaque bouton du contrôle interactif.  Pour faire la
même
chose que le bouton **forward** (faire avancer la buggle d'un pas), il
faut
écrire dans l'éditeur : 
<java> 
<pre> forward();</pre>
</java> 
<python> 
<pre> forward()</pre>
</python> De même, pour faire l'équivalent des boutons **backward** , **turn left** et **turn right** , il faut utiliser respectivement : 
<java> 
<pre> backward();
turnLeft();
turnRight();</pre>
</java> 
<python> 
<pre> backward()
turnLeft()
turnRight()</pre>
</python> Le bouton **mark** est un peu particulier, puisqu'il correspond à deux
méthodes : la première lève le crayon, tandis que la seconde le baisse. 
<java> 
<pre> brushDown();
brushUp();</pre>
</java> 
<python> 
<pre> brushDown()
brushUp()</pre>
</python> 

La buggle offre d'autres méthodes, présentées dans le menu "Help/about this
world". Elles seront introduites au fur et à mesure des besoins.


### Objectif de cet exercice ###

