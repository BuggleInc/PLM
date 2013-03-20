## Méthodes retournant un résultat ##
Écrire des méthodes retournant un résultat n'est pas bien plus compliqué
qu'écrire une méthode n'en renvoyant pas. Il suffit d'une part de mettre le
type de la donnée renvoyée avant le nom de la méthode, et d'autre part
d'écrire dans le corps de la méthode une instruction qui
précise ce qu'il faut renvoyer.     double pi() {return 3.14159;
    }
    boolean deuxEstIlPair() {return true;
    }

Il est possible d'avoir plusieurs instructions dans
différentes branches de . Ce qui est interdit, c'est d'avoir
une branche du code qui n'est pas terminée par un , ou
d'écrire du code après le .

En effet, si la machine arrive à la fin de la méthode sans avoir rencontré
de , elle ne peut pas savoir quelle valeur communiquer à
celui qui a appelé la méthode. De plus, le interrompt
immédiatement l'exécution de la méthode (pourquoi continuer à chercher quand
on a déjà trouvé le résultat de la méthode?). Donc, s'il y a du code après
un , c'est sans doute une erreur, et le compilateur vous
l'indique.

    }
### Objectif de cet exercice ###

Le plus simple pour écrire cette méthode est peut être d'utiliser une
variable booléenne indiquant si on a vu un baggle
jusque là. Initialement, elle contient faux.

Ensuite, on avance de 6 cases (le monde contient 7 cases, et on est déjà sur
l'une d'entre elles). Pour chaque case, si elle contient un baggle, on range
la valeur vrai dans (et on ne fait rien d'autre qu'avancer
si non).

Quand on est arrivé à la fin, on recule de 6 cases, et on retourne le
contenu de à l'appelant.

Cet exercice est un peu particulier, puisqu'il a deux mondes initiaux,
chacun ayant un objectif particulier. Votre code doit fonctionner pour
chacun d'entre eux. Remarquez que le menu déroulant de sélection du monde
(juste sous la barre de réglage de la vitesse) permet de spécifier le monde
que vous souhaitez observer.

Quand votre méthode fonctionne, passez à l'exercice
suivant.

