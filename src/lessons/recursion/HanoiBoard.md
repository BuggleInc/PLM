
## Tower of Hanoi ##
The Tower of Hanoi or Towers of Hanoi , also called the Tower of Brahma or
Towers of Brahma, is a mathematical game or puzzle. It consists of three rods,
and a number of disks of different sizes which can slide onto any rod. The
puzzle starts with the disks in a neat stack in ascending order of size on one
rod, the smallest at the top, thus forming a pyramid.

The objective of the puzzle is to move the entire stack to another rod, obeying the following rules:   
  
*  Only one disk may be moved at a time.  
*  Each move consists of taking the upper disk from one of the rods and
sliding it onto another rod, on top of the other disks that may already be
present on that rod.  
*  No disk may be placed on top of a smaller disk.  

### Goal of this exercise ###
Write the core of the method: ` public void solve(int src, int dst, int height) throws HanoiInvalidMove` This method will recursively solve the presented problem. First parameter
named ` src` is the index of the initial tower, second parameter ` dst` is the index of the expected final tower, and the third
parameter ` height` is the height of the tower.

A key to solving this puzzle is to recognize that it can be solved by breaking
the problem down into a collection of smaller problems and further breaking
those problems down into even smaller problems until a solution is reached.

The following procedure demonstrates this approach:   
  
*  label the pegs A, B, C (these labels may move at different steps)  
*  let n be the total number of discs (the height of the initial tower)  
*  number the discs from 1 (smallest, topmost) to n (largest, bottommost)  
To move n discs from peg A to peg C:   
  
*  move n−1 discs from A to B. This leaves disc #n alone on peg A  
*  move disc #n from A to C  
*  move n−1 discs from B to C so they sit on disc #n

