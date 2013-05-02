
## Méthodes ##

Nous allons maintenant voir comment écrire nos propres méthodes. Il s'agit
en quelque sorte d'étendre le vocabulaire de la buggle en lui apprenant à
faire de nouvelles choses.

Par exemple, nous avons vu à l'exercice précédent comment demander à la
buggle d'aller chercher la buggle qui se trouve devant elle, et la ramener à
sa position initiale. S'il y a maintenant plusieurs baggles sur le plateau,
et que nous voulons tous les ramener sur la ligne du bas, il faut soit
répéter ce code plusieurs fois, soit l'inclure dans une boucle. Dans les
deux cas, le code résultant n'est pas très lisible, et il serait mieux que
la buggle comprenne un ordre de type ` goAndGet()` tout comme elle
comprend un ` forward()` .


<python>

La syntaxe pour écrire une méthode simple nommée ` goAndGet` est
la suivante: 
<pre> public void goAndGet() {
actions();
aFaire();
}</pre>

<java>

La syntaxe pour écrire une méthode simple nommée ` goAndGet` est
la suivante: 
<pre> public void goAndGet() {
actions();
aFaire();
}</pre>

<java>

Le corps de la méthode (entre les accolades) peut contenir autant
d'instructions que vous le souhaitez, et toutes les constructions vues
jusque là (for, while, if, etc).

</java> 
<python>

Le corps de la méthode (entre les accolades) peut contenir autant
d'instructions que vous le souhaitez, et toutes les constructions vues
jusque là (for, while, if, etc).

</python> 
<java>

Nous reviendrons plus tard sur le sens exact du mot-clé ` public` . Disons simplement pour l'instant qu'il indique que tout
le monde a le droit d'utiliser cette méthode.

</java> 
<java>

` void` indique quant à lui que la méthode ne donne pas de
résultat. Nous avions vu que la méthode ` isOverBaggle()` renvoie
un booléen, et peut donc être utilisé comme une condition dans un if ou un
while. Cela signifie qu'elle a été déclarée de la façon suivante : 
<pre> public boolean isOverBaggle() { ... }</pre>
Nous verrons dans le prochain exercice comment faire ce genre de chose. Pour
l'instant, contentons nous d'écrire ce ` void` à cet endroit.

</java> 
<java>

En Java, il est d'usage de mettre la première lettre du nom d'une méthode en
minuscules, et de coller tous les mots décrivant la méthode les uns aux
autres en mettant une majuscule à chacun. Il est interdit de mettre des
espaces ou des accentués dans le nom d'une méthode. C'est pourquoi nous
écrivons "go and get" sous la forme "goAndGet".

</java> 
<python>



</python> 
### Objectif de cet exercice ###
` L'objectif de cet exercice est donc d'écrire une
méthode nomméegoAndGet()` 
<python>

La particularité de cet exercice est que tout votre code doit être écrit
dans cette fonction, avec rien en dehors. Le code qui appelle votre fonction
sera automagiquement ajouté quand vous cliquerez sur **et qui fait la même chose qu'aux exercices précédents (avance tant qu'on ne trouve pas de baggle, ramasser le baggle, reculer à la case départ, poser le baggle).Start** .

</python> 
<java>

La particularité de cet exercice est que vous n'allez donc pas écrire
directement le code que la buggle va exécuter, mais une méthode qu'elle
utilisera. En fait, dans tous les exercices précédents, vous avez écrit le
corps d'une méthode particulière de la buggle nommée ` run()` et
qui est invoquée par l'environnement quand on clique sur le bouton **Start** .

</java> **Début** 
<java>

N'essayez pas de définir ici cette méthode, puisque la buggle la connaît
déjà. Cela troublerait le compilateur qui vous donnerait un message d'erreur
en échange. Pour information, voici le corps de la méthode ` .run()` de la buggle de cet exercice: 
<pre> public void run() {for (int i=0; i7; i++) {goAndGet();turnRight();forward();turnLeft();}
}</pre>


</java>

Donc, votre buggle va répéter 7 fois (la taille du monde)  l'opération ` goAndGet()` que vous allez écrire, plus les opérations
nécessaire à se décaler d'une colonne vers la droite (tourner à droite,
avancer, tourner à gauche). Notez que la buggle va donc faire un pas vers la
droite alors qu'elle sera tout à droite du monde. Cela la ramènera sur le
bord gauche, car le monde des buggles est torique.

À vous d'écrire cette méthode goAndGet().

