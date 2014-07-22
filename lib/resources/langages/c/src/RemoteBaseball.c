
#undef printf



int getBasesAmount(){
	int ba;
	printf("110 getBasesAmout\n");
	fflush(stdout);
	scanf("%d",&ba);
	flush();
	return ba;
}

int getPositionsAmount(){
	int pa;
	printf("111 getPositionsAmout\n");
	fflush(stdout);
	scanf("%d",&pa);
	flush();
	return pa;
}

int getHoleBase(){
	int hb;
	printf("112 getHoleBase\n");
	fflush(stdout);
	scanf("%d",&hb);
	flush();
	return hb;
}

int getHolePosition(){
	int hp;
	printf("113 getHolePosition\n");
	fflush(stdout);
	scanf("%d",&hp);
	flush();
	return hp;
}

int getPlayerColor(int base, int position){
	int color;
	printf("114 %d %d getPlayerColor\n", base, position);
	fflush(stdout);
	scanf("%d",&color);
	flush();
	return color;
}

int isSorted(){
	int sort;
	printf("115 isSorted\n");
	fflush(stdout);
	scanf("%d", &sort);
	flush();
	return sort;
}

int isBaseSorted(int base){
	int sel;
	printf("116 %d isBaseSorted\n", base);
	fflush(stdout);
	scanf("%d", &sel);
	flush();
	return sel;
}

int isSelected(){
	int sel;
	printf("117 isSelected\n");
	fflush(stdout);
	scanf("%d", &sel);
	flush();
	return sel;
}

void move(int base, int position){
	printf("118 %d %d move\n", base, position);
	fflush(stdout);
}

void assertSorted(char* str){
	printf("119 %s assertSorted\n", str);
	fflush(stdout);
}

/* BINDINGS TRANSLATION: French */
int getNombreBases()     						{ return getBasesAmount(); }
int getNombrePositions() 						{ return getPositionsAmount(); }
int getCouleurJoueur(int base, int position) 	{ return getPlayerColor(base,position); }
int estBaseTriee(int base) 						{ return isBaseSorted(base); }
int estTrie()              						{ return isSorted(); }
int getTrouBase()     							{ return getHoleBase(); }
int getTrouPosition() 							{ return getHolePosition(); }
void deplace(int base, int position) 			{ move(base, position); }
int estSelectionne() 							{ return isSelected(); }


#define printf myPrintf