# BuggleWorld #
Ce monde a été inventé par Lyn Turbak, du Wellesley College. Il est peuplé
de Buggles, petites bêtes qui comprennent des ordres simples, et offre de
nombreuses possibilités d'interaction avec le monde: prendre ou poser des
objets, colorier le sol, se cogner à des murs, etc. ## Méthodes comprises par les buggles ##
*Bouger* *Reculer* *Changer la position* *Informations sur la buggle* *Changer la couleur* *Chercher un mur derriere* *Changer la direction* *À propos de la brosse* *Obtenir la position de la brosse* *Obtenir la couleur de la brosse* *Interagir avec le monde* *Obtenir la couleur du sol* *Poser un baggle* *Effacer le message* ## Note sur les exceptions ##
Regular buggles throw a BuggleWallException exception if you ask them to
traverse a wall.  They throw a NoBaggleUnderBuggleException exception if you
ask them to pickup a baggle from an empty cell, or a
AlreadyHaveBaggleException exception if they already carry a baggle.  Trying
to drop a baggle on a cell already containing one throws an
AlreadyHaveBaggleException exception.

Les SimpleBuggles (ie, celles utilisées dans les premiers exercices) affiche
un message d'erreur sans que vous ayez à vous soucier de ce qu'est une
exception.

