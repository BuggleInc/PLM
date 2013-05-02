
## Turmites ##

Cette exercice explore une nouvelle façon d'étendre le concept de la fourmi
de Langton. Maintenant, le comportement de la fourmi ne dépend plus
seulement de la couleur du sol, mais aussi de son état interne ( représenté
par une valeur entière ). L'idée de changer la fourmi en un automate découle
naturellement du concept de la machine de Turing. Ceci explique le nom de
ces nouveaux animaux, qui est un mélange de *Turing* et de *Termite* ( si vous ne savez pas ce qu'est une machine de Turing, vous
devriez vous jeter sur Wikipedia parce qu'il est tout simplement impossible
d'être un vrai informaticien sans le savoir).

Encore une fois, vous avez seulement à écrire la méthode ` step()` , dont le but est de réaliser un pas de turmite.
Encore une fois, vous devez tout d'abord trouver la rang de la couleur de la
case actuelle dans la séquence de couleur.
Le tableau ` colors` n'est pas passé en tant que paramètre mais
est déclaré comme une variable globale. Cela ne devrait rien changer pour
nous pouisque ce tableau n'a jamais été modifié pendant l'exécution. Cette
astuce pour passer des paramètres constants n'est pas très propre, mais cela
devrait améliorer un peu les performances.

La deuxième étape est d'utiliser la donnée ` rule` selon la
couleur actuelle et l'état courant. ` rule` contient en fait trois informations dans chaque situation
: la couleur à mettre, le mouvement à effectuer, et la valeur du prochain
état. Par exemple, rule[1][0] contient l'information à utiliser quand ` state==1` et ` color==0` . En d'autres mots, vous
pouvez récupérer l'information relative à votre situation actuelle en
utilisant ` rule[etatCourant][couleurActuelle]`

Chaque ensemble d'informations contient trois valeurs.
La première est le rang de la couleur à mettre sur le sol.
La deuxième est le mouvement à effectuer, avec la notation suivante :
0=stop, 1=pas de virage, 2=droite, 4=demi-tour, 8=gauche. Veuillez noter que
si l'instruction est stop, vous ne devez même pas avancer sur cette étape (
mais vous ne devez pas arrêter votre programme pour autant : les prochains
pas peuvent faire quelquechose d'autre). Finalement, le troisième entier est
la valeur du prochain ` state` à avoir après cette itération.

Puisque ces notations arbitraires sont parfois difficiles à se souvenir, le
code fourni définit un ensemble de constantes que vous pouvez utiliser à la
place des valeurs numériques. Leurs noms sont LEFT, RIGHT, etc.
Le modifieur ` final static` avant leur type est la façon
d'indiquer les variables comme constantes en Java ( nous nous excusons si la
notation semble complexe ).
Utiliser de telles constantes rend le code beaucoup plus simple à lire,
puisque 
<pre> if (rule[state][currentColor][NEXT_MOVE] == LEFT) {
turnLeft();
}</pre>
est beaucoup plus simple à lire que 
<pre> if (rule[x][y][1] == 2) {
turnLeft();
}</pre>

Vous devriez maintenant avoir assez d'informations pour réussir.


## Notes bibliographiques ##

D'après Wikipedia, les turmites ont été inventés indépendamment à la fin des
années 80. Il a été démontré que les turmites en général sont exactement
équivalent en terme de puissance à des machines de Turing à une dimension
avec un ruban infini, et peut donc simuler les autres. Cela signifie
qu'absolument tous les programmes auxquels vous pouvez penser peuvent
théoriquement être calculé sur cet outil.

