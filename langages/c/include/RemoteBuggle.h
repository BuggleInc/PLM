#ifndef REMOTE_BUGGLE_H
#define REMOTE_BUGGLE_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>


typedef enum{
	white,
	black,
	blue,
	cyan,
	darkGray,
	gray,
	green,
	lightGray,
	magenta,
	orange,
	pink,
	red,
	yellow
}Color;

typedef enum{
	NORTH,
	EAST,
	SOUTH,
	WEST
}Direction;

void left();
void right();
void back();
void forward(int nb);
void backward(int nb);
int getX();
int getY();
void setX(int nb);
void setY(int nb);
void setPos(int nb, int nb2);
Color getBodyColor();
void setBodyColor(Color color);
int isFacingWall();
int isBackingWall();
Direction getDirection();
void setDirection(Direction dir);
int isSelected();
void brushUp();
void brushDown();
int isBrushDown();
void setBrushColor(Color color);
Color getBrushColor();
Color getGroundColor();
int isOverBaggle();
int isCarryingBaggle();
void pickupBaggle();
void dropBaggle();
int isOverMessage();
void writeMessage(char* str);
char* readMessage();
void clearMessage();


#endif
