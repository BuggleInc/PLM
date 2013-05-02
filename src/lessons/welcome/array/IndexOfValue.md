
# Searching a given value #

The goal of this exercise is to search the cell of value 17 in an array, and return its position.

To that extend, you should fill the method ` indexOf(int[] tab,
int lookingFor)` , which parameters are the array to explore,
and the value to search. If the value ` lookingFor` is not in the array ` tab` , the method should return -1.

The idea of the algorithm is to sweep over the whole array,
checking the value of each cell. If it's the searched value, you
should return the index of the cell currently checked.

The first thing to remember about arrays is that their indices
begin at 0. So, if there is 3 cells, their indices will be 0, 1
and 2. There would not be any cell numbered 3.

Then, remember that the amount of cells in an array can be retrieved
using the ` length` attribute. So, if your array is called ` tab` , its size can be retrieved as ` tab.length` .
Note that there is no () after ` length` . An attribute is a
sort of variable embedded in another object (here, the array).

So, the last value of an array is given by ` tab[tab.length - 1]` .

