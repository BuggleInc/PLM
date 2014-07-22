
#undef printf
/* Utils */


char* int2str(int nb){
	char* str = malloc(sizeof(char)*16);
	sprintf(str, "%d", nb);
	return str;
}



/* Function for buggles */


void left(){
	printf("110 left\n");
}

void right(){
	printf("111 right\n");
}

void back(){
	printf("112 back\n");
}

void stepForward(){
	forward(1);
}

void forward(int nb){
	printf("113 %d forward\n", nb);
}

void stepBackward(){
	backward(1);
}

void backward(int nb){
	printf("114 %d backward\n",nb );
}

int getX(){
	int x=-1;
	printf("115 getX\n");
	fflush(stdout);
	scanf("%d",&x);
	flush();
	return x;
}

int getY(){
	int y=-1;
	printf("116 getY\n");
	fflush(stdout);
	scanf("%d",&y);
	flush();
	return y;
}

void setX(int nb){
	printf("117 %d setX\n", nb);
}

void setY(int nb){
	printf("118 %d setY\n", nb);
}

void setPos(int nb, int nb2){
	printf("119 %d %d setPos\n", nb, nb2);
}

Color getBodyColor(){
	printf("120 getBodyColor\n");
	int num=-1;
	fflush(stdout);
	scanf("%d",&num);
	flush();
	Color c = num;
	return c;
}

void setBodyColor(Color color){
	printf("121 %d setBodyColor\n", color);
}

int isFacingWall(){
	int face=0;
	printf("122 isFacingWall\n");
	fflush(stdout);
	scanf("%d",&face);
	flush();
	return face;
}

int isBackingWall(){
	int back=0;
	printf("123 isBackingWall\n");
	fflush(stdout);
	scanf("%d",&back);
	flush();
	return back;
}

Direction getDirection(){
	int dir=0;
	printf("124 getDirection\n");
	fflush(stdout);
	scanf("%d", &dir);
	flush();
	return dir;
}

void setDirection(Direction dir){
	printf("125 %d setDirection\n", dir);
}

int isSelected(){
	int sel=0;
	printf("126 isSelected\n");
	fflush(stdout);
	scanf("%d",&sel);
	flush();
	return sel;
}

void brushUp(){
	printf("127 brushUp\n");
}

void brushDown(){
	printf("128 brushDown\n");
}

int isBrushDown(){
	int down=0;
	printf("129 isBrushDown\n");
	fflush(stdout);
	scanf("%d",&down);
	flush();
	return down;
}
void setBrushColor(Color color){
	printf("130 %d setBrushColor\n", color);
}

Color getBrushColor(){
	int num=-1;
	printf("131 getBrushColor\n");
	fflush(stdout);
	scanf("%d",&num);
	flush();
	Color c = num;
	return c;
}

Color getGroundColor(){
	int num=-1;
	printf("132 getGroundColor\n");
	fflush(stdout);
	scanf("%d",&num);
	flush();
	return num;
}

int isOverBaggle(){
	int over=0;
	printf("133 isOverBaggle\n" );
	fflush(stdout);
	scanf("%d",&over);
	flush();
	return over;
}

int isCarryingBaggle(){
	int carr=1;
	printf("134 isCarryingBaggle\n");
	fflush(stdout);
	scanf("%d",&carr);
	flush();
	return carr;
}

void pickupBaggle(){
	printf("135 pickupBaggle\n");
}

void dropBaggle(){
	printf("136 dropBaggle\n");
}

int isOverMessage(){
	int over=0;
	printf("137 isOverMessage\n");
	fflush(stdout);
	scanf("%d",&over);
	flush();
	return over;
}

void writeMessage(char* str){
	printf("138 %s writeMessage\n",str);
}

char* readMessage(){
	char* mess = malloc(sizeof(char)*1024);
	printf("139 readMessage\n");
	fflush(stdout);
	scanf("%s",mess);
	flush();
	return mess;
}

void clearMessage(){
	printf("140 clearMessage\n");
}

int getWorldHeight(){
	int h=-1;
	printf("141 getWorldHeight\n");
	fflush(stdout);
	scanf("%d",&h);
	flush();
	return h;
}

int getWorldWidth(){
	int w=-1;
	printf("142 getWorldWidth\n");
	fflush(stdout);
	scanf("%d",&w);
	flush();
	return w;
}

void setIndication(int x, int y, int i){
	printf("143 %d %d %d setIndication\n", x,y,i);
}


int getIndication(int x, int y){
	int ind=-1;
	printf("144 %d %d getIndication\n",x,y);
	fflush(stdout);
	scanf("%d",&ind);
	flush();
	return ind;
}

int hasBaggle(int x, int y){
	int has=1;
	printf("145 %d %d hasBaggle\n",x,y);
	fflush(stdout);
	scanf("%d",&has);
	flush();
	return has;
}

int hasTopWall(int x, int y){
	int has=1;
	printf("146 %d %d hasTopWall\n",x,y);
	fflush(stdout);
	scanf("%d",&has);
	flush();
	return has;
}

int hasLeftWall(int x, int y){
	int has=1;
	printf("147 %d %d hasLeftWall\n",x,y);
	fflush(stdout);
	scanf("%d",&has);
	flush();
	return has;
}

char getIndicationBdr(){
	char c=' ';
	printf("148 getIndicationBdr\n");
	fflush(stdout);
	scanf("%c",&c);
	flush();
	return c;
}




/* Others */

int getParam(){
	int p=-1;
	printf("200 getParam\n");
	fflush(stdout);
	scanf("%d",&p);
	flush();
	return p;
}

void stepDone(){
	printf("201 stepDone\n");
	fflush(stdout);
}

int getParamLangtonColor1(char* tab){
	char* line = (char*)malloc(sizeof(char)*256);
	printf("202 getParamLangtonColor1\n");
	fflush(stdout);
	scanf("%s",line);
	flush();

	int length = strlen(line);
	int i=0;
	for(i=0;i<length;i++){
		tab[i]=line[i];
	}
	free(line);
	return length;
}

int*** getParamHelloTurmite1(int* dim1, int* dim2, int* dim3){
	char* line = (char*)malloc(sizeof(char)*256);
	printf("203 getParamHelloTurmite1\n");
	fflush(stdout);
	scanf("%s",line);
	flush();
	int i,j,k;
	*dim1=line[0]-48;
	*dim2=line[2]-48;
	*dim3=line[4]-48;
	int curs=6;

	int*** tab = (int***)malloc(sizeof(int**) * (*dim1));
	for(i=0;i<*dim1;i++){
		tab[i]=(int**)malloc(sizeof(int*)* (*dim2));
		for(j=0;j<*dim2;j++){
			tab[i][j]=(int*)malloc(sizeof(int)* (*dim1));
			for(k=0;k<*dim3;k++){
				tab[i][j][k]=line[curs]-48;
				curs+=2;
			}
		}
	}
	return tab;
}

/* BINDINGS TRANSLATION: French */
void gauche()   				{ left(); }
void droite()   				{ right(); }
void retourne() 				{ back(); }
void avancePas()  				{ forward(1); }
void avance(int steps)  		{ forward(steps); }
void reculePas() 				{ backward(1); }
void recule(int steps) 			{ backward(steps); }
Color getCouleurCorps()        	{ return getBodyColor(); }
void setCouleurCorps(Color c)  	{ setBodyColor(c); }
int estFaceMur()           		{ return isFacingWall(); }
int estDosMur()            		{ return isBackingWall(); }
void leveBrosse()              	{ brushUp(); }
void baisseBrosse()           	{ brushDown(); }
int estBrosseBaissee()     		{ return isBrushDown(); }
Color getCouleurBrosse()       	{ return getBrushColor(); }
void setCouleurBrosse(Color c) 	{ setBrushColor(c); }
Color getCouleurSol()          	{ return getGroundColor(); }
int estSurBiscuit()        		{ return isOverBaggle(); }
int porteBiscuit()         		{ return isCarryingBaggle(); }
void prendBiscuit()  			{ pickupBaggle(); }
void poseBiscuit()       		{ dropBaggle(); }
int estSurMessage()        		{ return isOverMessage(); }
char* litMessage()          	{ return readMessage(); }
void ecritMessage(char* s)    	{ writeMessage(s); }
void effaceMessage()           	{ clearMessage(); }
int getMondeHauteur()          	{ return getWorldHeight(); }
int getMondeLargeur()          	{ return getWorldWidth(); }
// get/set X/Y/Pos are not translated as they happen to be the same in French
int estChoisi()           		{ return isSelected(); } // we have to document the version without e, since po4a allows for one variant only
int estChoisie()          		{ return isSelected(); } // But we want to have the grammatically correct form also possible (Buggles are feminine in French)

int aBiscuit(int x, int y) 		{ return hasBaggle(x,y); }
int aMurNord(int x, int y) 		{ return hasTopWall(x,y); }
int aMurOuest(int x, int y)		{ return hasLeftWall(x, y); }

#define printf myPrintf