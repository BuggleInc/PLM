## Construire avec méthode ##
Nous souhaitons maintenant apprendre à la buggle à se faire une niche.  La
première approche, la plus simple au premier abord, est d'écrire directement
le code permettant de le faire, comme ci-dessous (cela marche car la buggle
de cet exercice laisse une traînée rouge dans son sillage quand elle se
déplace).     forward();
    forward();
    turnLeft();
    forward();
    forward();
    turnLeft();
    forward();
    forward();
    turnLeft();
    forward();
    forward();
    turnLeft();

Les problèmes commencent quand nous voulons faire faire deux cabanes à la
buggle : Il faut réécrire le code deux fois, ce qui n'est pas très
pratique. Pire que ça, une telle duplication de code est en général très mal
vu. En effet, si vous vous rendez compte que vous vous êtes trompé dans
votre code, et que vous l'avez recopier à plusieurs endroits, il va falloir
le corriger plusieurs fois. Et gare si vous oubliez de corriger un
exemplaire.

Il est bien préférable de *factoriser votre code* , c'est-à-dire
d'écrire le code une seule fois, par exemple dans une méthode. C'est ce que
vous allez faire maintenant. Il est même possible d'aller plus loin en
factorisant le code de la méthode avec une boucle comme vu
précédemment. ### Objectif de cet exercice ###

À vous de jouer.

