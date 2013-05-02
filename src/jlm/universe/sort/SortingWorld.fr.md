
# Monde des tris #
Ce monde vous donne les outils pour expérimenter les algorithmes de tris. On
peut l'utiliser de deux manières différentes : la première est bien sûr
d'écrire les algorithmes de tri demandés. Mais on peut aussi se contenter
dans un premier temps de lancer la démo de chaque exercice et observer les
algorithmes de tri fonctionner. Cela permet de mieux se rendre compte des
différences d'efficacité entre eux. 
## Méthodes disponibles pour les algorithmes de tri ##


<table border=1>
	<tr>
		<td > **Méthode** </td>
		<td > **Action** </td>
		<td > **Coût** </td>
	</tr>
	<tr>
		<td > int getValueCount() </td>
		<td > Retourne le nombre de valeurs dans le tableau </td>
		<td > aucun </td>
	</tr>
	<tr>
		<td > boolean isSmaller(int i, int j) </td>
		<td > Retourne vrai ssi le contenu de la case i est inférieur à celui de la case j </td>
		<td > deux lectures </td>
	</tr>
	<tr>
		<td > boolean isSmallerThan(int i, int val) </td>
		<td > Retourne vrai ssi le contenu de la case i est inférieur à la valeur val </td>
		<td > une lecture </td>
	</tr>
	<tr>
		<td > void swap(int i, int j) </td>
		<td > Echange le contenu de la case i avec celui de la case j </td>
		<td > deux lectures, deux écritures </td>
	</tr>
	<tr>
		<td > void copy(int from, int to) </td>
		<td > Copie le contenu de la case 'from' dans la case 'to' </td>
		<td > une lecture, une écriture </td>
	</tr>
	<tr>
		<td > int getValue(int idx) </td>
		<td > Retourne la valeur de la case idx </td>
		<td > une lecture </td>
	</tr>
	<tr>
		<td > void setValue(int idx,int val) </td>
		<td > Affecte la valeur 'val' à la case 'idx' </td>
		<td > une écriture </td>
	</tr>
	<tr>
		<td > void sorted(int idx) </td>
		<td > Indique que la case 'idx' a été triée avec succès (utilisé seulement pour
l'affichage) </td>
		<td > aucun </td>
	</tr>
</table>

