#ifndef REMOTE_HANOI_H
#define REMOTE_HANOI_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>


void move(int src, int dst);
int getSlotSize(int slot);
int isSelected();

int getParam(int nb);

/* BINDINGS TRANSLATION: French */
void deplace(int src,int dst);
int  getTaillePiquet(int rank);
int estChoisi();

#endif