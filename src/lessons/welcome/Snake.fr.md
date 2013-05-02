
## Monde de serpents ##
Nous allons maintenant apprendre à la buggle à explorer son monde. Sa
position de départ est en bas à gauche, et elle doit visiter toutes les
cases juqu'en haut (en coloriant le sol sur son passage).  La boucle
principale de la méthode ` run()` (que vous devez écrire)  est de
la forme : 
<pre> baisser le crayon
tant que l'on est pas à la position finale
avancer comme un serpent</pre>
Le prototype de cette méthode (sa première ligne) doit être : 
<pre> public void run()</pre>
(nous verrons plus tard ce que ce ` public` signifie).  Il faut
donc écrire deux méthodes en plus de ` run()` . L'une renvoit un
booléen et indique si l'on se trouve à une position finale, tandis que
l'autre ne renvoit pas de résultat et avance d'un pas.

On se trouve sur une position finale si et seulement si :   
  
On est face à un mur  
` On obtient la direction actuelle de la buggle avec la méthodegetDirection()` ` , et on sait si elle regarde à l'est avec le testgetDirection() == Direction.EAST` Pour la vérification elle-même, rien de magique : il faut se tourner et
regarder si on est face à un mur une fois tourné.  

Ensuite un pas de serpent se fait en avancant d'un pas si l'on est pas face
à un mur, et en montant à la ligne du dessus sinon (càd, si on regarde à
l'ouest face à un mur, il faut tourner à droite, avancer, tourner à droite).

Indication: la boucle principale de la méthode ` run()` doit
continuer tant que la fonction adéquate renvoie faux. On peut l'écrire de
deux façons: 
<pre> while (fonctionTest() == false)</pre>
ou bien 
<pre> while (! fonctionTest())</pre>
Cela fonctionne car le point d'exclamation en java indique une négation
booléenne.

À vous de jouer...

