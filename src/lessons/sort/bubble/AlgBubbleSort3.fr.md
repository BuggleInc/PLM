
# Le tri à bulle et variantes #

## BubbleSort3 ##

Réintroduisons maintenant la petite optimisation que nous avions retiré à
l'étape précédente : Si un parcours n'a rien inversé, c'est que le tableau
est maintenant trié. Cela nous donne le pseudo-code suivant :

  
  

Le pseudo-code de l'algorithme BubbleSort3 est le suivant :


<pre> Pour tout i dans [lgr-2,0] (parcours du plus grand au plus petit)
Pour tout j dans [0, i]
Si les cases j et j+1 doivent être inverser, le faire
Si le parcours sur les j n'a rien inversé, quiter la fonction</pre>
  
  

Cet optimisation est encore plus décevante : on ne gagne que quelques
pourcents en nombre de lectures sur BubbleSort2.

