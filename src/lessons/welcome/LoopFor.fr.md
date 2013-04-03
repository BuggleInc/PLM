##  ##

Warning, this lesson is currently written in Java only. Adapt it yourself

Les boucles sont bien adaptées aux situations où l'on veut
réaliser une action tant qu'une condition est réalisée, mais elles sont
moins pratiques pour réaliser une action un nombre prédéterminé de fois. Par
exemple, lorsque nous voulions reculer de dans l'exercice
précédent, il fallait créer une nouvelle variable, l'initialiser, et
demander à reculer tant que la nouvelle variable n'était pas égale à , en incrémentant cette variable à la main à la fin du
corps de la boucle.

Dans ce genre de cas, les boucles de type sont plus
pratique. Leur syntaxe est la suivante : *for (initialisation* *;condition* *;incrémentation* *) {action*     ();
    }
Ce code est parfaitement équivalent à celui-ci : *initialisation* *; while (condition* *) {action* *();incrémentation*     ;
    }

Dans ce genre de cas, les boucles de type sont plus
pratique. Leur syntaxe est la suivante : *for (initialisation* *;condition* *;incrémentation* *) {action*     ();
    }
Ce code est parfaitement équivalent à celui-ci : *initialisation* *; while (condition* *) {action* *();incrémentation*     ;
    }

Par exemple, les deux codes suivants sont équivalents. Avouez que la seconde
forme est plus lisible, non ?     int dejaFait = 0;
    while (dejaFaitnbPas) {backward();dejaFait++;
    }
    for (int dejaFait = 0; dejaFaitnbPas; dejaFait++) {backward();
    }

On peut imaginer des utilisations bien plus avancées des boucles car toute instruction valide peut être utilisée comme
initialisation, condition et incrémentation. L'exemple suivant est un peu
extrême, puisqu'elle calcule le PGCD de deux nombres sans avoir de corps de
boucle du tout, ni d'initialisation (tout est fait dans la condition et
l'incrémentation).     

Si vous ne comprenez pas tous les détails de cet exemple, pas de panique,
c'est assez logique puisqu'il utilise des points de syntaxe que nous n'avons
pas encore vu.

### Objectif de cet exercice ###

Une fois ceci fait, passez à l'exercice suivant.

