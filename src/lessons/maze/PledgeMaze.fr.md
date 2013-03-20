## Algorithmes de Pledge ##

Une fois de plus, vous pensiez que votre algorithme vous permettait de vous
échapper des labyrinthes, et une fois de plus, votre buggle est prise dans
un labyrinthe où votre algorithme précédent ne suffit pas. Vous pouvez
tenter de simplement appuyer sur le bouton «Run» et voir votre création
échouer. Le piège a la forme d'un «G» majuscule : la buggle entre dans le
piège, suit le bord interne. Au bout d'un moment, la direction nord est
libre et votre buggle se met donc à courir dans cette direction. Pour
retomber dans le piège...

L'algorithme de Pledge (nommé d'après Jon Pledge d'Exeter) peut résoudre ce
labyrinthe.

Cet algorithme est une version modifiée l'algorithme précédent conçu pour
éviter les obstacles. Il nécessite de choisir de manière arbitraire une
direction vers laquelle le buggle se dirigera. Quand un obstacle est
rencontré, une patte (disons la patte de gauche) est gardée le long des
obstacles tandis que les virages sont comptabilisés. Quand le buggle est
face à nouveau à la direction originale, et que la somme des virages est
égale à 0, le buggle quitte l'obstacle et continue de se déplacer dans sa
direction d'origine.

Notez que l'utilisation de la "somme des virages" à la place de la
"direction courante" permet à l'algorithme d'éviter les pièges tel que les
formes en "G" majuscule. Si l'on rentre par la gauche dans le piège, on
tourne de 360 degrés autour des murs. Un algorithme qui se contenterait
naivement de se retrouver dans la même direction qu'à l'origine rentre dans
un cycle infini puisque qu'il quite le mur le plus à droite en étant dirigé
vers la gauche, et entre à nouveau dans la section incurvée.

L'algorithme de Pledge ne quite pas le mur en bas à droite puisque la somme
des virages ne vaut pas zéro à ce moment. Il continue de suivre le mur
jusqu'à avoir complétement fait le tour, et le quite en regardant à gauche
une fois parvenu sous l'obstacle.

### Objectif de cet exercice ###

Reprenez la méthode de l'exercice
précédent. Modifiez cette méthode pour compter les virages pris par votre
buggle (+1 lorsqu'il a tourné à gauche par rapport à son origine, -1
lorsqu'il a tourné à droite). Pour comptabiliser vous aurez besoin d'ajouter
une variable de type entière à votre programme.

Écrivez une méthode indiquant
si la direction arbitraire que vous avez choisie est libre, c'est-à-dire si
vous pouvez vous déplacer dans cette direction. Notez que la démo utilise la
direction NORTH pour cela. Vous pouvez retrouver la direction courante de la
buggle en utilisant la méthode .  Vous
pouvez diriger (sans se déplacer) votre buggle dans une direction en
utilisant la méthode .  Pensez à
mémoriser (dans une variable de type ) la direction
courante de votre buggle avant de vérifier si votre buggle peut se diriger
vers sa direction de prédilection pour pouvoir restaurer l'état après coup.

Vous pouvez être amenés à modifier également le reste de votre code, mais
ces changements devraient rester limités.

