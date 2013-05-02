
# BubbleSort and variations #

Welcome to the sorting world. It allows you to experiment with the existing
sorting algorithms. Please consult the world help ("Help"->"About this
world") for more information on the available buildins for your algorithms.

In this first exercise, we will explore some of the simpler of them. The tab
"Source code" contains several sub-tabs, corresponding to the different
algorithms to write. To succeed in this exercise, your solution must sort
the array using exactly the same amount of read and write than the
solution. That is why you have to follow strictly the pseudo-code of each
algorithm.


## BubbleSort ##

Bubble sort consists in progressively moving up the smaller elements of the
array, as if they were air bubbles moving up to the surface of a liquid. The
algorithm traverse the array, and compare any pair of adjacent elements. If
two adjacent elements are wrongly sorted, they are swapped. Once the array
was completely traversed, the operation starts again from the beginning. When
no elements were sorted after a full traversal, it means that the array is
completely sorted: the algorithm can stop. Bubble sort is studied because of
its simplicity, but it is almost never used in practice because of its bad
performance (O(n^2) on average).

  
  

The pseudo-code of the BubbleSort algorithm is the following:


<pre> do:
For each i in [0,len-2]
If cells i and i+1 must be swapped, do it
while we swapped something during last traversal</pre>

