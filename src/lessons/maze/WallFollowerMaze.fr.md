## Longer les murs ##

Cette fois-ci le labyrinthe est beaucoup plus compliqué. Le hasard ne sera
pas suffisant, il va falloir être intelligent !

Heureusement, ce labyrinthe est plus simple qu'il n'y paraît: tous les murs
sont connectés les uns aux autres. Pour sortir de ce genre de labyrinthe, il
suffit à votre buggle de longer un mur (celui à sa droite, ou celui à sa
gauche: c'est sans importance). Et, tout en gardant sa patte posée sur ce
mur, votre buggle doit avancer jusqu'à ce qu'il trouve la sortie du
labyrinthe et ce biscuit qu'il apprécie tant.

This algorithm works here because there is no island of isolated walls, so
our buggle cannot loop around for ever without encountering the baggle it
looks for.

### Objectif de cet exercice ###

Comme dit dans l'introduction, il est sans importance de décider de suivre
le mur droit ou le mur gauche. Simplement, la démo suit le mur gauche, et il
serait donc avisé d'en faire de même dans votre solution pour simplifier la
comparaison de votre solution et de la démo.

Écrivez une méthode qui fait avancer
votre buggle d'une case tout en gardant la patte sur le mur du côté
choisi. Vous devez donc vous assurer que votre buggle garde toujours la
patte
sur le mur et également qu'il ne risque pas de percuter un mur. Vous pouvez
regarder l'indice (hint) si vous êtes coincés, mais vous devriez d'abord
essayer de le faire sans l'indice.

Enfin, écrivez la méthode qui cherche le mur le plus
proche (méthode ), puis parcours le
labyrinthe (méthode ) jusqu'à trouver
la sortie et le biscuit. Enfin il ne faut pas oubliez de prendre la baggle.

Quand votre buggle a un mur à sa gauche, il faut considérer trois situations
possible, qui dépendent des murs alentours. Le tableau suivant représente
graphiquement chaque situation initiale, et où vous devez placer votre
buggle à la fin de l'étape.

Si vous faites un dans tous les cas à la fin de
votre fonction, il est possible de l'écrire en trois lignes avec une boucle .

