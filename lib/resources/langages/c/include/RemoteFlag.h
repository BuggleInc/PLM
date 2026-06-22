#ifndef REMOTE_FLAG_H
#define REMOTE_FLAG_H

#include "Remote.h"

void swap(int x, int y);
int getSize();
int getColor(int line);
int isSorted();
int isSelected();

void assertSorted();

/* BINDINGS TRANSLATION: French */
void echange(int i, int j);
int getCouleur(int rank);
int getTaille();
int estTrie();
int estChoisi();

#endif