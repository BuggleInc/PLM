## Spirales ##
Nous allons maintenant écrire notre première fonction récursive avec les
tortues. L'objectif est de dessiner des spirales de différentes formes avec
la même fonction, dont le prototype est le suivant:     void spiral(int steps, int angle, int length, int increment)
Pour vous aider à comprendre comment l'écrire, voici un exemple de la suite
des différentes valeurs prises par les paramètres dans un cas:     spiral(5, 90, 0, 3);
    forward(0);
    turnLeft(90);
    spiral(4,90,3,3);
    forward(3);
    turnLeft(90);
    spiral(3,90,6,3);
    forward(6);
    turnLeft(90);
    spiral(2,90,9,3);
    forward(9);
    turnLeft(90);
    spiral(1,90,12,3);
    forward(12);
    turnLeft(90);
    spiral(0,90,12,3);

