#ifndef REMOTE_PANCAKE_H
#define REMOTE_PANCAKE_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>


int getStackSize();  
int getPancakeRadius(int rank);  
int isPancakeUpsideDown(int rank);   
void flip(int amount);
int isSorted();
int isSelected(); 

int wasRandom();


/* BINDINGS TRANSLATION: French */
void retourne(int numberOfPancakes); 
int getRayonCrepe(int rank); 		
int getTaillePile(); 				
int estCrepeRetournee(int rank); 	
int estTriee(); 						
int estChoisi(); 					 

#endif

