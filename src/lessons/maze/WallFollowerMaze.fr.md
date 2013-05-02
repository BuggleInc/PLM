
## Longer les murs ##

Cette fois-ci le labyrinthe est beaucoup plus compliqué. Le hasard ne sera
pas suffisant, il va falloir être intelligent !

Heureusement, ce labyrinthe est plus simple qu'il n'y paraît: tous les murs
sont connectés les uns aux autres. Pour sortir de ce genre de labyrinthe, il
suffit à votre buggle de longer un mur (celui à sa droite, ou celui à sa
gauche: c'est sans importance). Et, tout en gardant sa patte posée sur ce
mur, votre buggle doit avancer jusqu'à ce qu'il trouve la sortie du
labyrinthe et ce biscuit qu'il apprécie tant.

Cet algorithme fonctionne ici car il n'y a pas d'île de murs isolés, ce qui
fait que la buggle ne peut pas boucler autour des murs sans rencontrer le
biscuit qu'elle cherche.


### Objectif de cet exercice ###

Comme dit dans l'introduction, il est sans importance de décider de suivre
le mur droit ou le mur gauche. Simplement, la démo suit le mur gauche, et il
serait donc avisé d'en faire de même dans votre solution pour simplifier la
comparaison de votre solution et de la démo.

Écrivez une méthode ` void keepHandOnSideWall()` qui fait avancer
votre buggle d'une case tout en gardant la patte sur le mur du côté
choisi. Vous devez donc vous assurer que votre buggle garde toujours la
patte
sur le mur et également qu'il ne risque pas de percuter un mur. Vous pouvez
regarder l'indice (hint) si vous êtes coincés, mais vous devriez d'abord
essayer de le faire sans l'indice.

Enfin, écrivez la méthode ` void run()` qui cherche le mur le plus
proche (méthode ` void putHandOnSideWall()` ), puis parcours le
labyrinthe (méthode ` void keepHandOnSideWall()` ) jusqu'à trouver
la sortie et le biscuit. Enfin il ne faut pas oubliez de prendre la baggle.

  
  

Quand votre buggle a un mur à sa gauche, il faut considérer trois situations
possible, qui dépendent des murs alentours. Le tableau suivant représente
graphiquement chaque situation initiale, et où vous devez placer votre
buggle à la fin de l'étape.



<table border=1>
	<tr>
		<td > </td>
		<td > Case 1 </td>
		<td > Case 2 </td>
		<td > Case 3 </td>
	</tr>
	<tr>
		<td > Situation initiale </td>
		<td > <img src="lessons/maze/1A.png" /> </td>
		<td > <img src="lessons/maze/2A.png" /> </td>
		<td > <img src="lessons/maze/3A.png" /> </td>
	</tr>
	<tr>
		<td > Étape suivante </td>
		<td > <img src="lessons/maze/1B.png" /> </td>
		<td > <img src="lessons/maze/2B.png" /> </td>
		<td > <img src="lessons/maze/3B.png" /> </td>
	</tr>
</table>

Si vous faites un ` turnRight()` dans tous les cas à la fin de
votre fonction, il est possible de l'écrire en trois lignes avec une boucle ` while()` .

