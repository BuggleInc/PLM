
## Stocker et manipuler des données ##

Les programmes que nous avons écrit jusque là passent à coté d'un point
fondamental en informatique. En effet, la science informatique est celle de
traiter des **données** grâce à des **instructions** . Dans le monde
des buggles, les principales données sont cachées derrière la représentation
graphique, mais ce n'est pas une raison pour ne jamais manipuler
explicitement des données. 
<java> 
### Les données en Java ###
</java> 
<python> 
### Les données en Python ###
</python> Java permet d'utiliser différents *types* de données, tels que les
entiers, les nombres à virgules ou les chaînes de caractères. Si on veut
utiliser une donnée plusieurs fois, il faut la stocker dans une *variable* , qui est une sorte de case contenant une valeur. Ce n'est
pas très différent d'une étagère contenant un livre: vous rangez votre
donnée (disons '5') dans la variable (disons 'longueur'), et vous pouvez la
retrouver plus tard quand vous en avez besoin. 
<java>

Java est un langage *typé* , ce qui veut dire que l'on ne peut stocker
une valeur que dans une variable du bon type. Pas question de stocker votre
nom dans une variable entière.

</java> 
<python>

Le langage Python est dit *non typé* , ce qui signifie qu'une variable
donnée peutcontenir n'importe quel type de donnée. D'autres langages ( comme
Java) rende obligatoire que chaque variable  ne puisse contenir que des
données d'un certain type, mais il n'y a pas de telles difficultés ici.

</python> 
<java>

Pour *déclarer* (i.e. créer) une variable, il suffit d'écrire son type,
un espace et le nom de la variable. Notez qu'il n'est pas possible
d'utiliser d'accents dans les noms de variables, malheureusement. Parmi les
types existants, on trouve: **int** pour les entiers, **double** pour
les nombres à virgule, **boolean** pour les booléens (i.e. les variables
qui peuvent être soit vraies soit fausses) et **String** pour les chaînes
de caractères.  Si l'on veut, on peut faire suivre la déclaration du signe =
suivi de la valeur initiale à donner à la valeur.

</java> 
<java>

Ainsi, pour créer une variable nommée **x** contenant des entiers, on
écrira : 
<pre> int x;</pre>
Si on veut que sa valeur initiale soit 5, on écrira : 
<pre> int x = 5;</pre>


</java> 
<java>

Plus tard dans le programme, si l'on souhaite *affecter* une nouvelle
valeur à la variable, c'est très simple : 
<pre> x = 3;</pre>

<java>

La syntaxe pour créer une variable entière ` x` de valeur initiale
4 est la suivante : 
<pre> int x = 4;</pre>


</java> 
<java>

C'est la même histoire pour les chaînes, nombres à virgule flottante et les
booléens.

</java> 
<java> 
<pre> 
<comment> ( String nom = "Martin Quinson";
double taille=1.77;// en mètres) </comment> boolean marie=true;</pre>
</java> 
<python>

*Déclarer* (ie créer) une variable en Python est extrèmement simple:
vous avez juste besoin de lui donner une valeur initiale en écrivant son
nom, le signe égal et la valeur.

</python> 
<python>

Ainsi, pour créer une variable nommée **x** dont la valeur initiale sera
5, on écrira : 
<pre> x = 5;</pre>


</python> 
<python>

Plus tard dans le programme, si l'on souhaite *affecter* une nouvelle
valeur à la variable, c'est très simple : 
<pre> x = 3;</pre>

<python>

C'est la même histoire pour les chaînes, nombres à virgule flottante et les
booléens.

</python> 
<python> 
<pre> 
<comment> ( prenom = "Martin"
nom = 'Quinson'# les simples et les doubles quotes fonctionnent ici) </comment> 
<comment> ( devise = "Je ne finis jam' (mais je continue d'essayer)"# avoir des quotes simples dans des doubles quotes fonctionne) </comment> 
<comment> ( taille=1.77# en mètre) </comment> 
<comment> ( marie=True# le contraire serait marqué False) </comment> </pre>
</python> 

À droite du signe égal, on peut mettre une expression quelconque, qui peut
contenir des constantes, des variables et des opérations : 
<pre> x = 3 + 2;
x = 3 * x;
hello = "Salut "+nom;</pre>

### Objectif de cet exercice ###
Il est temps de faire un exercice un peu plus dur, n'est ce pas ? L'objectif
cette fois est d'avancer jusqu'au baggle qui se trouve devant la buggle, le
ramasser, revenir à la position initiale, puis de poser le baggle. 
### Comment faire ? ###

Pour résoudre ce problème, il faut le décomposer en parties que vous savez
résoudre. Par exemple, on peut vouloir faire les étapes suivantes :   
  
Avancer jusqu'à se trouver sur un baggle  
Ramasser le baggel au sol  
Reculer du même nombre de cases que ce qu'on a avancé  
Reposer le baggel au sol  

Bien entendu, il est impossible de reculer du bon nombre de case à l'étape 3
si l'on a pas compté le nombre de pas faits à la première étape. On va pour
cela utiliser une variable, que l'on peut nommer ` nbPas` .


<java>

On crée cette variable (de type ` int` ) avant l'étape 1, on
l'initialise à 0, et chaque fois qu'on avance d'un pas, on l'incrémente de 1
( ` nbPas = nbPas + 1;` ou ` nbPas++;` , les deux
écritures sont équivalentes).

</java> 
<python>

On crée cette variable (de type ` int` ) avant l'étape 1, on
l'initialise à 0, et chaque fois qu'on avance d'un pas, on l'incrémente de 1
( ` nbPas = nbPas + 1;` ou ` nbPas++;` , les deux
écritures sont équivalentes).

</python> 

Ensuite, l'étape 3 consiste simplement à créer une nouvelle variable entière ` dejaFait` initialisée à zéro, et reculer d'un pas tant que ` dejaFait` n'est pas égal à ` nbPas` , en incrémentant ` dejaFait` à chaque fois.

Attention, il est interdit d'utiliser des caractères accentués ou des
espaces dans les noms de variables Java. Vous pouvez donc nommer votre
variable ` dejaFait` , mais ni ` déjaFait` ni ` deja
Fait` ne sont des noms valides.

À vous de jouer !

