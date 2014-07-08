#ifndef REMOTE_FLAG_H
#define REMOTE_FLAG_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

void flush();

void swap(int x, int y);
int getSize();
int getColor(int line);
int isSorted();
int isSelected();

void assertSorted();

/* BINDINGS TRANSLATION: French */
void echange(int i, int j) { swap(i,j); }
int getCouleur(int rank)   { return getColor(rank); }
int getTaille()            { return getSize(); }
int estTrie()          { return isSorted(); }
int estChoisi()        { return isSelected(); }

#endif