
#include "../include/RemoteBuggle.h"

/* Function for buggles */

void left() { send_command("110 left"); }

void right() { send_command("111 right"); }

void back() { send_command("112 back"); }

void (*pre_forward)(int)  = NULL;
void (*post_forward)(int) = NULL;
void stepForward(){
	forward(1);
}
void forward(int nb)
{
  if (pre_forward != NULL)
    (*pre_forward)(nb);
  send_command("113 %d forward", nb);
  if (post_forward != NULL)
    (*post_forward)(nb);
}
void set_pre_forward(void (*param)(int))
{
  pre_forward = param;
}
void set_post_forward(void (*param)(int))
{
  post_forward = param;
}

void (*pre_backward)(int)  = NULL;
void (*post_backward)(int) = NULL;
void set_pre_backward(void (*param)(int))
{
  pre_backward = param;
}
void set_post_backward(void (*param)(int))
{
  post_backward = param;
}
void stepBackward(){
	backward(1);
}
void backward(int nb)
{
  if (pre_backward != NULL)
    (*pre_backward)(nb);
  send_command("114 %d backward", nb);
  if (post_backward != NULL)
    (*post_backward)(nb);
}

int getX(){
  send_command("115 getX");
  return get_answer_int();
}

int getY(){
  send_command("116 getY");
  return get_answer_int();
}

void setX(int nb) { send_command("117 %d setX", nb); }

void setY(int nb) { send_command("118 %d setY", nb); }

void setPos(int nb, int nb2) { send_command("119 %d %d setPos", nb, nb2); }

Color getBodyColor(){
  send_command("120 getBodyColor");
  Color c = get_answer_int();
  return c;
}

void setBodyColor(Color color) { send_command("121 %d setBodyColor", color); }

int isFacingWall(){
  send_command("122 isFacingWall");
  return get_answer_int();
}

int isBackingWall(){
  send_command("123 isBackingWall");
  return get_answer_int();
}
int isWallOnLeft()
{
  send_command("150 isWallOnLeft");
  return get_answer_int();
}
int isWallOnRight()
{
  send_command("151 isWallOnRight");
  return get_answer_int();
}

Direction getDirection() { return get_answer_int(); }

void setDirection(Direction dir) { send_command("125 %d setDirection", dir); }

int isSelected(){
  send_command("126 isSelected");
  return get_answer_int();
}

void brushUp() { send_command("127 brushUp"); }

void brushDown() { send_command("128 brushDown"); }

int isBrushDown(){
  send_command("129 isBrushDown");
  return get_answer_int();
}
void setBrushColor(Color color) { send_command("130 %d setBrushColor", color); }
void setBrushColorName(const char* name)
{
  send_command("130 %s setBrushColor", name);
}

Color getBrushColor(){
  send_command("131 getBrushColor");
  return get_answer_int();
}

Color getGroundColor(){
  send_command("132 getGroundColor");
  return get_answer_int();
}

int isOverBaggle(){
  send_command("133 isOverBaggle");
  return get_answer_int();
}

int isCarryingBaggle(){
  send_command("134 isCarryingBaggle");
  return get_answer_int();
}

void pickupBaggle() { send_command("135 pickupBaggle"); }

void dropBaggle() { send_command("136 dropBaggle"); }

int isOverMessage(){
  send_command("137 isOverMessage");
  return get_answer_int();
}

void writeMessage(char *str) { send_command("138 %s writeMessage", str); }

char* readMessage(){
  send_command("139 readMessage");
  return get_answer_string();
}

void clearMessage() { send_command("140 clearMessage"); }

int getWorldHeight(){
  send_command("141 getWorldHeight");
  return get_answer_int();
}

int getWorldWidth(){
  send_command("142 getWorldWidth");
  return get_answer_int();
}

void setIndication(int x, int y, int i){
  send_command("143 %d %d %d setIndication", x, y, i);
}


int getIndication(int x, int y){
  send_command("144 %d %d getIndication", x, y);
  return get_answer_int();
}

int hasBaggle(int x, int y){
  send_command("145 %d %d hasBaggle", x, y);
  return get_answer_int();
}

int hasTopWall(int x, int y){
  send_command("146 %d %d hasTopWall", x, y);
  return get_answer_int();
}

int hasLeftWall(int x, int y){
  send_command("147 %d %d hasLeftWall", x, y);
  return get_answer_int();
}

char getIndicationBdr(){
  send_command("148 getIndicationBdr");
  return get_answer_char();
}
char* getGroundColorName()
{
  send_command("149 getGroundColorName");
  return get_answer_string();
}
/* Others */

char* getParam(int i)
{
  send_command("200 %d getParam", i);
  return get_answer_string();
}
int getParamCount()
{
  send_command("201 getParamCount");
  return get_answer_int();
}

void stepDone()
{
  send_command("202 stepDone");
}

int getParamLangtonColor1(char* tab){
  send_command("203 getParamLangtonColor1");
  char *line = get_answer_string();

  int length = strlen(line);
  int i = 0;
  for (i = 0; i < length; i++) {
    tab[i] = line[i];
  }
  free(line);
  return length;
}

int*** getParamHelloTurmite1(int* dim1, int* dim2, int* dim3){
  send_command("204 getParamHelloTurmite1");
  char *line = get_answer_string();

  int i, j, k;
  *dim1 = line[0] - 48;
  *dim2 = line[2] - 48;
  *dim3 = line[4] - 48;
  int curs = 6;

  int ***tab = (int ***)malloc(sizeof(int **) * (*dim1));
  for (i = 0; i < *dim1; i++) {
    tab[i] = (int **)malloc(sizeof(int *) * (*dim2));
    for (j = 0; j < *dim2; j++) {
      tab[i][j] = (int *)malloc(sizeof(int) * (*dim1));
      for (k = 0; k < *dim3; k++) {
        tab[i][j][k] = line[curs] - 48;
        curs += 2;
      }
    }
  }
  return tab;
}

/* BINDINGS TRANSLATION: French */
void gauche()   				{ left(); }
void droite()   				{ right(); }
void retourne() 				{ back(); }
void avancePas()  				{ forward(1); }
void avance(int steps)  		{ forward(steps); }
void reculePas() 				{ backward(1); }
void recule(int steps) 			{ backward(steps); }
Color getCouleurCorps()        	{ return getBodyColor(); }
void setCouleurCorps(Color c)  	{ setBodyColor(c); }
int estFaceMur()           		{ return isFacingWall(); }
int estDosMur()            		{ return isBackingWall(); }
void leveBrosse()              	{ brushUp(); }
void baisseBrosse()           	{ brushDown(); }
int estBrosseBaissee()     		{ return isBrushDown(); }
Color getCouleurBrosse()       	{ return getBrushColor(); }
void setCouleurBrosse(Color c) 	{ setBrushColor(c); }
Color getCouleurSol()          	{ return getGroundColor(); }
int estSurBiscuit()        		{ return isOverBaggle(); }
int porteBiscuit()         		{ return isCarryingBaggle(); }
void prendBiscuit()  			{ pickupBaggle(); }
void poseBiscuit()       		{ dropBaggle(); }
int estSurMessage()        		{ return isOverMessage(); }
char* litMessage()          	{ return readMessage(); }
void ecritMessage(char* s)    	{ writeMessage(s); }
void effaceMessage()           	{ clearMessage(); }
int getMondeHauteur()          	{ return getWorldHeight(); }
int getMondeLargeur()          	{ return getWorldWidth(); }
// get/set X/Y/Pos are not translated as they happen to be the same in French
int estChoisi()           		{ return isSelected(); } // we have to document the version without e, since po4a allows for one variant only
int estChoisie()          		{ return isSelected(); } // But we want to have the grammatically correct form also possible (Buggles are feminine in French)

int aBiscuit(int x, int y) 		{ return hasBaggle(x,y); }
int aMurNord(int x, int y) 		{ return hasTopWall(x,y); }
int aMurOuest(int x, int y) { return hasLeftWall(x, y); }
