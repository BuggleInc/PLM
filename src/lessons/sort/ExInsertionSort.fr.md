# Algorithme par insertion et variantes #
Cet exercice vous permet d'expérimenter avec le tri par insertion et ses
variantes les plus classiques. ## InsertionSort ##
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

Une fois ceci fait, on se retrouve dans la situation suivante:

Le pseudo-code de cet algorithme est donc le suivant :     Pour tout i dans [1,lgr]
    stoquer la valeur de la case i dans une variable val
    recopier la case i-1 dans i, si i-1 contient une valeur plus grande que val
    recopier la case i-2 dans i-1, si i-2 contient une valeur plus grande que val
    recopier la case i-3 dans i-2, si i-3 contient une valeur plus grande que val
    recopier la case i-4 dans i-3, si i-4 contient une valeur plus grande que val
    ...
    recopier val dans la dernière case recopiée plus haut
Bien entendu, il faut utiliser une boucle pour écrire la grosse permutation
circulaire au coeur de la boucle.  L'écrire explicitement de la sorte serait
vraiment ... contre-productif. ## ShellSort ##
Cet algorithme porte le nom de son auteur, Donald Shell, qui l'a publié en
1959. Son principe peut être vu comme une application de l'idée du CombSort
(faire prendre des raccourcis aux éléments ayant beaucoup de chemin à faire)
au tri par insertion (le CombSort est une variante du tri à bulle).  Au lieu
de comparer les valeurs ajacentes lors du tri par insertion, on compare des
valeurs séparées par un (écartement) plus grand. Plus le gap est
grand et plus les éléments sont déplacés rapidement vers leur position
finale, mais aussi plus le déplacement est imprécis. Il faut donc appliquer
l'algorithme avec une séquence de gaps décroissante vers 1. Ainsi, à la
dernière étape quand le gap vaut 1, on applique l'algorithme de tri par
insertion de base, mais sur un tableau déjà presque trié par les étapes
précédentes.

Donald Shell propose d'utiliser comme première valeur du
gap, puis de le diviser par deux à chaque étape.  Le pseudo-code est donc le
suivant:     gap=lgr/2
    tant que gap>0:
    appliquer l'algorithme de tri par insertion en comparant i-gap et i, puis i-2gap et i-gap, puis i-3gap et i-2gap, etc.

Comme dans le cas du CombSort, la séquence des valeurs prises par le gap se
révèle être d'une importance capitale pour les performances du tri de
Shell. Il existe des cas pathologiques qui font que la séquence que nous
avons utilisée ici présente une complexité en O(n^2) dans le pire des
cas. D'autres séquences ont été proposé: la séquence des incréments de
Hibbard (2^k − 1) permet une complexité dans le pire des cas de O(n^(3/2)),
les incréments de Pratt (2^i*3^j) permettent un pire cas en O(n
log(n)log(n)).  Ces résultats font du tri de Shell un candidat tout à fait
valide pour des instances de tableau de quelques centaines de milliers
d'éléments quand il est correctement implémenté.

Dans notre cas, les instances de tableaux que nous utilisons sont trop
petites pour que ces optimisations présentent un réel avantage. Si on
voulait le faire, il faudrait prendre en valeur initiale du gap la plus
grande valeur de la suite utilisée, puis prendre les valeurs successives en
descendant ensuite.

De façon intéressante, déterminer la meilleure séquence de gap pour le shell
sort s'avère être un problème de recherche actuel en informatique. Par
exemple, un article publié en 2001 propose la suite suivante, qui semble
optimale en pratique pour des tailles de tableau allant jusqu'à 10^5: {1, 4,
10, 23, 57, 132, 301, 701, 1750} (Marcin Ciura, Best Increments for the
Average Case of Shellsort, 13th International Symposium on Fundamentals of
Computation Theory, LNCS 2001; Vol. 2138).

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

