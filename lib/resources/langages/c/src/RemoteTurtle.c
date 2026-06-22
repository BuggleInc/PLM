
#include "../include/RemoteTurtle.h"

int getParamInt(int nb){
  send_command("200 %d getParamInt", nb);
  return get_answer_int();
}

double getParamDouble(int nb){
  send_command("201 %d getParamDouble", nb);
  return get_answer_int();
}

void left(double nb) { send_command("110 %lf left", nb); }

void right(double nb) { send_command("111 %lf right", nb); }

void forward(double nb) { send_command("112 %lf forward", nb); }

void backward(double nb) { send_command("113 %lf backward", nb); }

double getX(){
  send_command("114 getX");
  return get_answer_int();
}
	
double getY(){
  send_command("115 getY");
  return get_answer_int();
}

void setX(double nb) { send_command("116 %lf setX", nb); }

void setY(double nb) { send_command("117 %lf setY", nb); }

void setPos(double nb, double nb2){
  send_command("118 %lf %lf setPos", nb, nb2);
}
	
void moveTo(double nb, double nb2){
  send_command("119 %lf %lf moveTo", nb, nb2);
}

void circle(double nb) { send_command("120 %lf circle", nb); }

void hide() { send_command("121 hide"); }

void show() { send_command("122 show"); }

int isVisible(){
  send_command("123 isVisible");
  return get_answer_int();
}

void clear() { send_command("124 clear"); }

double getHeading(){
  send_command("125 getHeading");
  return get_answer_double();
}

void setHeading(double nb) { send_command("126 %lf setHeading", nb); }

void penUp() { send_command("127 penUp"); }

void penDown() { send_command("128 penDown"); }

int isPenDown(){
  send_command("129 isPenDown");
  fflush(stdout);
  return get_answer_int();
}

Color getColor(){
  send_command("130 getColor");
  return get_answer_int();
}

void setColor(Color c) { send_command("131 %d setColor", c); }

int isSelected(){
  send_command("132 isSelected");
  return get_answer_int();
}

/* BINDINGS TRANSLATION: French */
void avance(double steps) 			{ forward(steps); }
void recule(double steps) 			{ backward(steps); }
void gauche(double angle) 			{ left(angle); }
void droite(double angle) 			{ right(angle); }
void cercle(double radius)			{ circle(radius); }
// get/set X/Y/Pos are not translated as they happen to be the same in French
void allerVers(double x, double y) 	{moveTo(x,y);}
double getCap()           			{ return getHeading(); }
void setCap(double cap)   			{ setHeading(cap);     }
void leveCrayon()         			{ penUp(); }
void baisseCrayon()       			{ penDown(); }
int estCrayonBaisse() 				{ return isPenDown();}
Color getCouleur()        			{ return getColor(); }
void setCouleur(Color c)  			{ setColor(c); }
int estChoisi()       				{ return isSelected(); } // we have to document the version without e, since po4a allows for one variant only
int estChoisie()      				{ return isSelected(); } // But we want to have the grammatically correct form also possible (turtles are feminine)
void efface()             			{ clear(); } 
void cache()              			{ hide(); }
void montre()             			{ show(); }
int estVisible() { return isVisible(); }
