# Tricots, tableaux et modulos #
Cet exercice ressemble au précédent : il faut reproduire le motif de
couleurs de la première colonne dans les colonnes suivantes.

La première différence est que le monde est entouré d'un mur : il faut donc
modifier légèrement les parcours pour s'assurer que la buggle ne se cogne
pas sur les bords. Le plus simple est de traiter la première case de la
colonne en dehors de la boucle (avant la boucle), et faire
seulement pas dans la boucle.

La seconde différence est que le décalage à effectuer entre les colonnes
n'est pas fixe, mais écrit sur la première case de chaque colonne. Pour
obtenir l'information sous forme d'un entier, on peut utiliser:     int offset = Integer.parseInt(readMessage())
lit l'indication au sol sous forme d'une chaine
de caractères, tandis que transforme une
chaine de caractères en entiers en la .

Ensuite, pour trouver la bonne couleur à utiliser, le plus simple est
d'utiliser l'opérateur (modulo). Par exemple, permet de trouver la ieme case d'un tableau de
taille quand on applique un décalage de cases.

À vous de jouer.

