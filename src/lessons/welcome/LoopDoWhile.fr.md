
## Boucles jusqu'à(do ... while)  ##

<python>

Attention, cette leçon est pour le moment seulement écrite en Java et fait
appel à une notion qui n'existe pas en Python. Oubliez la.

</python> 

Dans une boucle while , la condition est évaluée avant toute chose,
et si elle est fausse, le corps de la boucle n'est jamais exécuté. Il arrive
parfois que l'on veuille que le corps de la boucle soit évalué au moins une
fois, même si la condition est initialement fausse. On utilise pour cela une
variante de la boucle while , dont la syntaxe Java est la suivante : 
<pre> **do {action()** **; } while (condition** );</pre>

### Objectif de cet exercice ###

L'idée général est donc de faire: 
<pre> Certaines cases du monde sont jaunes, mais votre buggle
ne supporte pas de s'y trouver comme c'est son cas actuellement. Écrivez le
code lui permettant d'avancer jusqu'à se trouver sur une case blanche. Vous
utiliserez pour cela la méthode isGroundWhite(), que seule la buggle de cet
exercice connaît.avancer jusqu'à se trouver sur une case blanche</pre>

*Remarque :* il est également possible de résoudre cet exercice avec
une boucle while classique, mais ce n'est pas l'objectif.

