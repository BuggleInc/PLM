#include "../include/RemoteFlag.h"

void swap(int x, int y) { send_command("110 %d %d swap", x, y); }

int getSize(){
  send_command("111 getSize");
  return get_answer_int();
}

int getColor(int line){
  send_command("112 %d getColor", line);
  return get_answer_int();
}

int isSorted(){
  send_command("113 isSorted");
  return get_answer_int();
}

int isSelected(){
  send_command("114 isSelected");
  return get_answer_int();
}


void assertSorted(){
  send_command("115 assertSorted");
  fflush(stdout);
}

/* BINDINGS TRANSLATION: French */
void echange(int i, int j) { swap(i,j); }
int getCouleur(int rank)   { return getColor(rank); }
int getTaille()            { return getSize(); }
int estTrie()          { return isSorted(); }
int estChoisi() { return isSelected(); }