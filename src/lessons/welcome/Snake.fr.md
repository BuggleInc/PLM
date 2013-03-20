## Monde de serpents ##
Nous allons maintenant apprendre à la buggle à explorer son monde. Sa
position de départ est en bas à gauche, et elle doit visiter toutes les
cases juqu'en haut (en coloriant le sol sur son passage).  La boucle
principale de la méthode (que vous devez écrire)  est de
la forme :     baisser le crayon
    tant que l'on est pas à la position finale
    avancer comme un serpent
Le prototype de cette méthode (sa première ligne) doit être :     public void run()
(nous verrons plus tard ce que ce signifie).  Il faut
donc écrire deux méthodes en plus de . L'une renvoit un
booléen et indique si l'on se trouve à une position finale, tandis que
l'autre ne renvoit pas de résultat et avance d'un pas.

On se trouve sur une position finale si et seulement si : *    On est face à un mur
*    Pour la vérification elle-même, rien de magique : il faut se tourner et
regarder si on est face à un mur une fois tourné.

Ensuite un pas de serpent se fait en avancant d'un pas si l'on est pas face
à un mur, et en montant à la ligne du dessus sinon (càd, si on regarde à
l'ouest face à un mur, il faut tourner à droite, avancer, tourner à droite).

Indication: la boucle principale de la méthode doit
continuer tant que la fonction adéquate renvoie faux. On peut l'écrire de
deux façons:     while (fonctionTest() == false)
ou bien     while (! fonctionTest())
Cela fonctionne car le point d'exclamation en java indique une négation
booléenne.

À vous de jouer...

