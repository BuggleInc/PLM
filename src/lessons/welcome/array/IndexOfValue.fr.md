# Indice d'une valeur donnée #

L'objectif de cet exercice est de calculer l'indice d'une valeur donnée (17
dans cet exercice) dans un tableau.

Pour cela, il vous faut remplir le corps de la méthode . Ses paramètres sont le tableau à explorer et la
valeur
à chercher. Si la valeur n'est pas dans le tableau , la méthode doit renvoyer -1.

L'idée de l'algorithme est de parcourir tout le tableau en vérifiant la
valeur
contenue dans chaque case. S'il s'agit de la valeur cherchée, vous devez
renvoyer l'indice de la case actuellement explorée.

La première chose dont il faut se souvenir à propos des tableaux est que
leurs
indices sont numérotés à partir de 0. Donc, si vous avez 3 cases, leurs
indices
seront 0, 1 et 2. Il n'y aura pas de case numérotée 3.

Ensuite, souvenez vous que le nombre de cases d'un tableau peut être
retrouvé
grâce à l'attribut . Donc, si votre tableau s'appelle , sa taille sera . Notez
qu'il
n'y a pas de () après . Un attribut est une sorte de
variable embarquée dans un autre objet (ici, le tableau).

Donc, la dernière valeur d'un tableau est donnée par .

