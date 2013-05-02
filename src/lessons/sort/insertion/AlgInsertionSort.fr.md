
# Algorithme par insertion et variantes #

Cet exercice vous permet d'expérimenter avec le tri par insertion et ses
variantes les plus classiques.


## InsertionSort ##

Ce tri est relativement simple à comprendre et à écrire, même s'il n'offre
pas les meilleures performances possibles.  Il s'agit d'un algorithme
quadratique (complexité asymptotique en O(n2)), mais il est plus efficace en
pratique (linéaire dans le meilleur des cas, ie quand le tableau déjà trié
et N2/4 en moyenne).

L'idée est de parcourir tous les éléments du tableau, et d'insérer chacun à
sa place dans la partie du tableau déjà trié.  Lorsque l'on regarde un
élément x du tableau, on est dans la situation suivante : tous les éléments
à gauche du tableau sont déjà triés, et il faut insérer x à sa place dans le
tableau.

  
  
<img src="lessons/sort/insertion/Insertionsort-before.png" />   
  

Une fois ceci fait, on se retrouve dans la situation suivante:

  
  
<img src="lessons/sort/insertion/Insertionsort-after.png" />   
  

Le pseudo-code de cet algorithme est donc le suivant :


<pre> Pour tout i dans [1,lgr]
stoquer la valeur de la case i dans une variable val
recopier la case i-1 dans i, si i-1 contient une valeur plus grande que val
recopier la case i-2 dans i-1, si i-2 contient une valeur plus grande que val
recopier la case i-3 dans i-2, si i-3 contient une valeur plus grande que val
recopier la case i-4 dans i-3, si i-4 contient une valeur plus grande que val
...
recopier val dans la dernière case recopiée plus haut</pre>

Bien entendu, il faut utiliser une boucle pour écrire la grosse permutation
circulaire au coeur de la boucle.  L'écrire explicitement de la sorte serait
vraiment ... contre-productif.

Si vous vous étiez toujours demandé ce que font les chercheurs en
informatique à notre époque, voici un élément de réponse: certains d'entre
eux améliorent des algorithmes fondamentaux pour permettre à d'autres de
faire des programmes efficaces...


## D'autres variantes du tri par insertion ##

TreeSort construit un arbre binaire équilibré des données pour les
trier. Cela lui permet d'être en O(nlog n) en moyenne (mais O(n^2) dans le
pire cas). Nous n'étudierons pas cet algorithme ici puisque comprendre son
fonctionnement demande de savoir ce qu'est un arbre binaire, ce qui est au
dela de nos objectifs présents.

Il existe d'autres variantes du tri par insertion, comme PatienceSorting,
qui fait des paquets des valeurs et trie ensuite chaque paquet. Cet
algorithme présente un pire cas en O(nlog n), et une complexité spatiale en
O(n).  LibrarySort (proposé en 2004) échange également un peu d'espace pour
du temps puisqu'il présente une complexité moyenne en O(n log n), mais
nécessite de stoquer des données supplémentaires.

Wikipedia (en anglais) propose une description détaillée de tous ces
algorithmes que nous n'avons pas le temps de détailler ici.

