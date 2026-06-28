
#include "../include/RemotePancake.h"

int getStackSize(){
  send_command("110 getStackSize");
  return get_answer_int();
}

int getPancakeRadius(int rank){
  send_command("111 %d getPancakeRadius", rank);
  return get_answer_int();
} 

int isPancakeUpsideDown(int rank){
  send_command("112 %d isPancakeUpsideDown", rank);
  return get_answer_int();
}

void flip(int amount) { send_command("113 %d flip", amount); }

int isSorted(){
  send_command("114 isSorted");
  return get_answer_int();
}

int isSelected(){
  send_command("115 isSelected");
  return get_answer_int();
}  

int wasRandom(){
  send_command("116 wasRandom");
  return get_answer_int();
}  

/* BINDINGS TRANSLATION: French */
void retourne(int numberOfPancakes) { flip(numberOfPancakes); }
int getRayonCrepe(int rank) 		{ return getPancakeRadius(rank); }
int getTaillePile() 				{ return getStackSize(); }
int estCrepeRetournee(int rank) 	{ return isPancakeUpsideDown(rank); }
int estTriee() 						{ return isSorted(); }
int estChoisi() { return isSelected(); }