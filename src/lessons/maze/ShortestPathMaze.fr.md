
## Algorithme basique de rechercher du plus court chemin ##

Pour conclure avec cette leçon d'introduction aux algorithmes de sortie de
labyrinthe, nous allons étudier un autre moyen de trouver la sortie. Le
buggle de cette leçon est spécial : c'est un jedi. Il eut ressentir la
Force. Cela signifie qu'il peut ressentir son environnement.

En utilisant la méthode ` BuggleWorld getMyWorld()` il peut
récupérerdes informations à propos du monde où il vit.

Un objet ` BuggleWorld` est un objet Java sur lequel vous pouvez
effectuer les opérations suivantes :   
  
` int getHeight()` pour connaitre la hauteur du monde.  
` int getWidth()` pour connaitre la largeur du monde  
` BuggleWorldCell getCell(int x, int y)` ` pour récupérer l'objetBuggleWorldCell` localisé à une position donnée dans le monde.  

Un objet ` BuggleWorldCell` est un objet Java qui représente une
case du monde. Sur un objet de ce type, il est possible d'appeler les
méthodes suivantes :   
  
` boolean hasContent()` pour savoir si quelquechose est écrit sur
le sol de cette case.  
` void setContentFromInt(int v)` pour écrire une valeur entière
sur le sol de cette case.  
` int getContentAsInt()` pour récupérer la valeur entière qui est
sur le sol de cette case.  
` void emptyContent()` pour nettoyer le sol de cette case.  
` boolean hasTopWall()` pour savoir si un mur a été construit sur
le côté supérieur de la case.  
` boolean hasLeftWall()` pour savoir si un mur a été construit sur
le côté gauche de la case.  
` boolean hasBaggle()` pour savoir si un baggle est présent sur la
case.  

Il est bon de noter qu'il n'est pas possible de construire un mur sur la
côté inférieur ou droit d'une case.Néanmoins, quand de tels murs existents,
cela signifie qu'il a été construit sur une case voisine -- comme mur
supérieur ( respectivement gauche ) sur la case qui est située en dessous (
respectivement sur la droite ) de la case courante.


### Objectif de cet exercice ###

[]() Ecrivez une méthode ` evaluatePaths()` qui
implémente une version basique de l'algorithme de recherche du plus court
chemin. Cet algorithme écrira sur chaque case du monde ( ou au moins sur
celles qui sont nécessaires ) la distance qu'il y a de la case à la sortie
du labyrinthe. Pour parvenir à cet objectif, votre algorithme devra trouver la sortie du
labyrinthe sur la carte. Ensuite, pour chaque case à côté de la case où se
situe la sortie, il devra marquer cette case avec une valeur entière de 1 (
indiquant la distance ). Ensuite, il faudra itérer ce processus pour marquer
les cases qui sont à une distance de 2 et jusqu'à marquer la case où se
situe le buggle.

Ecrivez une méthode ` followShortestPath()` qui permettra au
buggle jedi de suivre le plus court chemin. Basiquement, le buggle a
seulement à suivre la case ayant la plus petite distance à la sortie. Vous
pouvez utiliser la méthode ` void setDirection(Direction d)` pour
faire regarder votre buggle dans une direction spécifique comme ` Direction.NORTH` ou ` Direction.EAST` .

  
  
Utilises la Force Luke !

