
## La fourmi de Langton ##

Dans cet exercice, vous aller métamorphoser votre buggle en une *fourmi de Langton* . Ces petits animaux artificiels sont très intéressants parce
qu'ils suivent des règles simples qui dépendent seulement de leur
environnement local, et qu'après une période d'un comportement d'apparence
chaotique, un schéma général *apparaît*

Les règles sont absolument triviales: pour calculer quel sera le prochain
pas, vous devez regarder la couleur actuelle du sol ( en utilisant ` getGroundColor()` ). Si c'est blanc, changez la en noir, tournez
à droite et avancez d'une case. Si la couleur du sol est le noir, changez la
en blanc, tournez à gauche et avancez d'une case.

Il est difficile de trouver des règles plus simples, n'est-ce pas ? Et bien,
allons-y et codons les maintenant. Vous avez à compléter la méthode ` step()` , qui définit le comportement de la fourmi à chaque
pas. Vous utiliserez probablement la méthode ` getGroundColor()` pour récupérer la valeur de la case sur laquelle la fourmi se trouve. Pour
comparer des couleurs, vous ne pouvez pas utiliser le signe égal (=), parce
que les couleurs ne sont pas des valeurs scalaires mais des objets. A la
place, vous aurez besoin d'écrire quelquechose comme:


<pre> Color c /* = une initialisation*/;
if (c.equals(Color.black)) {
/* c'était égal */
} else {
/* ce n'était pas égal */
}</pre>

Changer la couleur du sol n'est pas difficile, seulement un peu long : vous
avez à changer la couleur de la brosse de votre buggle, l'abaisser ( pour
marquer la case courante -- avec ` brushDown()` , et relever la
brosse (avec ` brushUp()` ) pour éviter des problèmes lorsque la
buggle va se déplacer. Vous être naturellement libre d'organiser votre code
comme vous le souhaitez, mais vous pouvez vouloir écrire une méthode ` void setGroundColor(Color c)` pour factoriser le tout.


## Plus d'informations sur la fourmi de Langton ##

Comme vous pouvez le constater avec l'exécution de cet exercice, l'intérêt
dans cet algorithme est qu'après environ 10 000 pas de comportement
relativement chaotique, la fourmi commence à suivre un schéma
régulier. L'émergence de ce schéma régulier à partir du chaos est réellement
fascinant, n'est-ce pas ?

Ce mécanisme a été inventé en 1986 par Chris Langton, et généralisé après de
plusieures façons (comme nous le verrons dans les prochains exercices). Il a
été prouvé en 2000 que la trajectoire de la fourmi peut être utilisée pour
calculer n'importe quel circuit booléen, et que la fourmi est capable de
calcul universel ( ie, n'importe quel calcul possible peut être réalisé en
utilisant la fourmi comme instrument de calcul). Encore un autre sujet de
fascination...

Consultez la page correspondante sur Wikipedia, à partir de laquelle cet
exercice est inspiré, pour d'avantages de détails.

