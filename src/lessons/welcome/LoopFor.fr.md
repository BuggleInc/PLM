
## Boucles pour(for)  ##

<python>

Attention, cette leçon est pour le moment écrite seulement en Java. Adapter
la de vous-même

</python> 

Les boucles while sont bien adaptées aux situations où l'on veut
réaliser une action tant qu'une condition est réalisée, mais elles sont
moins pratiques pour réaliser une action un nombre prédéterminé de fois. Par
exemple, lorsque nous voulions reculer de ` nbPas` dans l'exercice
précédent, il fallait créer une nouvelle variable, l'initialiser, et
demander à reculer tant que la nouvelle variable n'était pas égale à ` nbPas` , en incrémentant cette variable à la main à la fin du
corps de la boucle.

Dans ce genre de cas, les boucles de type ` for` sont plus
pratique. Leur syntaxe est la suivante : 
<pre> **for (initialisation** **;condition** **;incrémentation** **) {action** ();
}</pre>
Ce code est parfaitement équivalent à celui-ci : 
<pre> **initialisation** **; while (condition** **) {action** **();incrémentation** ;
}</pre>

Dans ce genre de cas, les boucles de type ` for` sont plus
pratique. Leur syntaxe est la suivante : 
<pre> **for (initialisation** **;condition** **;incrémentation** **) {action** ();
}</pre>
Ce code est parfaitement équivalent à celui-ci : 
<pre> **initialisation** **; while (condition** **) {action** **();incrémentation** ;
}</pre>

Par exemple, les deux codes suivants sont équivalents. Avouez que la seconde
forme est plus lisible, non ? 
<pre> int dejaFait = 0;
while (dejaFaitnbPas) {backward();dejaFait++;
}</pre>

<pre> for (int dejaFait = 0; dejaFaitnbPas; dejaFait++) {backward();
}</pre>

On peut imaginer des utilisations bien plus avancées des boucles for car toute instruction valide peut être utilisée comme
initialisation, condition et incrémentation. L'exemple suivant est un peu
extrême, puisqu'elle calcule le PGCD de deux nombres sans avoir de corps de
boucle du tout, ni d'initialisation (tout est fait dans la condition et
l'incrémentation). 
<pre> 
<comment> ( int x=20, y=3, tmp;
for (; y!=0 ; tmp=x, x=y, y=tmp%y) { }/* le PGCD est stocké dans x */) </comment> </pre>

Si vous ne comprenez pas tous les détails de cet exemple, pas de panique,
c'est assez logique puisqu'il utilise des points de syntaxe que nous n'avons
pas encore vu.


### Objectif de cet exercice ###
Il s'agit de refaire le même exercice que précédemment
(avancer jusqu'à trouver un baggle, le ramasser, revenir là où on était au
début puis reposer le baggle), mais en utilisant une bouclefor pour revenir au point de départ à la place d'une bouclewhile

Une fois ceci fait, passez à l'exercice suivant.

