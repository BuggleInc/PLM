#include "../../../../../lib/resources/langages/c/include/RemoteBuggle.h"

void real_left()
{
  left();
}
void go_left(int line_count)
{
  static int first_line_count = -1;

  if (first_line_count < 0)
    first_line_count = line_count;
  else if (first_line_count != line_count) {
    printf("Sorry Dave, I cannot let you use left() in two separate lines of this exercise. You can write left() only "
           "once today. Use a for loop as instructed.\n");
    exit(1);
  }
  left();
}
void go_right()
{
  printf("Sorry Dave, I cannot let you use right() in this exercise. Use left() instead.");
  exit(1);
}
#define left() go_left(__LINE__)
#define right() go_right()

/* BEGIN TEMPLATE */
/* BEGIN SOLUTION */
void dogHouse() {
	int i;
	for (i=0;i<4;i++) {
		forward(1);
		forward(1);
		left();
	}
}
/* END SOLUTION */
/* END TEMPLATE */

void run()
{
  brushDown();
  dogHouse();
  brushUp();

  forward(4);

  brushDown();
  dogHouse();
  brushUp();

  forward(2);
  real_left();
  forward(4);

  brushDown();
  dogHouse();
  brushUp();

  forward(2);
  real_left();
  forward(4);

  brushDown();
  dogHouse();
}
