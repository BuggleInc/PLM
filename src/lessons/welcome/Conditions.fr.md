
## Instructions conditionnelles ##
Les programmes faits d'une simple suite d'instructions comme celui de
l'exercice précédent sont bien ennuyeux. Ils font toujours la même chose, et
ne peuvent pas réagir aux conditions extérieurs. Une **instruction conditionnelle** permet au programme de s'adapter en disant quelque chose
comme *S'il pleut, prend un parapluie* . 
<java>

La syntaxe Java est la suivante :

</java> 
<python>

La syntaxe Python est la suivante :

</python> 
<java> 
<pre> **if (condition** **) {quoiFaire();** }</pre>
</java> 
<python> 
<pre> **ifcondition** **quoiFaire()** </pre>
</python> 
<java>

Si la condition est vraie, tout le code compris entre le { et le }
correspondant sera exécuté. Si non, il sera ignoré. Bien entendu, il est
possible d'écrire plusieurs instructions entre les deux accolades (voire un
autre test).

</java> 
<python>

Si la condition est vraie, tout le code du bloc placé après les deux points
(:)
sera exécuté. Si non, il sera ignoré. Bien entendu, il est possible d'écrire
plusieurs instructions dans le sous-bloc (voire un autre test).

</python> 
<python>

Python utilise l'indentation pour définir les blocs de code. L'indentation
standard en Python est de quatre espaces. Veuillez noter que les blocs de
code n'ont pas besoin de terminaison. Indenter commence un bloc et
désindenter le termineDans le code suivant, les instructions **whatToDo()** et **whatToDoNext()** seront executées si la condition
est vrai, ensuite l'instruction **whatToDoAnyway()** sera executée dans
tous les cas. 
<python> 
<pre> **ifcondition** **:quoiFaire()** **quoiFaireEnsuite()** **quoiFaireDansTousLesCas()** </pre>
</python> 
<python>

Les fonctions Pythons n'ont pas de début ou de fin explicite, ni d'accolade
pour indiquer où le code commence et où il se termine. Le seul délimiteur
est un double points (:) et l'indentation du code lui même.

</python> 
<python>

Exemple 2.5. Indenter la fonction buildConnectionString

</python> 
<python> 
<pre> def buildConnectionString(params):
"""Construit une connection string à partir d'un dictionnaire de paramètres.

Renvoie une chaîne de caractères."""
return ";".join(["%s=%s" % (k, v) for k, v in params.items()])</pre>
</python> 
<python>

Les blocs de code sont définis par leur indentation. Par "bloc de code", je
veux dire les fonctions, les branchements conditionnels if, les boucles for,
les boucles while, etc. Indenter commence un bloc et désindenter le
termine. Il n'y a pas de parenthèses, d'accolades, ou de mots clés. Cela
signifie qu'un espace est significatif et doit être cohérent. Dans cet
exemple, le code de la fonction ( en comptant la documentation) est indenté
avec quatre espaces. Il n'est pas nécessaire que cela soit quatre espaces,
il faut juste que cela soit cohérent. La première ligne qui n'est pas
indentée est en dehors de la fonction.

</python> 
<python>

Il est important que l'indentation de toutes les instructions d'un bloc soit
cohérente, et il n'est pas possible de couper un bloc. Les deux exemples
suivants de code sont incorrects et vont générer des erreurs.

</python> 
<python> 
<pre> **ifcondition** **:quoiFaire()** **quoiFaireEnsuite()** **quoiFaireDansTousLesCas()** </pre>
</python> 
<python> 
<pre> **ifcondition** **:quoiFaire()** **quoiFaireDansTousLesCas()** **quoiFaireEnsuite()** </pre>
</python> 
<java>

Une condition peut être une variable de type boolean . Le code entre
accolades sera exécuté si la variable vaut la valeur true (vrai),
et il sera ignoré si elle vaut false (faux).

</java> 
<python>

Une condition peut être une variable de type boolean . Le code dans
le
bloc indenté sera exécuté si la variable vaut la valeur True (vrai), et il sera ignoré si elle vaut False (faux).

</python> 

La condition peut aussi être un test arithmétique, comme x **==** 5 , qui vérifie si la valeur actuelle de x est 5,
ou bien comme **!=** (teste l'inégalité), **** (inférieur à), **** (supérieur à), **=** (inférieur ou égal à), **=** (supérieur ou égal à).

Attention au piège classique, qui consiste à tester l'égalité d'une variable
avec = au lieu de ==. Heureusement, le compilateur vous indique le plus
souvent ce problème, mais pas tout le temps. Si la variable est de type
booléen, il peut se faire prendre au piège, et il convient donc d'être
attentif...

La condition peut également être un appel à certaines méthodes
particulières, dont le résultat est un booléen. Par exemple, la méthode isFacingWall() de la buggle renvoie vrai si la buggle est face à un
mur, et faux sinon.

Enfin, il est possible de construire une condition composée de plusieurs
sous-conditions reliées par des opérations booléennes.   
  
cond1&&cond2 est vraie sicond1 **et** cond2 est vraie (sicond1 est fausse,cond2 n'est même pas
évaluée).  
cond1 || cond2 est vraie sicond1 **ou** cond2 est vraie (sicond1 est vraie,cond2 n'est même pas
évaluée).  
!cond est vraie sicond ne l'est pas.  
On peut forcer l'ordre d'évaluation en ajoutant des parenthèses. En cas de
doute, n'hésitez pas à mettre plus de parenthèses que nécessaire pour lever
les ambiguïtés sur l'ordre d'évaluation.  
  
  
cond1 and cond2 est vraie sicond1 **et** cond2 est vraie (sicond1 est fausse,cond2 n'est
même pas évaluée).  
cond1 or cond2 est vraie sicond1 **ou** cond2 est vraie (sicond1 est vraie,cond2 n'est même pas
évaluée).  
not cond est vraie sicond ne l'est pas.  
On peut forcer l'ordre d'évaluation en ajoutant des parenthèses. En cas de
doute, n'hésitez pas à mettre plus de parenthèses que nécessaire pour lever
les ambiguïtés sur l'ordre d'évaluation.  

Pour finir, il est possible de spécifier ce qu'il faut faire quand la
condition est fausse, en utilisant la syntaxe suivante : 
<java> 
<pre> **if (condition** **) {quoiFaireSiLaConditionEstVraie();** **} else {quoiFaireSinon();** }</pre>
</java> 
<python> 
<pre> **if (condition** **):quoiFaireSiLaConditionEstVraie()** **else:quoiFaireSinon()** </pre>
</python> 
<python>

N'oubliez pas les deux points (:) après le else, ils indiquent qu'un nouveau
bloc débute.

</python> 
### Objectif de cet exercice ###
Si la buggle est face à un mur (prédicatisFacingWall()

Cet exercice est un peu particulier : il faut que votre programme fonctionne
pour plusieurs buggles, chacune étant dans une situation initiale
différente. Le même code sera utilisé pour chacune d'entre elles.

Quand votre programme fonctionne, passez à l'exercice suivant.

