
# LightBotWorld #
Ce monde introduit un petit puzzle de programmation qui peut être utilisé
pour introduire la programmation aux enfants ne sachant pas lire. En effet,
il est programmé de manière graphique. L'objectif de chaque niveau est
d'allumer toutes les lumières du plateau en utilisant le petit
robot. Celui-ci comprend les ordres suivants : 

<table border=1>
	<tr>
		<td > **Ordre** </td>
		<td > **Signification** </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/forward.png" /> </td>
		<td > **Avancer** Ne peut être utilisé si la case d'arrivée n'est pas à la même altitude que
la case de départ. </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/jump.png" /> </td>
		<td > **Sauter en avant** Ne peut être utilisé que si la case d'arrivée est un étage plus haute que la
case de départ, ou si l'arrivée est plus basse que le départ. Ne peut être
utilisée pour des déplacements à plat. </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/left.png" /> </td>
		<td > **Tourner à gauche** . </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/right.png" /> </td>
		<td > **Tourner à droite** . </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/light.png" /> </td>
		<td > **Allumer ou éteindre** Allumer l'ampoule si elle était éteinte, et éteindre si elle était
allumée. Sans effet s'il n'y a pas d'ampoule dans la case courante. </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/f1.png" /> </td>
		<td > **Appeller la fonction 1** . </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/f2.png" /> </td>
		<td > **Appeller la fonction 2** . </td>
	</tr>
</table>

Veuillez noter que ce jeu n'est pas vraiment adapté aux jeunes enfants
puisque la principale difficulté vient de la limitation du nombre
d'instructions utilisable dans chaque programme. Les niveaux avancés ne
peuvent être résolus sans écrire des fonctions pour factoriser le code, ce
qui est souvent au delà des capacités des jeunes enfants.

Ce jeu est très largement inspiré d'un jeu en flash du même nom, que l'on
peut par exemple trouver sur kongregate.com. Il a été écrit par Danny
Yaroslavski (Coolio-Niato), d'après une idée originale de Matt Chase.

