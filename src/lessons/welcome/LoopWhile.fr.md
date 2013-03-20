##  ##

In addition to conditionals, another handy construction is the ability to
repeat an action while a specific condition does not appear. A while loop is
used for that, with the following syntax: *while (condition* *) {action()*     ;
    }
*whilecondition* *:action()*     

Evidement, si l'action en question ne modifie pas la valeur de la condition,
la buggle va exécuter l'action à l'infini. C'est dans ce genre de cas que le
bouton *stop* de l'interface devient utile. Pour tester cela, vous
pouvez essayer de taper le code suivant dans l'éditeur :     while (true) {turnLeft();
    }
    while True:turnLeft()
The buggle will turn left while true is true (ie, endlessly), or until you
stop it manually using the stop button.

### Objectif de cet exercice ###
    Il vous faut maintenant écrire le code nécessaire pour
    que vos buggles avancent jusqu'à rencontrer un mur. L'idée est donc de faire
    :while we are not facing a wall, do:moveForward()

Quand votre programme fonctionne, passez à l'exercice suivant.

