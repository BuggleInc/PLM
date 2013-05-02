
## Écrire des programmes plus complexes ##
Maintenant que vous savez écrire des choses sur le sol, nous allons utiliser
cette compétence pour dessiner un merveilleux G par terre. Consultez le
panneau d'objectif pour les détails de ce qui est attendu.

Quand vous écrivez un programme un peu complexe, il est souvent pratique
d'ajouter des **commentaires** pour simplifier la relecture du code après
coup. Ici par exemple, il est assez facile de se perdre dans les ordres
nécessaires au dessin. Il peut être pratique d'ajouter des commentaires
comme *fin de la barre verticale* ou *Fini de dessiner le G. Retournons à la position initiale* . Documenter votre code est presque toujours nécessaire
si
quelqu'un (vous ou quelqu'un d'autre) veut le lire après coup. Cependant,
commenter des
choses triviales est une mauvaise idée car cela dilue les informations
dans des commentaires trop verbeux. En anglais, ce travers s'appelle *overcommenting* .


<java>

Il y a trois sortes de commentaires en Java, indiquant au compilateur qu'il
ne
doit pas lire le texte ajouté pour les humains.

</java> 
<python>

Il y a deux sortes de commentaires en Python, indiquant au compilateur qu'il
ne
doit pas lire le texte ajouté pour les humains.

</python>   
  
*  **Commentaires sur une seule ligne** . Dès que le compilateur rencontre
les symboles //, il ignore la fin de la ligne.  
*  **Commentaires sur plusieurs lignes** . Le compilateur ignore tout ce qui
se trouve entre les symboles /* et */, même s'ils sont sur des lignes
différentes  
  
  
*  **Commentaires sur une seule ligne** . Dès que le compilateur rencontre le
symbole #, il ignore la fin de la ligne.  
*  **Commentaires sur plusieurs lignes** . Le compilateur ignore tout ce qui
se
trouve entre une ligne commençant par ''' et la prochaine ligne terminant
par '''.  

<java> 
<pre> 
<comment> ( appelMethodeLuParLeCompilateur();// tout ceci est ignoré) </comment> 
<comment> ( autreAppel();/* ceci est) </comment> 
<comment> ( également ignoré */) </comment> encoreUnAppel();</pre>
</java> 
<python> 
<pre> 
<comment> ( appelMethodeLuParLeCompilateur();# tout ceci est ignoré) </comment> 
<comment> ( autreAppel();''' ceci est) </comment> 
<comment> ( également ignoré ''') </comment> encoreUnAppel();</pre>
</python> 
<java>

Il existe une troisième forme de commentaires en Java, entre /** et */, qui
sont lus par un programme nommé JavaDoc pour générer automatiquement la
documentation expliquant comment utiliser le code. Ces commentaires doivent
suivre un formalisme précis.

</java> 
<python>

Les commentaires sur plusieurs lignes sont souvent utilisés pour documenter
comment utiliser le code, tandis que les autres types de commentaires sont
souvent utilisé pour documenter comment le code fonctionne.

</python>

