## Courbe du Dragon (2) ##

La solution précédente nécessite que la tortue se téléporte, autrement dit,
qu'il est parfois nécessaire de lever le crayon pour tracer la courbe.  En
effet, entre la destination du trait tracé par le premier appel récursif ne
correspond pas à la source du trait qui doit être tracé par le second appel
récursif.  C'est pourquoi il était nécessaire d'utiliser la méthode

Dans cette leçon, vous allez donc réaliser une méthode récursive qui permet
de tracer la courbe du dragon sans lever le crayon. Pour ce faire, vous
définirez une seconde méthode récursive qui trace la courbe à l'envers.

La méthode sera définie récursivement en fonction des
méthodes et . De même, la
méthode sera définie récursivement en terme de et de . Voici un bien bel
exemple de .

Le prototype de la méthode reste le même que
précédemment :     void dragon(int ordre, double x, double y, double z, double t)
Les coordonnées (u, v) du nouveau point introduit par la méthode seront :     u = (x + z)/2 + (t - y)/2
    v = (y + t)/2 - (z - x)/2

Le prototype de la méthode est identique : :     void dragonInverse(int ordre, double x, double y, double z, double t)
Les coordonnées (u, v) du nouveau point introduit par la méthode seront :     u = (x + z)/2 - (t - y)/2
    v = (y + t)/2 + (z - x)/2

Afin de rendre, plus visible le travail de chacune des deux méthodes
récursives, le trait tracé par la méthode devra être
rouge ( ) et le trait tracé par la méthode devra être bleu ( ).

Consultez les objectifs de chaque monde pour comprendre comment écrire la
fonction.

