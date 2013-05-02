
## Buggle Dance Revolution (BDR) ##

Aujourd'hui est un grand jour : nous allons apprendre à nos buggles à jouer
à Dance Revolution, ce jeu très prisé de certains étudiants où le joueur
doit bouger sur un tapis prévu à cet effet en fonction des consignes
présentées à l'écran en rythme avec la musique. Mais avant cela, nous avons
quelques détails à étudier.


<java> 
### Conditionnelles sans accolades ###
</java> 
<java>

Il y a un détail que nous avons omis à propos de la syntaxe des
conditionnelles : si une branche ne contient qu'une seule instruction, les
accolades sont optionnelles. Ainsi, les deux extraits suivants sont
équivalents:

</java> 
<java> 
<pre> **if (condition** **) {quoiFaireSiLaConditionEstVraie();** **} else {quoiFaireSinon();** }</pre>
</java> 
<java> 
<pre> **if (condition** **)quoiFaireSiLaConditionEstVraie();** **elsequoiFaireSinon();** </pre>
</java> 
<java>

Mais attention, ceci peut être dangereux si on enchaîne les if comme dans l'exemple suivant.

</java> 
<java> 
<pre> if (isOverBaggle())
if (x == 5)
turnLeft();
else
turnRight();
forward();</pre>
</java> 
<java>

En fait, ça ne tourne pas à droite quand il n'y a pas un baggle par terre ET
que x vaut 5, mais quand la buggle a trouvé un baggle, et que x vaut une
autre valeur. Autrement dit, la buggle lit le code précédent comme suit
(notez que le else est décalé vers la droite par rapport à
précédemment) :

</java> 
<java> 
<pre> if (isOverBaggle())
if (x == 5)
turnLeft();
else
turnRight();
forward();</pre>
</java> 
<java>

La première leçon, c'est que l'indentation aide les humains à comprendre,
mais elle est sans importance pour la signification du code. On aurait tout
aussi bien pu écrire le code suivant et obtenir le même résultat. Mais
attention, si on veut qu'un humain puisse relire le code, l'indentation
devient très importante voire indispensable. C'est par exemple le cas si
votre code doit être relu par un professeur (pour qu'il le note ou pour lui
poser une question), ou si vous comptez réutiliser votre code plus tard.

</java> 
<java> 
<pre> if (isOverBaggle()) if (x == 5) turnLeft(); else turnRight(); forward();</pre>
</java> 
<java>

La seconde leçon, c'est qu'une branche else se raccroche toujours
au if le plus proche. C'est parfois un peu contre-intuitif, et il
est préférable d'ajouter plus d'accolades que nécessaire pour lever toute
ambiguïté.

</java> 
### Enchaînements de conditionnelles ###

Il arrive que l'on veuille demander à la buggle quelque chose similaire à :


<pre> s'il pleut, prend un parapluie;
si non, s'il fait chaud, prend une bouteille d'eau;
si non, si nous sommes le 14 juillet, prend un drapeau français</pre>

Le piège étant que nous voudrions qu'au plus l'une de ces actions soient
réalisées. C'est à dire, que s'il pleut un 14 juillet très chaud, on ne veut
pas que la buggle sorte avec un parapluie, de l'eau et un drapeau, mais
juste avec un parapluie. Le code suivant est donc faux.


<java> 
<pre> if (ilPleut()) {
prendreParapluie();
}
if (ilFaitChaud()) {
prendreDeLEau();
}
if (sommes14Juillet()) {
prendreDrapeau();
}</pre>
</java> 
<python> 
<pre> if ilPleut():prendreParapluie()
if ilFaitChaud():prendreDeLEau()
if sommes14Juillet():prendreDrapeau()</pre>
</python> 

En effet, toutes les conditions sont évaluées les unes après les autres, et
on risque donc d'aller au défilé un jour de pluie. À la place, il faut donc
écrire quelque chose comme :


<java> 
<pre> if (ilPleut()) {prendreParapluie();
} else {if (ilFaitChaud()) {prendreDeLEau();} else {if (sommes14Juillet()) {prendreDrapeau();}}
}</pre>
</java> 
<python> 
<pre> if ilPleut():prendreParapluie()
else:if ilFaitChaud():prendreDeLEau()else:if sommes14Juillet():prendreDrapeau()</pre>
</python> 
<java>

Une telle cascade de conditionnelles est un peu difficile à lire, et il est
préférable d'omettre les accolades associées aux else comme
suit. Il y a même certains langages qui introduisent un mot-clé spécial pour
ces else if (mais pas Java).

</java> 
<java> 
<pre> if (ilPleut()) {
prendreParapluie();
} else if (ilFaitChaud()) {
prendreDeLEau();
} else if (sommes14Juillet()) {
prendreDrapeau();
}</pre>
</java> 
<python>

Une telle cascade de conditionnelles est un peu difficile à lire, et il est
préférable d'omettre les indentations supplémentaires associées aux else comme suit. Le mot-clé elif du langage Python permet
de
faire ceci facilement. 
<python> 
<pre> if ilPleut():prendreParapluie()
elif ilFaitChaud():prendreDeLEau()
elif sommes14Juillet():prendreDrapeau()
}</pre>
</python> 
### Les graffitis dans le monde des buggles ###
Les buggles peuvent écrire des choses par terre dans leur monde. Pour ce
faire, elles utilisent les quatre méthodes suivantes:   
  
` boolean isOverMessage()` : renvoie vrai si et seulement s'il y a
un message écrit par terre.  
` String readMessage()` : renvoie le message qu'il y a écrit par
terre (s'il y a rien, on obtient une chaîne vide).  
` void writeMessage(String msg)` : écrit le message spécifié en
argument par terre. S'il y a déjà quelque chose écrit par terre, on ajoute
le nouveau message à la fin du précédent.  
` void clearMessage()` : efface ce qui est écrit par terre.  

### Objectif de cet exercice ###


<table border=1>
	<tr>
		<td > Indication L'objectif est donc d'organiser une partie de BDR entre
les buggles en leur apprenant à bouger en fonction des indications écrites
par terre.  Ces indications sont des messages au sol, avec le code suivant:</td>
		<td > Quoi faire </td>
		<td > Mnémotechnique </td>
	</tr>
	<tr>
		<td > R </td>
		<td > Tourner à droite et avancer d'une case </td>
		<td > Right </td>
	</tr>
	<tr>
		<td > L </td>
		<td > Tourner à gauche et avancer d'une case </td>
		<td > Left </td>
	</tr>
	<tr>
		<td > I </td>
		<td > Tourner en sens inverse (demi tour) et avancer d'une case </td>
		<td > Inverse </td>
	</tr>
	<tr>
		<td > A </td>
		<td > Avancer d'une case </td>
		<td > première lettre de l'alphabet </td>
	</tr>
	<tr>
		<td > B </td>
		<td > Avancer de deux cases </td>
		<td > deuxième lettre de l'alphabet </td>
	</tr>
	<tr>
		<td > C </td>
		<td > Avancer de trois cases </td>
		<td > troisième lettre de l'alphabet </td>
	</tr>
	<tr>
		<td > Z </td>
		<td > Reculer d'une case </td>
		<td > A une lettre de la fin de l'alphabet </td>
	</tr>
	<tr>
		<td > Y </td>
		<td > Reculer de deux cases </td>
		<td > à deux lettres de la fin de l'alphabet </td>
	</tr>
	<tr>
		<td > X </td>
		<td > Reculer de trois cases </td>
		<td > A trois lettres de la fin de l'alphabet </td>
	</tr>
</table>

Dans tous les autres cas, il faut s'arrêter.

Écrivez le code de la danse dans la méthode ` run()` dont le
prototype se trouve déjà dans l'éditeur.


### Indications ###

La première subtilité est que nous utiliserons la méthode ` Cet exercice peut sembler un peu compliqué, mais il s'agit principalement de
traduire le contenu du tableau ci-dessus dans un enchaînement de
conditionnelles.char
getIndication()` à la place de ` String
readMessage()` . Cette méthode, qui n'est connue que des buggles des
exercice BDR, renvoie le premier caractère du message au sol (ou ' ' s'il
n'y a rien d'écrit au sol).

L'autre subtilité est de travailler tant qu'on du travail à faire, i.e. tant
qu'on a pas trouvé une case n'étant pas décrite dans le tableau. Le plus
simple pour cela est de faire une boucle infinie ( ` while
(true)` ), avec tous les tests dans le corps. Si on trouve une case ne
répondant à aucune ligne du tableau, on arrêtera tout d'un simple ` return;` .

Les fonctions dont le type de retour est void peuvent contenir des return sans valeur associée. Cela interrompt immédiatement leur
exécution.


### Trucs et astuces ###
` Si vous ne parvenez plus à comprendre pourquoi votre buggle n'exécute pas
les pas de danse demandés, essayez d'ajouterbrushDown()`

Quand votre programme fonctionne enfin, passez à l'exercice suivant.

