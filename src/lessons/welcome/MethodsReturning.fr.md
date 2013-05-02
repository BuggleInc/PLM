
## Méthodes retournant un résultat ##
Écrire des méthodes retournant un résultat n'est pas bien plus compliqué
qu'écrire une méthode n'en renvoyant pas. Il suffit d'une part de mettre le
type de la donnée renvoyée avant le nom de la méthode, et d'autre part
d'écrire dans le corps de la méthode une instruction ` return` qui
précise ce qu'il faut renvoyer. 
<pre> double pi() {return 3.14159;
}
boolean deuxEstIlPair() {return true;
}</pre>

Il est possible d'avoir plusieurs instructions ` return` dans
différentes branches de ` if` . Ce qui est interdit, c'est d'avoir
une branche du code qui n'est pas terminée par un ` return` , ou
d'écrire du code après le ` return` .

En effet, si la machine arrive à la fin de la méthode sans avoir rencontré
de return , elle ne peut pas savoir quelle valeur communiquer à
celui qui a appelé la méthode. De plus, le return interrompt
immédiatement l'exécution de la méthode (pourquoi continuer à chercher quand
on a déjà trouvé le résultat de la méthode?). Donc, s'il y a du code après
un return , c'est sans doute une erreur, et le compilateur vous
l'indique.


<pre> 
<comment> ( boolean negation(boolean cond) {if (cond == true) {return true;/* interdit d'écrire du code ici */) </comment> 
<comment> ( } else {return false;/* ici aussi */) </comment> 
<comment> ( }/* même ici */) </comment> }</pre>

### Objectif de cet exercice ###
` Vous allez encore une fois écrire une méthode qui sera
utilisée par la buggle. Son nom doit êtrehaveBaggle`

Le plus simple pour écrire cette méthode est peut être d'utiliser une
variable booléenne ` , et elle
doit renvoyer un booléen indiquant si la colonne face à la buggle contient
un baggle ou non. Votre buggle va s'en servir pour chercher la première
colonne contenant un baggle et s'y arrêter.vuBaggle` indiquant si on a vu un baggle
jusque là. Initialement, elle contient faux.

Ensuite, on avance de 6 cases (le monde contient 7 cases, et on est déjà sur
l'une d'entre elles). Pour chaque case, si elle contient un baggle, on range
la valeur vrai dans vuBaggle (et on ne fait rien d'autre qu'avancer
si non).

Quand on est arrivé à la fin, on recule de 6 cases, et on retourne le
contenu de vuBaggle à l'appelant.

Cet exercice est un peu particulier, puisqu'il a deux mondes initiaux,
chacun ayant un objectif particulier. Votre code doit fonctionner pour
chacun d'entre eux. Remarquez que le menu déroulant de sélection du monde
(juste sous la barre de réglage de la vitesse) permet de spécifier le monde
que vous souhaitez observer.

Quand votre méthode haveBaggle fonctionne, passez à l'exercice
suivant.

