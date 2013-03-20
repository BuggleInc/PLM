## Dessiner avec méthode ##
Dans cet exercice, nous allons reproduire un dessin géométrique, que vous
pouvez voir en cliquant sur l'onglet "Objective".

Votre objectif (ici, et dans tous les programmes bien faits) est d'écrire
une méthode la plus simple possible. Pour cela, vous
veillerez à décomposer le travail à faire en sous-étape, et à faire réaliser
chaque sous-étape par une méthode particulière.

Si on observe attentivement le modèle à dessiner, on remarque qu'il est
composé de quatre formes en sorte de V de couleurs différentes, et à des
positions différentes.  Un découpage possible est d'écrire une fonction
chargée de faire un V de la couleur indiquée à partir de la position
courante. Son prototype peut être :     void makeV(Color c)

Le type de données représente naturellement une couleur
en particulier.  Votre méthode invoquera sans doute avec les arguments suivants (une couleur différente à
chaque appel) : *    Color.yellow
*    Color.red
*    Color.blue
*    Color.green

Dans la méthode , il faut utiliser la méthode , qui est prédéfinie dans la buggle, pour
changer la couleur du pinceau, ainsi que et pour lever et baisser le pinceau.

Il faudrait également que la méthode place la buggle en
position pour dessiner le prochain V directement.

À vous de jouer. La méthode ne devrait pas prendre plus
de quatre lignes...

