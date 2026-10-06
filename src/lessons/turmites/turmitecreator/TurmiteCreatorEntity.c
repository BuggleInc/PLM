#include "../../../../target/classes/resources/langages/c/RemoteTurmite.h"

/* Do not change these definitions */
/* BEGIN HELPER */
Color* colors;
int colorsLength;
int state = 0;

void step()
{
  int currentColor = 0;
  Color current    = getGroundColor();
  for (int i = 0; i < colorsLength; i++)
    if (current == colors[i])
      currentColor = i;

  setBrushColor(colors[rule[state][currentColor][NEXT_COLOR]]);
  brushDown();
  brushUp();

  switch (rule[state][currentColor][NEXT_MOVE]) {
    case STOP: /* nothing */
      break;
    case NOTURN: /* no turn */
      stepForward();
      break;
    case LEFT:
      left();
      stepForward();
      break;
    case RIGHT:
      right();
      stepForward();
      break;
    case BACK:
      back();
      stepForward();
      break;
    default:
      printf("Unknown turn command associated to i=%d: %d\n", currentColor, rule[state][currentColor][NEXT_MOVE]);
  }

  state = rule[state][currentColor][NEXT_STATE];
}
/* END HELPER */

/* BEGIN TEMPLATE */
#define STOP 0
#define NOTURN 1
#define LEFT 2
#define BACK 4
#define RIGHT 8

#define NEXT_COLOR 0
#define NEXT_MOVE 1
#define NEXT_STATE 2

int nbSteps;
int*** rule;

/**
 * init the rule array from a string defining a Langton's ant
 *
 *  You can use this method inside your init() method if you want
 *  to test langton's ant instead of full turmites.
 */
void initLangton(char* name)
{
  int nbColors = strlen(name); /* As many colors as letters in the ant's name */

  rule    = malloc(sizeof(int**) * 1);       /* one state only */
  rule[0] = malloc(sizeof(int*) * nbColors); /* As many colors as letters in the ant's name */
  for (int i = 0; i < nbColors; i++) {
    rule[0][i] = malloc(sizeof(int) * 3); /* every command set has 3 elements */

    rule[0][i][NEXT_COLOR] = (i + 1) % nbColors;

    if (name[i] == 'L') {
      rule[0][i][NEXT_MOVE] = LEFT;
    } else if (name[i] == 'R') {
      rule[0][i][NEXT_MOVE] = RIGHT;
    } else {
      printf("Unknown command in your ant's name: %c\n", name[i]);
    }

    rule[0][i][NEXT_STATE] = 0; /* only one state */
  }
}
void init()
{
  /* Your code comes here. */

  /* Something like
   *   nbSteps = 42;
   *   rule = a [states][colors][3] int array, eg {{{0, NOTURN, 0}, {0, NOTURN, 0}}};
   * but with possibly more states (ie, bigger 1st dimension), and more colors (ie bigger 2nd dimension)
   * and naturally, less boring than this turmite doing absolutely nothing (runs forward endlessly).
   */

  /* It can also be something like
   *   nbSteps = 42;
   *   initLangton("RL");
   */

  /* remember to send your best creations for inclusion in the gallery */
  /* BEGIN SOLUTION */
  nbSteps = 8342;

  int r0[2][3] = {{1, LEFT, 0}, {1, LEFT, 1}};
  int r1[2][3] = {{0, NOTURN, 0}, {0, NOTURN, 1}};

  rule    = malloc(sizeof(int**) * 2);
  rule[0] = malloc(sizeof(int*) * 2);
  rule[1] = malloc(sizeof(int*) * 2);
  for (int i = 0; i < 2; i++) {
    rule[0][i] = malloc(sizeof(int) * 3);
    rule[1][i] = malloc(sizeof(int) * 3);
    for (int j = 0; j < 3; j++) {
      rule[0][i][j] = r0[i][j];
      rule[1][i][j] = r1[i][j];
    }
  }

  setX(8);
  setY(33);
  /* END SOLUTION */
}
/* END TEMPLATE */

void run()
{
  Color allColors[]  = {white, yellow, red, cyan, green, orange, blue, black, gray, magenta, darkGray, pink, lightGray};
  int allColorsCount = 13;

  init();

  colorsLength = 2; /* i.e. rule[0].length: number of colors used by state 0 (hardcoded, C arrays carry no length) */
  colors       = malloc(sizeof(Color) * colorsLength);
  int i;
  for (i = 0; i < colorsLength && i < allColorsCount; i++)
    colors[i] = allColors[i];
  for (; i < colorsLength; i++) /* allColors is too short; pick arbitrary colors */
    colors[i] = (Color)rand();

  for (int stepNb = 0; stepNb < nbSteps; stepNb++) {
    step();
    stepDone();
  }
}
