# Le tri à bulle et variantes #

Vous voici dans le monde des tris. Il vous permet d'expérimenter avec les
différents algorithmes de tri existant.  Consultez l'aide du monde
("Help->About this world") pour une aide sur les primitives utilisables par
vos algorithmes.

Dans ce premier exercice, nous allons voir les plus simples d'entre
eux. L'onglet "Source code" est composé de plusieurs sous-onglets,
correspondant aux différents algorithmes à écrire. Pour réussir l'exercice,
il faut que votre solution réalise le même nombre de lectures et écritures
que la solution codée dans l'objectif.  Attention donc à suivre le
pseudo-code de chaque algorithme à la lettre!

## BubbleSort ##

Le tri à bulles ou tri par propagation consiste à faire remonter
progressivement les plus petits éléments d'une liste, comme les bulles d'air
remontent à la surface d'un liquide. L'algorithme parcourt la liste, et
compare les couples d'éléments successifs. Lorsque deux éléments successifs
ne sont pas dans l'ordre croissant, ils sont échangés. Après chaque parcours
complet de la liste, l'algorithme recommence l'opération. Lorsqu'aucun
échange n'a lieu pendant un parcours, cela signifie que la liste est triée :
l'algorithme peut s'arrêter.  La simplicité de cet algorithme justifie son
étude, mais ses piètres performances (O(n2) en moyenne) font qu'il n'est
quasiment jamais utilisé en pratique.

Le pseudo-code de l'algorithme du tri à bulles est donc le suivant :

    faire:
    Pour tout i dans [0,lgr-2]
    Si les cases i et i+1 doivent être inverser, le faire
    tant qu'on a inverser des choses lors du dernier parcours
## BubbleSort2 ##

En étudiant le comportement du tri à bulle, on peut voir une première
optimisation facile à effectuer: Après un parcours, le dernier élément du
tableau est forcément le plus grand d'entre tous car le parcours l'a fait
remonté comme une bulle à sa position. Plus généralement, après N parcours,
on sait que les N derniers éléments du tableau sont déjà triés. En
conclusion, il n'est pas utile de les recomparer sur les parcours
suivants. Dans un premier temps, nous ferons autant de parcours qu'il y a
d'éléments dans le tableau.

Le pseudo-code de l'algorithme BubbleSort2 est le suivant :

    Pour tout i dans [lgr-2,0] (parcours du plus grand au plus petit)
    Pour tout j dans [0, i]
    Si les cases j et j+1 doivent être inverser, le faire

Lorsqu'on exécute cet algorithme, il peut être un peu décevant de constater
qu'il s'exécute à la même vitesse que la version de base de
BubbleSort. C'est un effet graphique seulement puisque seules les
changements de valeurs dans le tableau sont représentées. Comme cette
variante consiste à éviter des comparaisons inutiles, elle effectue très
exactement le même nombre d'échanges que la version de base. Il est donc
normal que notre interface graphique la représente à la même vitesse que la
version de base. Mais les statistiques sur le nombre de lectures montrent
bien que l'on a économisé plus d'un quart du nombre de lectures, ce qui
n'est pas si mal.

D'un point de vue complexité algorithmique, cela ne change rien: cette
variante est toujours en O(n2) en moyenne (on ne gagne que sur la
constante).

## BubbleSort3 ##

Réintroduisons maintenant la petite optimisation que nous avions retiré à
l'étape précédente : Si un parcours n'a rien inversé, c'est que le tableau
est maintenant trié. Cela nous donne le pseudo-code suivant :

Le pseudo-code de l'algorithme BubbleSort3 est le suivant :

    Pour tout i dans [lgr-2,0] (parcours du plus grand au plus petit)
    Pour tout j dans [0, i]
    Si les cases j et j+1 doivent être inverser, le faire
    Si le parcours sur les j n'a rien inversé, quiter la fonction

Cet optimisation est encore plus décevante : on ne gagne que quelques
pourcents en nombre de lectures sur BubbleSort2.

## CocktailSort ##

Pour améliorer encore notre algorithme, il faut regarder un peu plus en
détail son comportement. On peut constater que les grands éléments sont très
rapidement mis en place tandis que les petits éléments se déplacent très
lentement vers leur destination. On parle classiquement de lièvres et de
tortues pour désigner respectivement les grandes valeurs et les petites
valeurs.

Pour permettre aux tortues d'aller plus vite, le tri cocktail parcours
alternativement le tableau de gauche à droite et de droite à gauche. Voici
son pseudo-code:

    Faire
    Pour tout i dans [0,lgr-2], faire:
    si i et i+1 doivent être échangées, le faire
    Pour tout i dans [lgr-2,0] en descendant, faire:
    si i et i+1 doivent être échangées, le faire
    tant qu'au moins l'un des parcours a inversé un élément

On constate que le tri cocktail fait très exactement le même nombre
d'échanges que le tri à bulle, mais qu'il améliore les choses sur le nombre
de lectures. Il reste cependant moins bon que BubbleSort2 en la matière.

## CocktailSort2 ##

Nous allons appliquer la même optimisation que BubbleSort2 à
CocktailSort. Il nous faut nous souvenir des bornes du tableau pas encore
trié, et le parcourir alternativement de droite à gauche et de gauche à
droite:

    debut=0; fin=lgr-2
    Faire
    Pour tout i dans [debut,fin], faire:
    si i et i+1 doivent être échangées, le faire
    fin--
    Pour tout i dans [fin, debut] en descendant, faire:
    si i et i+1 doivent être échangées, le faire
    debut++
    tant qu'au moins l'un des parcours a inversé un élément
## CocktailSort3 ##

Même si la complexité algorithmique de CocktailSort2 est la même que le tri
à bulle, il semble bien mieux s'en tirer en pratique. On peut encore
l'améliorer un tout petit peu en arretant tout si le parcours dans l'ordre
croissant n'a rien inversé, sans faire le parcours dans l'ordre
descroissant. De même, on peut quiter si le parcours croissant a inversé des
choses, mais pas le parcours décroissant.

## CombSort ##

Nous avons vu que le tri cocktail permet d'améliorer un peu les choses pour
les tortues (ie, les petites valeurs près de la fin du tableau), mais on
peut faire mieux. CombSort revient à leur faire prendre un racourci: Au lieu
de comparer les valeurs adjacentes, on compare des valeurs séparés par un
écart plus grand que 1. Ainsi, les tortues vont se déplacer de cases à chaque parcours. Bien entendu, il faut appliquer l'algorithme avec
des écarts de plus en plus petit et terminer avec pour
s'assurer que le tableau est entièrement trié à la fin. Choisir comment
réduire l'écart entre les parcours est une question difficile (voir
wikipédia en anglais sur le thème), mais en pratique, le diviser par 1.3 à
chaque fois amène de bons résultats. Voici le pseudo-code correspondant :

    ecart = lgr;
    faire
    si ecart>1 alors
    ecart = ecart / 1.3
    i = O
    tant que i+ecartlgr faire:
    si i et i+ecart doivent être inversés, le faire
    ajouter ecart à i
    tant que l'écart est plus grand que 1 ou que le dernier parcours a inversé au moins un élément

Cet algorithme a été inventé par Wlodek Dobosiewicz en 1980 et redécouvert
et popularisé par Stephen Lacey et Richard Box, qui l'ont décrit dans le
magazine Byte d'avril 1991.

## CombSort11 ##

Les auteurs de l'algorithme ont constaté que l'on améliore les performances
en s'assurant que les dernières valeurs de l'écart sont (11, 8, 6, 4, 3, 2,
1) plutôt que (9, 6, 4, 3, 2, 1) ou (10, 7, 5, 3, 2, 1). Reprenez donc le
code de CombSort, et indiquez juste après la mise à jour de l'écart que si
la valeur est 9 ou 10, il faut utiliser 11 à la place.

## GnomeSort ##

Le tri du gnome s'apparente au tri par insertion, à ceci près que les
éléments sont déplacés par une série d'échange comme dans le tri à bulle. Le
nom vient du comportement supposé des gnomes des jardins quand ils rangent
une série de pots de fleurs. Voici la description de l'algorithme par son
auteur:

L'algorithme de tri Gnome repose sur la manière habituelle de travailler des
Nains de Jardin Hollandais Garden(Du.: tuinkabouter). Voici comment les
nains de jardin trient une ligne de pots de fleurs.Le nain regarde le pot de
fleur qui est à côté de lui et il regarde également le pot de fleurs
précédent; si ces deux pots sont alignés dans le bon ordre, le nain se
déplace en avant d'un pot de fleurs, sinon il les échange et se déplace en
arrière d'un pot de fleur. Condition d'arrêt : si il n'y a pas de pot de
fleurs précédent, le nain avance ; si il n'y a pas de pot suivant alors il a
terminé sa tâche.

