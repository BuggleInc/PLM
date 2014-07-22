
#undef printf


void move(int src, int dst){
	printf("110 %d %d move\n", src, dst);
	fflush(stdout);
}

int getSlotSize(int slot){
	int size;
	printf("111 %d getSlotSize\n", slot);
	fflush(stdout);
	scanf("%d",&size);
	flush();
	return size;
}

int isSelected(){
	int sel;
	printf("112 isSelected\n");
	fflush(stdout);
	scanf("%d", &sel);
	flush();
	return sel;
}

int getParam(int nb){
	int param;
	printf("114 %d getParam\n", nb);
	fflush(stdout);
	scanf("%d",&param);
	flush();
	return param;
}

/* BINDINGS TRANSLATION: French */
void deplace(int src,int dst) 	{ move(src, dst); }
int  getTaillePiquet(int rank) 	{ return getSlotSize(rank); }
int estChoisi() 				{ return isSelected(); }

#define printf myPrintf