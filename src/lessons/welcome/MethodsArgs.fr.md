
## Méthodes avec paramètres ##
N'êtes vous pas fatigué d'écrire encore et encore le code qui permet
d'avancer ou de reculer d'un nombre prédéterminé pas ? Oui, mais écrire les
méthode forward2() , forward3() , forward4() , et backward2() , backward3() , backward4() , et ainsi
de suite, ça ne constitue pas un réel gain de temps...

Heureusement, il est possible de donner des **paramètres** à vos
méthodes. Il faut marquer leur type et leur nom entre les parenthèses qui
suivent le nom de la méthode. Ensuite, on peut les utiliser dans le corps de
la fonction comme s'il s'agissait de variables définies ici. 
<pre> double diviseParDeux(double x) {return x / 2;
}</pre>

À l'usage, il faut indiquer les valeurs qu'elles doivent prendre entre les
parenthèses de l'appel. 
<pre> double y = diviseParDeux(3.14);</pre>

Si on veut avoir plusieurs paramètres, il faut les séparer par des virgules,
lors de la déclaration comme lors de l'appel. 
<pre> double divise(double x, double y) {return x / y;
}</pre>

<pre> double y = divise(3.14 , 1.5);</pre>

En Java, il est possible d'avoir plusieurs méthodes du même nom, à condition
qu'elles n'aient pas le même nombre et les mêmes types de paramètres (on dit
qu'elles n'ont pas la même **signature** ). 
<pre> int max(int x, int y) {if (x > y) {return x;}return y;
}
int max(int x, int y, int z) {if (x > y&&x > z) {return x;}if (y > z) {return y;}return z;
}</pre>

Remarquez que nous avons ici laissé de coté les else de chaque
alternative. Cela fonctionne tout de même car un return interrompt
l'exécution de la méthode. Si on arrive à la dernière ligne de ` max(int,int)` , on est donc sur que ` x=y` car dans
le cas contraire, le return de la deuxième ligne aurait arrêté
l'exécution de la fonction.


### Objectif de cet exercice ###
` Il s'agit cette fois d'écrire une méthodemove(int nbPas, boolean forward)` ` qui avance denbPas` ` siforward`

Cette fois, il y a un seul monde, et sept buggles. Mais ça ne change pas
grand chose pour vous, puisque toutes sont ici régies par le même code.

Le code de la méthode à proprement parler ne devrait pas poser de problème.


## Et ensuite? ##

Vous connaissez maintenant les bases de la programmation Java. Disons au
moins que vous avez vu les concepts les plus important et que vous devriez
être capable de lire et comprendre la plupart des programmes écrits en
Java. Pour plus de sécurité, vous devriez faire les autres exercices de
cette leçon pour solidifier vos acquis en les réutilisant dans diverses
situations. Après cela, vous maîtriserez ce qu'on appelle la "programmation
tactique", ce qui veut dire que votre maîtrise de la syntaxe Java vous
permettra de vous concentrer sur les problèmes à résoudre sans vous battre
contre le compilateur à propos de la syntaxe. Certains de ces exercices sont
même amusants à résoudre ;)

Si vous êtes pressés et voulez plus, vous pouvez sauter ces exercices et
passer directement à des exercices plus complets et plus intéressants. Par
exemple, la leçon sur les *Labyrinthes* vous apprendra à sortir d'un
labyrinthe. Les algorithmes impliqués ne sont pas ultra complexes, mais vous
serez amenés à améliorer votre code à plusieurs reprises afin de pouvoir
sortir de n'importe quel type de labyrinthe. La leçon *LightBot* est un
petit jeu de programmation où vous contrôlez un robot voulant éclairer le
monde. Comme il est programmé graphiquement et non en Java, les premiers
exercices peuvent être utilisés comme une introduction à ce que programmer
veut dire pour les débutants. Mais les derniers exercices sont plus
difficiles et devraient constituer des casse-têtes même pour les
programmeurs professionnels.

