# BEGIN SOLUTION
def branch(size):
  forward(size);
  right(360 / BRANCH_COUNT);
  forward(size);
  #
  for i in range(2):
    left(360 / BRANCH_COUNT);

addSizeHint(165, 200, 165, 150)
BRANCH_COUNT = 5;
for i in range(BRANCH_COUNT):
   branch(50);
# END SOLUTION
