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
la buggle comprenne un ordre de type tout comme elle
comprend un .

La syntaxe pour écrire une méthode simple nommée est
la suivante:     def goAndGet():
    actions();
    to();
    do();

La syntaxe pour écrire une méthode simple nommée est
la suivante:     public void goAndGet() {
    actions();
    aFaire();
    }

Le corps de la méthode (entre les accolades) peut contenir autant
d'instructions que vous le souhaitez, et toutes les constructions vues
jusque là (for, while, if, etc).

The method body (the code that is indended) can contain as many instructions
as you want, and any construction we saw so far (for, while, if, etc).

Nous reviendrons plus tard sur le sens exact du mot-clé . Disons simplement pour l'instant qu'il indique que tout
le monde a le droit d'utiliser cette méthode.

indique quant à lui que la méthode ne donne pas de
résultat. Nous avions vu que la méthode renvoie
un booléen, et peut donc être utilisé comme une condition dans un if ou un
while. Cela signifie qu'elle a été déclarée de la façon suivante :     public boolean isOverBaggle() { ... }
Nous verrons dans le prochain exercice comment faire ce genre de chose. Pour
l'instant, contentons nous d'écrire ce à cet endroit.

En Java, il est d'usage de mettre la première lettre du nom d'une méthode en
minuscules, et de coller tous les mots décrivant la méthode les uns aux
autres en mettant une majuscule à chacun. Il est interdit de mettre des
espaces ou des accentués dans le nom d'une méthode. C'est pourquoi nous
écrivons "go and get" sous la forme "goAndGet".

FIXME: add here a word about the variable naming schema. Either caseBased or
as_in_C

### Objectif de cet exercice ###

One particularity of this exercise is that all your code should be written
in this function, with nothing out of it. The code that calls your function
will be automagically added when you click on *et qui fait la même chose qu'aux exercices précédents (avance tant qu'on ne trouve pas de baggle, ramasser le baggle, reculer à la case départ, poser le baggle).Start* .

La particularité de cet exercice est que vous n'allez donc pas écrire
directement le code que la buggle va exécuter, mais une méthode qu'elle
utilisera. En fait, dans tous les exercices précédents, vous avez écrit le
corps d'une méthode particulière de la buggle nommée et
qui est invoquée par l'environnement quand on clique sur le bouton *Start* .

N'essayez pas de définir ici cette méthode, puisque la buggle la connaît
déjà. Cela troublerait le compilateur qui vous donnerait un message d'erreur
en échange. Pour information, voici le corps de la méthode de la buggle de cet exercice:     public void run() {for (int i=0; i7; i++) {goAndGet();turnRight();forward();turnLeft();}
    }

Your buggle will repeat 7 times (which matches the world's dimension)
sequence constituted of a call to the method that
you should write, plus a move to get to the next row (turn right, move
forward, turn left). As you can see, the buggle will do one step right from
the right border of the world. It will bring it back to the left side since
its world is a torus.

À vous d'écrire cette méthode goAndGet().

