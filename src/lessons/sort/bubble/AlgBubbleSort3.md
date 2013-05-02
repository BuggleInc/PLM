
# BubbleSort and variations #

## BubbleSort3 ##

Let's now reintroduce the little optimization we removed at previous step:
if a traversal does not swap any element, it means that the array is already
sorted.

  
  

The pseudo-code of the BubbleSort3 algorithm is the following:


<pre> For all i in [len-2,0] (traversing from biggest to smallest)
For all j in [0, i]
If cells j and j+1 must be swapped, do it
If traversal on j did not swap anything, return from the function</pre>
  
  

This optimization is even more disappointing: it only provide a gain of a few
percents on the amount of reads over BubbleSort2.

