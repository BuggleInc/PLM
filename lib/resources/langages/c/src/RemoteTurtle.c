
#undef printf

char* int2str(int nb){
	char* str = malloc(sizeof(char)*16);
	sprintf(str, "%d", nb);
	return str;
}

int getParamInt(int nb){
	int p=-1;
	printf("200 %d getParamInt\n",nb);
	fflush(stdout);
	scanf("%d",&p);
	flush();
	return p;
}

double getParamDouble(int nb){
	double p=-1;
	printf("201 %d getParamDouble\n",nb);
	fflush(stdout);
	scanf("%lf",&p);
	flush();
	return p;
}

void left(double nb){
	printf("110 %lf left\n",nb);
}
				
void right(double nb){
	printf("111 %lf right\n",nb);
}
	
void forward(double nb){
	printf("112 %lf forward\n",nb);
}
	
void backward(double nb){
	printf("113 %lf backward\n",nb);
}
	
double getX(){
	double x=-1;
	printf("114 getX\n");
	fflush(stdout);
	scanf("%lf",&x);
	flush();
	return x;
}
	
double getY(){
	double y=-1;
	printf("115 getY\n");
	fflush(stdout);
	scanf("%lf",&y);
	flush();
	return y;
}
	
void setX(double nb){
	printf("116 %lf setX\n", nb);
}

void setY(double nb){
	printf("117 %lf setY\n", nb);
}
	
void setPos(double nb, double nb2){
	printf("118 %lf %lf setPos\n", nb, nb2);
}
	
void moveTo(double nb, double nb2){
	printf("119 %lf %lf moveTo\n", nb, nb2);
}
	
void circle(double nb){
	printf("120 %lf circle\n", nb);
}
	
void hide(){
	printf("121 hide\n");
}
	
void show(){
	printf("122 show\n");
}
	
int isVisible(){
	int vis=0;
	printf("123 isVisible\n");
	fflush(stdout);
	scanf("%d",&vis);
	flush();
	return vis;
}
	
void clear(){
	printf("124 clear\n");
}
	
double getHeading(){
	double cap=-1;
	printf("125 getHeading\n");
	fflush(stdout);
	scanf("%lf",&cap);
	flush();
	return cap;
}
	
void setHeading(double nb){
	printf("126 %lf setHeading\n", nb);
}
	
void penUp(){
	printf("127 penUp\n");
}
	
void penDown(){
	printf("128 penDown\n");
}
	
int isPenDown(){
	int down=0;
	printf("129 isPenDown\n");
	fflush(stdout);
	scanf("%d",&down);
	flush();
	return down;
}

Color getColor(){
	int num=-1;
	printf("130 getColor\n");
	fflush(stdout);
	scanf("%d",&num);
	flush();
	Color c = num;
	return c;
}

void setColor(Color c){
	printf("131 %d setColor\n", c);
}
	
int isSelected(){
	int sel=0;
	printf("132 isSelected\n");
	fflush(stdout);
	scanf("%d",&sel);
	flush();
	return sel;
}





/* BINDINGS TRANSLATION: French */
void avance(double steps) 			{ forward(steps); }
void recule(double steps) 			{ backward(steps); }
void gauche(double angle) 			{ left(angle); }
void droite(double angle) 			{ right(angle); }
void cercle(double radius)			{ circle(radius); }
// get/set X/Y/Pos are not translated as they happen to be the same in French
void allerVers(double x, double y) 	{moveTo(x,y);}
double getCap()           			{ return getHeading(); }
void setCap(double cap)   			{ setHeading(cap);     }
void leveCrayon()         			{ penUp(); }
void baisseCrayon()       			{ penDown(); }
int estCrayonBaisse() 				{ return isPenDown();}
Color getCouleur()        			{ return getColor(); }
void setCouleur(Color c)  			{ setColor(c); }
int estChoisi()       				{ return isSelected(); } // we have to document the version without e, since po4a allows for one variant only
int estChoisie()      				{ return isSelected(); } // But we want to have the grammatically correct form also possible (turtles are feminine)
void efface()             			{ clear(); } 
void cache()              			{ hide(); }
void montre()             			{ show(); }
int estVisible()      				{ return isVisible(); }

#define printf myPrintf