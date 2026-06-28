#include "../../../../lib/resources/langages/c/include/RemoteBuggle.h"

char* colors[] = {"0/155/0",   "50/155/0",  "100/155/0", "140/155/0", "160/155/0",
                  "180/155/0", "200/155/0", "210/155/0", "255/0/0"};

void forward_observer(int steps)
{
  if (steps > 1) {
    printf("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.");
    exit(1);
  }

  const int color_length = sizeof(colors) / sizeof(colors[0]);
  char* old              = getGroundColorName();

  if (strcmp(old, "0/0/255") == 0) { /* fallen into water */
    free(old);
    return;
  }

  char* new = old;
  for (int i = 0; i < color_length - 1; i++)
    if (strcmp(colors[i], old) == 0) {
      if (i == color_length - 1)
        new = colors[i];
      else
        new = colors[i + 1];
      break;
    }
  setBrushColorName(new);
  brushDown();
  brushUp();
  free(old);
}

void backward_observer(int steps)
{
  printf("Sorry Dave, I cannot let you use back with an argument in this exercise. Exercising is hard enough -- please "
         "don't overplay.");
  exit(1);
}

void run()
{
//   set_post_forward(&forward_observer);
//   set_pre_backward(&backward_observer);
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  for (int i = 0; i < 7; i++)
    for (int side = 0; side < 4; side++) {
      for (int step = 0; step < 4; step++)
        forward(1);
      left();
      for (int step = 0; step < 2; step++)
        forward(1);
      right();
      for (int step = 0; step < 4; step++)
        forward(1);
      right();
      forward(1);
      forward(1);
      left();
      for (int step = 0; step < 4; step++)
        forward(1);
      left();
    }
  /* END SOLUTION */
  /* END TEMPLATE */
}
