
# PancakeWorld #

This world implements the control freak pancake seller problem, who wants his pancakes to be sorted on their plate from the biggest to the smallest.

You are asked to resort a pancakes stack to make him happy. Since it is obviously forbidden to place a pancake on the dirty table, the only allowed action is to flip one or more pancakes on the top of the stack.
A pancake is defined by its radius and position on the stack, from the top to the bottom. These characteristics are positive integers.

  
  


<pre> Only three functions are provided :int getStackSize()</pre>
Returns the size of the stack, in other words the amount of pancakes it contains.


<pre> int getPancakeRadius(int pancakeNumber)</pre>
Returns the radius of the pancake you gave in argument.


<pre> void flip(int numberOfPancakes)</pre>
Flips the ` numberOfPancakes` first pancakes composing the stack, from the top of it.

