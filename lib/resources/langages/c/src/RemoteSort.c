
#include "../include/RemoteSort.h"

int getValueCount(){
  send_command("110 getValueCount");
  return get_answer_int();
}

int isSmaller(int i, int j){
  send_command("111 %d %d isSmaller", i, j);
  return get_answer_int();
}

int isSmallerThan(int i, int value){
  send_command("112 %d %d isSmallerThan", i, value);
  return get_answer_int();
}

void swap(int i, int j) { send_command("113 %d %d swap", i, j); }

void copy(int i, int j) { send_command("114 %d %d copy", i, j); }

int getValue(int idx){
  send_command("115 %d getValue", idx);
  return get_answer_int();
}

void setValue(int idx, int value){
  send_command("116 %d %d setValue", idx, value);
}

int isSelected(){
  send_command("117 isSelected");
  return get_answer_int();
}


/* BINDINGS TRANSLATION: French */
int getNombreValeurs() 				{ return getValueCount(); }
int getValeur(int i)   				{ return getValue(i);}
void setValeur(int i,int val) 		{ setValue(i, val); }
int plusPetit(int i, int j) 		{ return isSmaller(i, j); }	
int plusPetitQue(int i, int value)	{ return isSmallerThan(i, value); }
void echange(int i, int j) 			{ swap(i,j); }
void copie(int from, int to) { copy(from, to); }
