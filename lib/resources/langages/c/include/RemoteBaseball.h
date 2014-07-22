#ifndef REMOTE_BASEBALL_H
#define REMOTE_BASEBALL_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>


void assertSorted(char* str);

int getBasesAmount();
int getPositionsAmount();
int getHoleBase();
int getHolePosition();
int getPlayerColor(int base, int position);
int isSorted();
int isBaseSorted(int base);
int isSelected();
void move(int base, int position);


/* BINDINGS TRANSLATION: French */
int getNombreBases();     						
int getNombrePositions(); 						
int getCouleurJoueur(int base, int position); 	
int estBaseTriee(int base); 						
int estTrie();              						
int getTrouBase();     							
int getTrouPosition(); 							
void deplace(int base, int position); 			
int estSelectionne(); 							

#endif