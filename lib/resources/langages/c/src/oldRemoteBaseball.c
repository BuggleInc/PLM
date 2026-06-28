
#include "../include/RemoteBaseball.h"

int getBasesAmount(){
  send_command("110 getBasesAmout");
  return get_answer_int();
}

int getPositionsAmount(){
  send_command("111 getPositionsAmout");
  return get_answer_int();
}

int getHoleBase(){
  send_command("112 getHoleBase");
  return get_answer_int();
}

int getHolePosition(){
  send_command("113 getHolePosition");
  return get_answer_int();
}

int getPlayerColor(int base, int position){
  send_command("114 %d %d getPlayerColor", base, position);
  return get_answer_int();
}

int isSorted(){
  send_command("115 isSorted");
  return get_answer_int();
}

int isBaseSorted(int base){
  send_command("116 %d isBaseSorted", base);
  return get_answer_int();
}

int isSelected(){
  send_command("117 isSelected");
  return get_answer_int();
}

void move(int base, int position){
  send_command("118 %d %d move", base, position);
}

void assertSorted(char *str) { send_command("119 %s assertSorted", str); }

/* BINDINGS TRANSLATION: French */
int getNombreBases()     						{ return getBasesAmount(); }
int getNombrePositions() 						{ return getPositionsAmount(); }
int getCouleurJoueur(int base, int position) 	{ return getPlayerColor(base,position); }
int estBaseTriee(int base) 						{ return isBaseSorted(base); }
int estTrie()              						{ return isSorted(); }
int getTrouBase()     							{ return getHoleBase(); }
int getTrouPosition() 							{ return getHolePosition(); }
void deplace(int base, int position) 			{ move(base, position); }
int estSelectionne() { return isSelected(); }
