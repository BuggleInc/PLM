## Courbe du Dragon (1) ##

Un exemple classique de la méthode récursive est la courbe du Dragon.

La définition de cette courbe est la suivante : la courbe du Dragon d'ordre 1 est un vecteur entre deux points quelconques P
et Q, la courbe du Dragon d'ordre n est la courbe du Dragon d'ordre n-1 entre P et
R suivie de la même courbe d'ordre n-1 entre R et Q (à l'envers), où PRQ est
le triangle isocèle rectangle en R, et R est à droite du vecteur PQ.  Donc,
si P et Q sont les points de coordonnées (x, y) et (z,t), les coordonnées
(u, v) de R sont     u = (x + z)/2 + (t - y)/2
    v = (y + t)/2 - (z - x)/2

Le prototype de la méthode traçant la courbe est le suivant :     void dragon(int ordre, double x, double y, double z, double t)

Vous utiliserez la méthode pour déplacer votre
tortue aux coordonnées (x,y) et la méthode pour
tracer un trait depuis le point où la tortue est positionnée vers le point
de coordonnées (z,t)

Consultez les objectifs de chaque monde pour comprendre comment écrire la
fonction.

