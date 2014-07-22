
#undef printf

void swap(int x, int y){
	printf("110 %d %d swap\n", x, y);
	fflush(stdout);
}

int getSize(){
	int size;
	printf("111 getSize\n");
	fflush(stdout);
	scanf("%d",&size);
	flush();
	return size;
}

int getColor(int line){
	int col;
	printf("112 %d getColor\n", line);
	fflush(stdout);
	scanf("%d",&col);
	flush();
	return col;
}

int isSorted(){
	int sort;
	printf("113 isSorted\n");
	fflush(stdout);
	scanf("%d", &sort);
	flush();
	return sort;
}

int isSelected(){
	int sel;
	printf("114 isSelected\n");
	fflush(stdout);
	scanf("%d", &sel);
	flush();
	return sel;
}


void assertSorted(){
	printf("115 assertSorted\n");
	fflush(stdout);
}

/* BINDINGS TRANSLATION: French */
void echange(int i, int j) { swap(i,j); }
int getCouleur(int rank)   { return getColor(rank); }
int getTaille()            { return getSize(); }
int estTrie()          { return isSorted(); }
int estChoisi()        { return isSelected(); }

#define printf myPrintf