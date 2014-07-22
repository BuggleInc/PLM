#ifndef REMOTE_SORT_H
#define REMOTE_SORT_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>


int getValueCount();
int isSmaller(int i, int j);
int isSmallerThan(int i, int value);
void swap(int i, int j);
void copy(int i, int j);
int getValue(int idx);
void setValue(int idx, int value);
int isSelected();


/* BINDINGS TRANSLATION: French */
int getNombreValeurs(); 				
int getValeur(int i);   				
void setValeur(int i,int val); 		
int plusPetit(int i, int j); 			
int plusPetitQue(int i, int value);	
void echange(int i, int j);			
void copie(int from,int to); 		


#endif