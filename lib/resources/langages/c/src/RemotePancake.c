
#undef printf


int getStackSize(){
	int size;
	printf("110 getStackSize\n");
	fflush(stdout);
	scanf("%d",&size);
	flush();
	return size;
}

int getPancakeRadius(int rank){
	int rad;
	printf("111 %d getPancakeRadius\n", rank);
	fflush(stdout);
	scanf("%d",&rad);
	flush();
	return rad;
} 

int isPancakeUpsideDown(int rank){
	int down;
	printf("112 %d isPancakeUpsideDown\n", rank);
	fflush(stdout);
	scanf("%d", &down);
	flush();
	return down;
}  

void flip(int amount){
	printf("113 %d flip\n",amount);
	fflush(stdout);
}

int isSorted(){
	int sort;
	printf("114 isSorted\n");
	fflush(stdout);
	scanf("%d", &sort);
	flush();
	return sort;
}

int isSelected(){
	int sel;
	printf("115 isSelected\n");
	fflush(stdout);
	scanf("%d", &sel);
	flush();
	return sel;
}  

int wasRandom(){
	int rand;
	printf("116 wasRandom\n");
	fflush(stdout);
	scanf("%d", &rand);
	flush();
	return rand;
}  

/* BINDINGS TRANSLATION: French */
void retourne(int numberOfPancakes) { flip(numberOfPancakes); }
int getRayonCrepe(int rank) 		{ return getPancakeRadius(rank); }
int getTaillePile() 				{ return getStackSize(); }
int estCrepeRetournee(int rank) 	{ return isPancakeUpsideDown(rank); }
int estTriee() 						{ return isSorted(); }
int estChoisi() 					{ return isSelected(); }

#define printf myPrintf