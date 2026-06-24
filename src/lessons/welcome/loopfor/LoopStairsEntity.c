#include "../../../../lib/resources/langages/c/include/RemoteBuggle.h"

Color colors[]            = {blue, cyan, green, yellow, orange, red, magenta, pink};
int inTeerNal_Steep_Count = -3;
void forward_observer(int i)
{
  const int color_length = sizeof(colors) / sizeof(colors[0]);
  if (inTeerNal_Steep_Count < 0 || inTeerNal_Steep_Count % 2 == 1 || (inTeerNal_Steep_Count / 2) >= color_length) {
    if (inTeerNal_Steep_Count < 0)
      setBrushColor(lightGray);
    else if ((inTeerNal_Steep_Count / 2) >= color_length)
      setBrushColor(pink);
    else
      setBrushColor(colors[(inTeerNal_Steep_Count / 2) % color_length]);
    brushDown();
    brushUp();
  }
  inTeerNal_Steep_Count++;
}

void run(){
  set_post_forward(&forward_observer);
  /* BEGIN TEMPLATE */
  /* BEGIN SOLUTION */
  forward(1);
  forward(1);
  forward(1);
  left();
  int i;
  for (i = 0; i < 8; i++) {
    forward(1);
    right();
    forward(1);
    left();
  }
  right();
  forward(1);
  forward(1);
  forward(1);

  /* END SOLUTION */
  /* END TEMPLATE */
}
