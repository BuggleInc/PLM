
#undef printf


int getValueCount(){
	int val;
	printf("110 getValueCount\n");
	fflush(stdout);
	scanf("%d",&val);
	flush();
	return val;
}

int isSmaller(int i, int j){
	int small;
	printf("111 %d %d isSmaller\n", i, j);
	fflush(stdout);
	scanf("%d", &small);
	flush();
	return small;
}

int isSmallerThan(int i, int value){
	int small;
	printf("112 %d %d isSmallerThan\n", i, value);
	fflush(stdout);
	scanf("%d", &small);
	flush();
	return small;
}

void swap(int i, int j){
	printf("113 %d %d swap\n", i, j);
	fflush(stdout);
}

void copy(int i, int j){
	printf("114 %d %d copy\n", i, j);
	fflush(stdout);
}

int getValue(int idx){
	int val;
	printf("115 %d getValue\n", idx);
	fflush(stdout);
	scanf("%d",&val);
	flush();
	return val;
}

void setValue(int idx, int value){
	printf("116 %d %d setValue\n", idx, value);
	fflush(stdout);
}

int isSelected(){
	int sel;
	printf("117 isSelected\n");
	fflush(stdout);
	scanf("%d", &sel);
	flush();
	return sel;
}


/* BINDINGS TRANSLATION: French */
int getNombreValeurs() 				{ return getValueCount(); }
int getValeur(int i)   				{ return getValue(i);}
void setValeur(int i,int val) 		{ setValue(i, val); }
int plusPetit(int i, int j) 		{ return isSmaller(i, j); }	
int plusPetitQue(int i, int value)	{ return isSmallerThan(i, value); }
void echange(int i, int j) 			{ swap(i,j); }
void copie(int from,int to) 		{ copy(from,to);}

#define printf myPrintf