
## Courbe du Dragon (2) ##

La solution précédente nécessite que la tortue se téléporte, autrement dit,
qu'il est parfois nécessaire de lever le crayon pour tracer la courbe.  En
effet, entre la destination du trait tracé par le premier appel récursif ne
correspond pas à la source du trait qui doit être tracé par le second appel
récursif.  C'est pourquoi il était nécessaire d'utiliser la méthode ` setPos()`

Dans cette leçon, vous allez donc réaliser une méthode récursive qui permet
de tracer la courbe du dragon sans lever le crayon. Pour ce faire, vous
définirez une seconde méthode récursive qui trace la courbe à l'envers.

La méthode ` dragon()` sera définie récursivement en fonction des
méthodes ` dragon()` et ` dragonInverse()` . De même, la
méthode ` dragonInverse()` sera définie récursivement en terme de ` dragon()` et de ` dragonInverse()` . Voici un bien bel
exemple de *récursivité croisée* .

Le prototype de la méthode ` dragon()` reste le même que
précédemment : 
<pre> void dragon(int ordre, double x, double y, double z, double t)</pre>
Les coordonnées (u, v) du nouveau point introduit par la méthode ` dragon()` seront : 
<pre> u = (x + z)/2 + (t - y)/2
v = (y + t)/2 - (z - x)/2</pre>

Le prototype de la méthode ` dragonInverse()` est identique : : 
<pre> void dragonInverse(int ordre, double x, double y, double z, double t)</pre>
Les coordonnées (u, v) du nouveau point introduit par la méthode ` dragonInverse()` seront : 
<pre> u = (x + z)/2 - (t - y)/2
v = (y + t)/2 + (z - x)/2</pre>

Afin de rendre, plus visible le travail de chacune des deux méthodes
récursives, le trait tracé par la méthode ` dragon()` devra être
rouge ( ` Color.red` ) et le trait tracé par la méthode ` dragonInverse` devra être bleu ( ` Color.blue` ).

Consultez les objectifs de chaque monde pour comprendre comment écrire la
fonction.

