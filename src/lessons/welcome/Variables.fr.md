## Stocker et manipuler des données ##

Les programmes que nous avons écrit jusque là passent à coté d'un point
fondamental en informatique. En effet, la science informatique est celle de
traiter des *données* grâce à des *instructions* . Dans le monde
des buggles, les principales données sont cachées derrière la représentation
graphique, mais ce n'est pas une raison pour ne jamais manipuler
explicitement des données. ### Les données en Java ###
### Data in Python ###
In a program, you can use several of data, such as integers or
strings of chars. If you want to use a data several times, you need to store
it within a , which is a memory cell containing a value. It's
not very different from a shelve containing a book: you put your data (say
'5') in the variable (say 'length'), and you can retrieve it latter when you
need it.

Java is said to be a language, which means that it is only
possible to store a value in a variable of the right type. Don't think about
storing the letters of your name into an integer variable. In other
languages (such as Python)  allow you to store any kind of data in any
variable without such restriction, but not in Java.

The Python language is said to , which means you can store
any type of data into a given variable. Other languages (such as Java)
mandate that each variable store only data of a given type, but there is no
such difficulties here.

Pour (i.e. créer) une variable, il suffit d'écrire son type,
un espace et le nom de la variable. Notez qu'il n'est pas possible
d'utiliser d'accents dans les noms de variables, malheureusement. Parmi les
types existants, on trouve: *int* pour les entiers, *double* pour
les nombres à virgule, *boolean* pour les booléens (i.e. les variables
qui peuvent être soit vraies soit fausses) et *String* pour les chaînes
de caractères.  Si l'on veut, on peut faire suivre la déclaration du signe =
suivi de la valeur initiale à donner à la valeur.

Ainsi, pour créer une variable nommée *x* contenant des entiers, on
écrira :     int x;
Si on veut que sa valeur initiale soit 5, on écrira :     int x = 5;

Plus tard dans le programme, si l'on souhaite une nouvelle
valeur à la variable, c'est très simple :     x = 3;

La syntaxe pour créer une variable entière de valeur initiale
4 est la suivante :     int x = 4;

C'est la même histoire pour les chaînes, nombres à virgule flotante et les
booléens.

    boolean marie=true;

(ie, creating) a variable in Python is dead simple: you
just need to give it an initial value by writing its name, the equal sign
and the value.

So, to create a variable named *x* which initial value should be 5, you
should type:     x = 5

Plus tard dans le programme, si l'on souhaite une nouvelle
valeur à la variable, c'est très simple :     x = 3

C'est la même histoire pour les chaînes, nombres à virgule flotante et les
booléens.

    

À droite du signe égal, on peut mettre une expression quelconque, qui peut
contenir des constantes, des variables et des opérations :     x = 3 + 2;
    x = 3 * x;
    hello = "Salut "+nom;
### Objectif de cet exercice ###
Il est temps de faire un exercice un peu plus dur, n'est ce pas ? L'objectif
cette fois est d'avancer jusqu'au baggle qui se trouve devant la buggle, le
ramasser, revenir à la position initiale, puis de poser le baggle. ### Comment faire ? ###

Pour résoudre ce problème, il faut le décomposer en parties que vous savez
résoudre. Par exemple, on peut vouloir faire les étapes suivantes : 1.    Avancer jusqu'à se trouver sur un baggle
1.    Ramasser le baggel au sol
1.    Reculer du même nombre de cases que ce qu'on a avancé
1.    Reposer le baggel au sol

Bien entendu, il est impossible de reculer du bon nombre de case à l'étape 3
si l'on a pas compté le nombre de pas faits à la première étape. On va pour
cela utiliser une variable, que l'on peut nommer .

On crée cette variable (de type ) avant l'étape 1, on
l'initialise à 0, et chaque fois qu'on avance d'un pas, on l'incrémente de 1
( ou , les deux
écritures sont équivalentes).

Create a variable before phase 1, initialize it to 0, and each time you move
one step forward, increment its value by one ( ).

Ensuite, l'étape 3 consiste simplement à créer une nouvelle variable entière initialisée à zéro, et reculer d'un pas tant que n'est pas égal à , en incrémentant à chaque fois.

Please note that it is forbidden to use spaces in variable names. So you can
name you variable , but is
not a valid name.

À vous de jouer !

