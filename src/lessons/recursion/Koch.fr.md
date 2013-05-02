
## Flocons de neige ##
Nous allons maintenant dessiner des flocons de neige en utilisant la
fractale de Koch.  Une fractale est un dessin géométrique dont le motif se
reproduit à n'importe quelle échelle.

La forme générale est un triangle dont chaque coté est donné par une série
d'appels récursifs.  La forme générale est obtenue en enchainant trois cotés
de la façon suivante. 
<pre> void snowFlake (int levels, double length) {
snowSide(levels, length);
turnRight(120);
snowSide(levels, length);
turnRight(120);
snowSide(levels, length);
turnRight(120);
}</pre>

Observez le dessin dans chaque monde objectif pour comprendre la logique de
ce motif afin de la reproduire.  Vous devez écrire la fonction snowSide, qui
est récursive.

