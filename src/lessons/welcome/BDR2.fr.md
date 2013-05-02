
## Buggle Dance Revolution 2 ##

### (BDR2) ###
Le BDR, c'est cool, mais c'est un peu le chaos. Tout d'abord, les buggles
gigotent en tout sens, et en plus, le code que vous avez écrit pour les
faire bouger est très difficile à lire. Voici un nouveau monde de BDR, où
les buggles vont faire une gentille petite ronde plus reposante. Nous
profiterons de cette accalmie pour nettoyer un peu le code grâce aux
nouveaux éléments que nous allons maintenant étudier. 
### Branchement conditionnelswitch  ###
Le plus difficile à lire du code précédent est certainement la cascade de
conditionnelles. Quelque part dans votre programme, vous avez sans doute
écrit quelque chose comme: 
<pre> if (getIndication() == 'R') {
turnRight();
forward();
} else if (getIndication() == 'L') {
turnLeft();
forward();
} else if (getIndication() == 'I') {
turnBack();
forward();
/* d'autres else if */
} else {
return;
}</pre>
Quand on relit ce programme, on ne voit pas forcément tout de suite qu'il
s'agit simplement d'un choix à 4 branches selon la valeur de
getIndication(). Pour faire mieux, on va utiliser la construction switch , dont la syntaxe est la suivante: 
<pre> **switch (expression** **) { casepremierValeur** **:queFaireSiExpressionVautPremiereValeur();** **break; casedeuxiemeValeur** **:queFaireSiExpressionVautDeuxiemeValeur();** **break; casetroisiemeValeur** **:queFaireSiExpressionVautTroisiemeValeur();** **break; /* autant de cas sur le même modèle qu'on le souhaite */ default:queFaireSiExpressionVautAucuneDesValeursProposees();** }</pre>
Remarquez que chaque branche du switch doit être terminée par un ` break` . Si on l'oublie, la machine continue d'exécuter le cas
suivant dans la liste quand elle a fini le code du cas où elle a sauté dans
le switch. Il y a même quelques **très rares** cas où ce comportement est
pratique.

On peut réécrire le code BDR précédent bien plus clairement grâce à la
construction switch de la façon suivante.


<pre> switch (getIndication()) {
case 'R':
turnRight();
forward();
break;
case 'L':
turnLeft();
forward();
break;
case 'U':
turnBack();
forward();
break;
default:
return;
}</pre>

## Variables partagées par les méthodes ##
Un autre problème de votre code est qu'il commence à être un peu long pour
être dans une seule fonction. On voudrait découper en deux méthodes:   
  
*  ` danceOneStep()` s'occuperait de faire un pas de danse  
*  ` run()` s'occuperait de faire la danse en entier, c'est à dire de
faire des pas de danse tant que l'on n'est pas arrivé à une case ne
demandant pas d'aller plus loin.  

Le problème est de faire en sorte que danceOneStep() prévienne run() qu'il n'y a plus de pas possible. La solution la plus simple
est
encore d'avoir une variable booléenne visible depuis les deux fonctions
indiquant s'il reste des pas à faire, ou si on a fini. On écrira donc en
dehors
de toute méthode : 
<pre> boolean moreMusic = true;</pre>

Notez que s'il est possible d'écrire des déclarations en dehors des
méthodes, les instructions doivent obligatoirement être placées dans des
méthodes. En Java, on appelle ces variables *globales* à plusieurs
méthodes des **champs** .

Ensuite, la fonction danceOneStep() doit être modifiée pour mettre
cette valeur à false quand il n'y a plus rien à faire. Pour cela,
ajoutez simplement ` moreMusic = false;` avant tout return .

On peut alors utiliser la fonction run() suivante: 
<pre> public void run() {
while (moreMusic)
danceOneStep();
}</pre>

### Objectif de cet exercice ###

Vous n'avez pas à écrire la fonction Réécrivez le code des buggles en appliquant les
améliorations que nous venons de voir.run , que les buggles
connaissent déjà. Si vous tentez de la définir malgré tout, le compilateur
se plaindra de trouver deux définitions de la même fonction, sans se rendre
compte qu'il s'agit de la même fonction. Contentez vous de déclarer la
variable moreMusic et la méthode danceOneStep() .

Voici les indications au sol à utiliser pour BDR2. Remarquez qu'on peut
maintenant avancer la buggle de 6 cases d'un coup. 

<table border=1>
	<tr>
		<td > Indication </td>
		<td > Quoi faire </td>
	</tr>
	<tr>
		<td > R </td>
		<td > Tourner à droite et avancer d'une case </td>
	</tr>
	<tr>
		<td > L </td>
		<td > Tourner à gauche et avancer d'une case </td>
	</tr>
	<tr>
		<td > I </td>
		<td > Tourner en sens inverse (demi-tour) et avancer d'une case </td>
	</tr>
	<tr>
		<td > A </td>
		<td > Avancer d'une case </td>
	</tr>
	<tr>
		<td > B </td>
		<td > Avancer de deux cases </td>
	</tr>
	<tr>
		<td > C </td>
		<td > Avancer de trois cases </td>
	</tr>
	<tr>
		<td > D </td>
		<td > Avancer de quatre cases </td>
	</tr>
	<tr>
		<td > E </td>
		<td > Avancer de cinq cases </td>
	</tr>
	<tr>
		<td > F </td>
		<td > Avancer de six cases </td>
	</tr>
	<tr>
		<td > Z </td>
		<td > Reculer d'une case </td>
	</tr>
	<tr>
		<td > Y </td>
		<td > Reculer de deux cases </td>
	</tr>
	<tr>
		<td > X </td>
		<td > Reculer de trois cases </td>
	</tr>
	<tr>
		<td > W </td>
		<td > Reculer de quatre cases </td>
	</tr>
	<tr>
		<td > V </td>
		<td > Reculer de cinq cases </td>
	</tr>
	<tr>
		<td > U </td>
		<td > Reculer de six cases </td>
	</tr>
</table>

Dans tous les autres cas, il faut s'arrêter.

Quand votre programme fonctionne de nouveau, passez à l'exercice suivant.

