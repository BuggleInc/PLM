#include "../../../../lib/resources/langages/c/include/RemoteBuggle.h"

char* colors[] = {"255/255/255", "255/240/240", "255/220/220", "255/205/205", "255/190/190", "255/170/170",
                  "255/150/150", "255/130/130", "255/110/110", "255/45/45",   "255/5/5",     "255/0/255"};

void forward_observer(int steps)
{
  if (steps > 1) {
    printf("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.");
    exit(1);
  }

  const int color_length = sizeof(colors) / sizeof(colors[0]);
  char* old              = getGroundColorName();
  char* new              = old;
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
  for (int i = 0; i < 10; i++)
    for (int side = 0; side < 4; side++) {
      for (int step = 0; step < 8; step++)
        forward(1);
      left();
    }
  /* END SOLUTION */
  /* END TEMPLATE */
}
