
## Basic Shortest Path algorithm ##

To conclude with this introductory lesson to maze solving algorithms, we will
investigate another way to find the exit. The buggle in this lesson is a special
buggle: he is a jedi. He can feel the Force. This means he is able to feel his environment.

By using method ` BuggleWorld getMyWorld()` he can retrieve information about the world he is leaving in.

A ` BuggleWorld` object is an object on which you can perform the following operations:   
  
` int getHeight()` to know the height of the world.  
` int getWidth()` to know the width of the world.  
` BuggleWorldCell getCell(int x, int y)` ` to retrieve theBuggleWorldCell` object located at a specific position in the
world.  

A ` BuggleWorldCell` is an object that represents a cell of the world. On an object
of this type, it is possible to call the following methods:   
  
` boolean hasContent()` to know if something is written on the floor of this cell.  
` void setContentFromInt(int v)` to write an integer value on the floor of this cell.  
` int getContentAsInt()` to retrieve the integer value which is written on the floor of this cell.  
` void emptyContent()` to clean the floor of this cell.  
` boolean hasTopWall()` to know if a wall is built on the top edge of this cell.  
` boolean hasLeftWall()` to know if a wall is built on the left edge of this cell.  
` boolean hasBaggle()` to know if a baggle is present on this cell.  

It has to be noted that it is not possible to build a wall on the bottom edge or on the right edge of a cell.
Therefore when such wall exists, it means it was built on a sibling cells -- as a top (respectively left) wall
on the cell that is located below (respectively at the right of) the current cell.


### Exercise goal ###

[]() Write a method ` evaluatePaths()` which computes a very basic shortest path
algorithm. This algorithm will write on each world cell (or at least the one that are useful) the distance
from this cell to the maze exit. To achieve this objective, your algorithm has to find the exit of the maze on the map. Then, for every cells
next to the cell where the exit is located, it has to mark these cells with a integer value of 1 (indicating the
distance). Then, it has to iterate the same process to mark the cells which are at a distance of 2 and so on
until it marks the cell where the buggle is located.

Write a method ` followShortestPath()` that will make the jedi buggle to follow the shortest path.
Basically the buggle has only to walk on each case with the lesser distance from the exit. You can use the
method ` void setDirection(Direction d)` to make your buggle look at the specific direction such as ` Direction.NORTH` or ` Direction.EAST` .

  
  
Use the Force Luke!

