#include "RemoteBuggle.h"


/* Utils */

void flush(){
    int c = 0;
    FILE* f = fopen("/tmp/flush","a+");
    while (c != '\n' && c != EOF){
        c = getchar();
        fputc(c,f);
    }
    fclose(f);
}

char* int2str(int nb){
	char* str = malloc(sizeof(char)*16);
	sprintf(str, "%d", nb);
	return str;
}



/* Function for buggles */

int getParam(){
	int p=-1;
	printf("109 getParam\n");
	fflush(stdout);
	scanf("%d",&p);
	flush();
	return p;
}

void left(){
	printf("110 left\n");
}

void right(){
	printf("111 right\n");
}

void back(){
	printf("112 back\n");
}

void forward(int nb){
	printf("113 %d forward\n", nb);
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
	printf("132 getBrushColor\n");
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

