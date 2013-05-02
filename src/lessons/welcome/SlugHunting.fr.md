
## Chasse à la limace ##
Après toute l'agitation de la dance revolution, nous allons passer à une
activité plus calme : la chasse à la limace. Votre buggle a en effet trouvé
par hasard la trace d'une limace : une traînée baveuse verte. Si elle
parvient à la suivre jusqu'au bout, elle trouvera un baggle représentant la
grosse limace si appétissante (pour une buggle).

Pour arriver au résultat, le plus simple serait d'avoir une méthode
booléenne isFacingTrail , qui permettrait de savoir si on est face à
une case verte ou non. Bien sûr, si on est face à un mur, elle doit répondre
faux. Il faudrait de plus que cette méthode ne modifie ni l'état de la
buggle qui l'appelle, ni celui du monde. Une telle méthode est dite **sans effet de bord** .

Ensuite, le mieux serait qu'elle prenne en argument la couleur de la piste
que l'on veut suivre. Les limaces laissent des traces vertes, mais d'autres
proies laissent des traces d'autres couleurs.  En Java, il existe un *type* Color pour désigner les couleurs.  La couleur verte est
définie par ` Color.green` .

Afin de ne pas confondre la partie de la trace à suivre avec celle que votre
buggle a déjà suivie, il est conseillé à votre buggle de laisser une trace
derrière ses pas.  Pensez à utiliser la méthode ` brushDown()` pour baisser votre crayon (et ` brushUp()` pour le relever)

Un buggle peut connaître la couleur de la case sur laquelle il est
positionné en utilisant la méthode ` Color getGroundColor()` .

Enfin, n'oubliez de capturer votre proie une fois que vous l'aurez débusquée
(avec ` pickUpBaggle()` ).


### Objectif de cet exercice ###
` Complétez la méthodehunt()` ` qui sera
appelée par la méthoderun()` définissant le comportement par
défaut de votre buggle.  La méthodehunt() ` doit utiliser la méthodeisFacingTrail()`

