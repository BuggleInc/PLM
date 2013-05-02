
# Tricots et tableaux #
L'objectif de cet exercice est de reproduire le motif de la première colonne
en le décalant d'une case (voir l'onglet Objectives pour plus de
détails). La grande différence entre cet exercice et les précédents sur les
motifs, c'est qu'il faut maintenant lire (sur la première colonne) le motif
souhaité, puis le reproduire ensuite. Il est impossible de faire autrement
car votre programme sera exécuté par trois buggles dans trois mondes
différents, chacune ayant un motif propre à reproduire.

Une possibilité est de lire la prochaine case, puis d'aller la recopier en
position, avant de revenir lire la case suivate, etc. Mais comme vous n'avez
pas le droit d'utiliser les méthodes permettant de téléporter la buggle à
une case particulière ( ` setPos()` et autres), cette façon de
faire va être très pénible à mettre en place.

Le plus simple est de stocker l'enchainement de couleurs dans un **tableau** . 
## Les tableaux en Java ##
Un tableau est une séquence d'emplacements dans lesquels on peut mettre des
valeurs de même type (une par emplacement). C'est donc une séquence de cases
de même type :



<table border=0>
	<tr>
		<td > T </td>
		<td > 3 </td>
		<td > 7 </td>
		<td > 13 </td>
		<td > 8 </td>
		<td > 15 </td>
		<td > 6 </td>
		<td > 11 </td>
		<td > 10 </td>
		<td > 9 </td>
	</tr>
</table>

T est le nom du tableau, T[0] est le nom de la première case, T[1] de la
deuxième case, T[2] de la troisième case, etc... Et oui, la première case
est T[0] et la dernière case d'un tableau de dimension N est T[N - 1].

On peut utiliser une variable entière *i* pour accéder avec T[i] aux
cases d'un tableau : lorsque *i* vaut 0 alors T[i] dénote la case T[0],
lorsque *i* vaut 10, T[i] dénote T[10]. On dit alors que *i* est
un **indice** dans le tableau T . 
## Remplissage d'un tableau ##
Supposons que ` T` soit un tableau de 10 éléments entiers.  On
peut alors le remplir de la manière suivante : 
<pre> for (int i = 0; i10; i++) {
T[i] = 3;
}</pre>
` T[i]` s'utilise comme une variable. On peut l'affecter : 
<pre> T[i] = 78;</pre>

On peut lire sa valeur : 
<pre> x = T[i];</pre>

On peut tester cette valeur : 
<pre> if (T[i] > 0 ) {
// instructions...
}</pre>


### Déclaration d'un tableau ###
Un tableau se déclare de la manière suivante : 
<pre> int[] T;</pre>

` int` indique que les éléments du tableau ` T` sont de
type entier est le nom du tableau, ` []` indique qu'il s'agit d'un
tableau. On peut aussi écrire cette déclaration de la manière suivante. Les
deux écritures sont syntaxiquement équivalente, mais la première est souvent
préférée en Java. 
<pre> int T[];</pre>


### Allocation d'un tableau ###
Déclarer un tableau ` T` nous réserve juste le nom ` T` pour l'utiliser plus tard. Mais le tableau n'est pas initialisé : il n'a pas
de valeur. Que voudrait dire ` T[4]` si nous n'avons pas encore
dit que ` T` est un tableau d'au moins 5 éléments ?

Avant tout, il faut donc lui affecter une valeur à ` T` : 
<pre> T = new int[10];</pre>
` new` indique qu'il faut créer quelque chose, et ` int[10]` indique qu'il s'agit d'un tableau de 10 valeur
entières. En réponse, un tableau d'entiers de longueur 10 est crée en
mémoire, et la variable ` T` référence ce tableau.

La taille d'un tableau est fixée et ne peut plus être changée après la
création du tableau. Pour connapitre la taille d'un tableau ` T` ,
on peut consulter la valeur de la variable ` T.length` .

On ne peut pas écrire : 
<pre> int T[10]; // FAUX !!!</pre>
Il faut absolument utiliser l'instruction ` new` . Par contre, on
peut très bien donner la dimension par une variable ` i` . 
<pre> T = new int[i];</pre>
Dans ce cas, la taille du tableau sera la valeur de ` i` **au moment où on a fait le** ` new` . Si ` i` change après
coup, cela ne modifie pas la taille du tableau. 
#### Déclaration et allocation ####

<pre> int[] T = new int[10];</pre>
On déclare et alloue le tableau en une seule ligne. 
#### Déclaration et initialisation ####

<pre> int[] T = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };</pre>
On déclare, alloue et initialise le tableau en une seule ligne. Pour
connaître la taille du tableau à allouer, le compilateur compte les valeurs
données.  Ce code est équivalent à : 
<pre> int[] T = new int[10];
T[0] = 1;
T[1] = 2;
...
T[9] = 10;</pre>
C'est aussi équivalent au code : 
<pre> int[] T = new int[10];
for (int i=0; iT.length; i++) {
T[i] = i+1;
}</pre>

### Tableaux et paramètres de méthode ###
On peut tout à fait passer un tableau en paramètre d'une méthode. La méthode
doit l'indiquer dans son prototype de la façon suivante: 
<pre> void maMethode(int[] valeurs) {
// faire quelque chose
}</pre>
Coté appelant, c'est aussi simple : 
<pre> int[] tab = new int[10];
// initialiser les valeurs
maMethode(tab);</pre>

On peut également avoir des méthodes renvoyant des tableaux en résultat : 
<pre> int[] autreMethode() {
int[] resultat = new int[10];
// faire quelque chose
return resultat;
}</pre>

## Objectif de l'exercice ##
La méthode ` run()` que vous devez écrire doit commencer par
déclarer un tableau de couleurs ( ` Color[]` ) et
l'allouer. Attention, le premier monde est de taille 6x6, mais ce n'est pas
le cas des autres. Utilisez donc la méthode ` getWorldHeight()` pour retrouver le nombre de lignes du monde actuel.

Une fois le tableau alloué, il faut le remplir. Pour chaque case de la
colonne, lisez la couleur du sol (avec ` getGroundColor()` ), et
stockez le résultat de cette méthode dans la bonne case du tableau.

Une fois le tableau initialisé, il faut répliquer le motif sur toutes les
colonnes, par exemple en exécutant ` getWorldWidth()` fois une
méthode écrite tout exprès.

À vous de jouer.

