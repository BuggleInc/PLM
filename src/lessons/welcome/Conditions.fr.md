## Instructions conditionnelles ##
Les programmes faits d'une simple suite d'instructions comme celui de
l'exercice précédent sont bien ennuyeux. Ils font toujours la même chose, et
ne peuvent pas réagir aux conditions extérieurs. Une *instruction conditionnelle* permet au programme de s'adapter en disant quelque chose
comme .

La syntaxe Java est la suivante :

La syntaxe Python est la suivante :

*if (condition* *) {quoiFaire();*     }
*ifcondition* *quoiFaire()*     

Si la condition est vraie, tout le code compris entre le { et le }
correspondant sera exécuté. Si non, il sera ignoré. Bien entendu, il est
possible d'écrire plusieurs instructions entre les deux accolades (voire un
autre test).

Si la condition est vraie, tout le code du bloc placé après les deux points
(:)
sera exécuté. Si non, il sera ignoré. Bien entendu, il est possible d'écrire
plusieurs instructions dans le sous-bloc (voire un autre test).

Python uses indentation to define code blocks. The standard Python
indentation is 4 spaces. Notice that code blocks do not need any
termination. Indenting starts a block and unindenting ends it. In the
following code the instructions *whatToDo()* and *whatToDoNext()* will be exectuded if the condition is true, then the instruction *whatToDoAnyway()* will be executed anyway. *ifcondition* *:quoiFaire()* *quoiFaireEnsuite()* *quoiFaireDansTousLesCas()*     

Python functions have no explicit begin or end, and no curly braces to mark
where the function code starts and stops. The only delimiter is a colon (:)
and the indentation of the code itself.

Example 2.5. Indenting the buildConnectionString Function

    def buildConnectionString(params):
    """Build a connection string from a dictionary of parameters.
    
    Returns string."""
    return ";".join(["%s=%s" % (k, v) for k, v in params.items()])

Code blocks are defined by their indentation. By "code block", I mean
functions, if statements, for loops, while loops, and so forth. Indenting
starts a block and unindenting ends it. There are no explicit braces,
brackets, or keywords. This means that whitespace is significant, and must
be consistent. In this example, the function code (including the doc string)
is indented four spaces.  It doesn't need to be four spaces, it just needs
to be consistent. The first line that is not indented is outside the
function.

It is important that the indentations of all the instructions of a block are
consistent, and it is not possible to cut a block. The two following codes
are incorrect and will raise errors.

*ifcondition* *:quoiFaire()* *quoiFaireEnsuite()* *quoiFaireDansTousLesCas()*     
*ifcondition* *:quoiFaire()* *quoiFaireDansTousLesCas()* *quoiFaireEnsuite()*     

Une condition peut être une variable de type . Le code entre
accolade sera exécuté si la variable vaut la valeur (vrai), et
il sera ignoré si elle vaut (faux).

Une condition peut être une variable de type . Le code dans
le
bloc indenté sera exécuté si la variable vaut la valeur (vrai), et il sera ignoré si elle vaut (faux).

La condition peut aussi être un test arithmétique, comme *==* , qui vérifie si la valeur actuelle de est 5,
ou bien comme *!=* (teste l'inégalité), ** (inférieur à), ** (supérieur à), *=* (inférieur ou égal à), *=* (supérieur ou égal à).

Attention au piège classique, qui consiste à tester l'égalité d'une variable
avec = au lieu de ==. Heureusement, le compilateur vous indique le plus
souvent ce problème, mais pas tout le temps. Si la variable est de type
booléen, il peut se faire prendre au piège, et il convient donc d'être
attentif...

La condition peut également être un appel à certaines méthodes
particulières, dont le résultat est un booléen. Par exemple, la méthode de la buggle renvoie vrai si la buggle est face à un
mur, et faux sinon.

Enfin, il est possible de construire une condition composée de plusieurs
sous-conditions reliées par des opérations booléennes. *et* *    n'est même pas
évaluée).
*ou* *    n'est même pas
évaluée).
*    ne l'est pas.
*    On peut forcer l'ordre d'évaluation en ajoutant des parenthèses. En cas de
doute, n'hésitez pas à mettre plus de parenthèses que nécessaire pour lever
les ambiguïtés sur l'ordre d'évaluation.
*et* *    n'est
même pas évaluée).
*ou* *    n'est même pas
évaluée).
*    ne l'est pas.
*    On peut forcer l'ordre d'évaluation en ajoutant des parenthèses. En cas de
doute, n'hésitez pas à mettre plus de parenthèses que nécessaire pour lever
les ambiguïtés sur l'ordre d'évaluation.

Pour finir, il est possible de spécifier ce qu'il faut faire quand la
condition est fausse, en utilisant la syntaxe suivante : *if (condition* *) {quoiFaireSiLaConditionEstVraie();* *} else {quoiFaireSinon();*     }
*if (condition* *):quoiFaireSiLaConditionEstVraie()* *else:quoiFaireSinon()*     

N'oubliez pas les deux points (:) après le else, ils indiquent qu'un nouveau
bloc débute.

### Objectif de cet exercice ###

Cet exercice est un peu particulier : il faut que votre programme fonctionne
pour plusieurs buggles, chacune étant dans une situation initiale
différente. Le même code sera utilisé pour chacune d'entre elles.

Quand votre programme fonctionne, passez à l'exercice suivant.

