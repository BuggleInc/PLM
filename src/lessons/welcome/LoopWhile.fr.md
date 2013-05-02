
## Boucles tant que(while)  ##

En plus des instructions conditionnelles, une autre construction pratique
est de pouvoir demander à la buggle de répéter une action tant qu'une
condition particulière n'est pas arrivée. On utilise pour cela une boucle while , dont la syntaxe est la suivante : 
<java> 
<pre> **while (condition** **) {action()** ;
}</pre>
</java> 
<python> 
<pre> **while (condition** **) {action()** ;
}</pre>
</python> 

Evidement, si l'action en question ne modifie pas la valeur de la condition,
la buggle va exécuter l'action à l'infini. C'est dans ce genre de cas que le
bouton **stop** de l'interface devient utile. Pour tester cela, vous
pouvez essayer de taper le code suivant dans l'éditeur : 
<java> 
<pre> while (true) {turnLeft();
}</pre>
</java> 
<java> 
<pre> while (true) {turnLeft();
}</pre>
</java> La buggle va tourner vers la gauche tant que ` true` est vrai
(sans fin donc) jusqu'à ce que vous l'arrêtiez manuellement avec le bouton
stop.


### Objectif de cet exercice ###

<pre> Il vous faut maintenant écrire le code nécessaire pour
que vos buggles avancent jusqu'à rencontrer un mur. L'idée est donc de faire
:tant que l'on est pas face à un mur, faire :avancer();</pre>

Quand votre programme fonctionne, passez à l'exercice suivant.

