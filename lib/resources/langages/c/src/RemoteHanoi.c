
#include "../include/RemoteHanoi.h"

void move(int src, int dst) { send_command("110 %d %d move", src, dst); }

int getSlotSize(int slot){
  send_command("111 %d getSlotSize", slot);
  return get_answer_int();
}

int isSelected(){
  send_command("112 isSelected");
  return get_answer_int();
}

int getParam(int nb){
  send_command("114 %d getParam", nb);
  return get_answer_int();
}

/* BINDINGS TRANSLATION: French */
void deplace(int src,int dst) 	{ move(src, dst); }
int  getTaillePiquet(int rank) 	{ return getSlotSize(rank); }
int estChoisi() { return isSelected(); }