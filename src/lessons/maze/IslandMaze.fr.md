
## Perdu dans les îles ##

Vous pensiez que votre algorithme était suffisant pour échapper de tous les
labyrinthes ? Et bien, pas tous les labyrinthes...

L' algorithme du *Wall Follower* que nous avons utilisé jusqu'à présent
fonctionne seulement si l'entrée et la sortie sont placées à côté de murs
connectés à un mur externe. Mais si le buggle commence au milieu du
labyrinthe, il peut exister des pans de mur déconnectés du mur externe.

C'est pourquoi notre stratégie précédente laissera notre buggle tourner en
rond pour toujours. En effet, le labyrinthe dont vous devez vous échapper
maintenant contient des îles, et le buggle ne commence pas sur un des murs
externes. Vous pouvez essayer si vous voulez : appuyez sur le bouton 'run'
et admirez votre solution précédente échouer lamentablement

Cette méthode de suivre un mur est toujours efficace est permet d'échapper
de manière assez efficace à certaines parties du labyrinthe, on ne va donc
pas la supprimer entièrement. A la place, nous allons cesser de suivre le
mur sous certaines conditions. Notez que le baggle repose près d'un mur
externe du labyrinthe. Donc nous voulons atteindre un mur externe et ensuite
le suivre. Nous avons par exemple de rechercher le mur nord avant de le
suivre jusqu'au baggle.

Pour trouver le mur nord, vous avez tout simplement à foncez vers le nord
tant que c'est possible, et quand vous faites face à un obstacle, vous
l'évitez ( en utilisant la méthode précédente ).

  
  

<pre> ` Notre nouvelle méthode run() va consister en deux états: notre buggle va
alterner entre le mode "north runner" et le mode "left follower".
Vous commencerz dans le mode "north runner", et vous passerez au mode "left
follower" quand il y aura un mur au nord ( n'oubliez pas de vous assurez
d'avoir un mur à votre gauche avant de changer de passer au mode "left
follower").
Vous repasserez au mode "north runner" dès que votre buggle fera face au
nord et qu'elle n'est pas face à un mur.
La manière la plus simple d'écrire une telle machine à état est quelque
chose du typeint state=0;
switch (state) {
case 0:
...
state = 1;
break;
case 1:
...
state = 0;
break;
}` </pre>
  
  

N'oubliez pas de faire ramasser le baggle par votre buggle à la fin de votre
code.

Vous êtes prêts. Tout ceci devrait suffire pour vous permettre de trouver
comment sortir de ce labyrinthe, mais si ce n'est pas le cas, vous pouvez
toujours demander l'astuce. Mais vous n'avez plus besoin d'aide, n'est-ce
pas ?

