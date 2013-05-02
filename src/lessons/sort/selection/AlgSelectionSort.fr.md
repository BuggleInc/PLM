
# Tri par selection #
Cet exercice est l'occasion d'implémenter un autre algorithme très classique
: le tri par sélection.

Son principe est simplement de sélectioner pour chaque case du tableau la
plus petite valeur de la partie pas encore triée. Ainsi, pour la premiere
case, je prend la plus petite valeur du tableau. Pour la seconde, je prend
la seconde plus petite valeur, qui se trouve être la plus petite des cases
n'étant pas encore triées.

Plus généralement, pour la case N, je cherche la case M de [n;lgr] contenant
la plus petite valeur possible. Ensuite, j'inverse le contenu de la case N
et celui de la case M. 
## Variantes possibles ##
Un autre algorithme classique dont le principe repose sur la sélection des
bons éléments est le tri par tas, mais il utilise une structure de données
en tas, que nous n'avons pas encore introduite. Sachez simplement que ce tri
présente un coût en O(n log n) dans le pire des cas, ce qui en fait un
algorithme très intéressant en pratique.

