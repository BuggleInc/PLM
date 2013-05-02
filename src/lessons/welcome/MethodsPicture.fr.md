
## Dessiner avec méthode ##
Dans cet exercice, nous allons reproduire un dessin géométrique, que vous
pouvez voir en cliquant sur l'onglet "Objective".

Votre objectif (ici, et dans tous les programmes bien faits) est d'écrire
une méthode ` run()` la plus simple possible. Pour cela, vous
veillerez à décomposer le travail à faire en sous-étape, et à faire réaliser
chaque sous-étape par une méthode particulière.

Si on observe attentivement le modèle à dessiner, on remarque qu'il est
composé de quatre formes en sorte de V de couleurs différentes, et à des
positions différentes.  Un découpage possible est d'écrire une fonction
chargée de faire un V de la couleur indiquée à partir de la position
courante. Son prototype peut être : 
<pre> void makeV(Color c)</pre>

Le type de données ` Color` représente naturellement une couleur
en particulier.  Votre méthode ` run()` invoquera sans doute ` makeV` avec les arguments suivants (une couleur différente à
chaque appel) :   
  
Color.yellow  
Color.red  
Color.blue  
Color.green  

Dans la méthode ` makeV()` , il faut utiliser la méthode ` setBrushColor()` , qui est prédéfinie dans la buggle, pour
changer la couleur du pinceau, ainsi que ` brushUp()` et ` brushDown()` pour lever et baisser le pinceau.

Il faudrait également que la méthode ` makeV()` place la buggle en
position pour dessiner le prochain V directement.

À vous de jouer. La méthode ` run()` ne devrait pas prendre plus
de quatre lignes...

