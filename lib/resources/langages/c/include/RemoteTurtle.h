#ifndef REMOTE_TURTLE_H
#define REMOTE_TURTLE_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>



typedef enum{
	white,
	black,
	blue,
	cyan,
	darkGray,
	gray,
	green,
	lightGray,
	magenta,
	orange,
	pink,
	red,
	yellow
}Color;

typedef enum{
	NORTH,
	EAST,
	SOUTH,
	WEST
}Direction;


int getParamInt(int nb);
double getParamDouble(int nb);

void left(double nb);
void right(double nb);
void forward(double nb);
void backward(double nb);
double getX();
double getY();
void setX(double nb);	
void setY(double nb);
void setPos(double nb, double nb2);
void moveTo(double nb, double nb2);
void circle(double nb);
void hide();
void show();
int isVisible();
void clear();
double getHeading();
void setHeading(double nb);
void penUp();
void penDown();
int isPenDown();
Color getColor();
void setColor(Color c);
int isSelected();


/* BINDINGS TRANSLATION: French */
void avance(double steps); 	
void recule(double steps); 	
void gauche(double angle); 	
void droite(double angle); 	
void cercle(double radius);	
// get/set X/Y/Pos are not translated as they happen to be the same in French
void allerVers(double x, double y);
double getCap();           	
void setCap(double cap);   	
void leveCrayon();         	
void baisseCrayon();       	
int estCrayonBaisse(); 		
Color getCouleur();        	
void setCouleur(Color c);  	
int estChoisi();       		 // we have to document the version without e, since po4a allows for one variant only
int estChoisie();      		 // But we want to have the grammatically correct form also possible (turtles are feminine)
void efface();            	 
void cache();              	
void montre();             	
int estVisible();      		

#endif