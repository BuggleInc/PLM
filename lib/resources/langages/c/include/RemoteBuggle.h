#ifndef REMOTE_BUGGLE_H
#define REMOTE_BUGGLE_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>


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

/* utils */

char* int2str(int nb);
int getParam();
int getParamLangtonColor1(char* tab);
void stepDone();
int*** getParamHelloTurmite1(int* dim1, int* dim2, int* dim3);


void left();
void right();
void back();
void stepForward();
void forward(int nb);
void stepBackward();
void backward(int nb);
int getX();
int getY();
void setX(int nb);
void setY(int nb);
void setPos(int nb, int nb2);
Color getBodyColor();
void setBodyColor(Color color);
int isFacingWall();
int isBackingWall();
Direction getDirection();
void setDirection(Direction dir);
int isSelected();
void brushUp();
void brushDown();
int isBrushDown();
void setBrushColor(Color color);
Color getBrushColor();
Color getGroundColor();
int isOverBaggle();
int isCarryingBaggle();
void pickupBaggle();
void dropBaggle();
int isOverMessage();
void writeMessage(char* str);
char* readMessage();
void clearMessage();
int getWorldHeight();
int getWorldWidth();

void setIndication(int x, int y, int i);
int getIndication(int x, int y);
char getIndicationBdr();
int hasBaggle(int x, int y);
int hasTopWall(int x, int y);
int hasLeftWall(int x, int y);




/* BINDINGS TRANSLATION: French */
void gauche();  		
void droite();   		
void retourne();
void avancePas(); 		
void avance(int steps);
void reculePas();  
void recule(int steps); 	
Color getCouleurCorps();       
void setCouleurCorps(Color c); 
int estFaceMur();           
int estDosMur();            
void leveBrosse();             
void baisseBrosse();          
int estBrosseBaissee();     
Color getCouleurBrosse();      
void setCouleurBrosse(Color c);
Color getCouleurSol();         
int estSurBiscuit();        
int porteBiscuit();         
void prendBiscuit();  	
void poseBiscuit();       
int estSurMessage();        
char* litMessage();         
void ecritMessage(char* s);   
void effaceMessage();          
int getMondeHauteur();         
int getMondeLargeur();         
// get/set X/Y/Pos are not translated as they happen to be the same in French
int estChoisi();            // we have to document the version without e, since po4a allows for one variant only
int estChoisie();           // But we want to have the grammatically correct form also possible (Buggles are feminine in French);

int aBiscuit(int x, int y); 
int aMurNord(int x, int y); 
int aMurOuest(int x, int y);

#endif
