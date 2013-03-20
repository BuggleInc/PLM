# Selection Sort #
In this exercise we will implement another classical algorithm: selection
sort.

The idea is simply to select for each cell of the array the smallest value
from the part not already sorted. Thus for the first cell, it takes the
smallest value over the whole array. For the second one, it takes the second
smallest value, which is the smallest value from the cell not already
sorted.

More generally, for the cell N, it looks the cell M in [n;len] containing
the smallest possible value of the interval. Then, it swaps the content of
cell N with the one of cell M. ## Existing variations ##
Another classical algorithm which idea is based on the selection of good
elements is HeapSort, but it uses a heap data structure which we did not see
yet. Simply remember that HeapSort provides a O(n log n) performance in
worst case, which is why it is a very interesting algorithm in practice.

