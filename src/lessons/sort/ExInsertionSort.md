# Insertion algorithms and variations #
This exercise allows you to experiment with the insertion sort and its major
variations. ## InsertionSort ##
This sorting algorithm is quite simple to understand and write, even if it
is not as efficient as possible. Its asymptotic complexity is in O(n2), but
it is more efficient in practice (linear in best case, ie when the array is
already sorted, and N2/4 on average).

The idea is to traverse all elements of the array, and to insert each of
them into its proper position in the already sorted part of the array. When
we look at an element x, the situation is the following: any elements to the
left of the array are already sorted, and we have to insert x at its
position in the array.

Once this is done, the situation is the following:

The pseudo-code of this algorithm is thus the following:     For each i in [1,len]
    store the value of i in a variable val
    copy the cell i-1 into i if i-1 contains a value bigger than val
    copy the cell i-2 into i-1 if i-2 contains a value bigger than val
    copy the cell i-3 into i-2 if i-3 contains a value bigger than val
    copy the cell i-4 into i-3 if i-4 contains a value bigger than val
    ...
    copy val into the last cell copied above
Naturally, you should use a loop to write the big permutation within the
given loop. Writing it this way would be really ... counter-productive. ## ShellSort ##
This algorithm is named after its author, Donald Shell, who published it in
1959. It can be seen as an application of the CombSort idea (let elements
having a long path to travel take shortcuts) to the insertion sort (CombSort
is a variation of BubbleSort). Instead of comparing adjacent values during
the insertion sort, it compares values separated by a bigger gap. The bigger
the gap, the faster the elements are moved to their final destination, but
also the less precise is this move. It is thus mandatory to apply the
algorithm with a serie of decreasing gaps. At the last step, when the gap is
1, InsertionSort is used, but onto an array which is almost already sorted
by previous steps.

Donald Shell propose as initial value of the gap, and
then to divide it by 2 at each step. The pseudo-code is thus the following:     gap=len/2
    while gap>0:
    apply InsertionSort, comparing i-gap and i, then i-2gap and i-gap, then i-3gap and i-2gap, etc.

Just like in CombSort, the sequence of values taken by the gap is crucial
for Shell sort performance. In some rare pathological cases, the sequence we
used above can lead to a O(n^2) performance. Other sequences were proposed:
the Hibbard's increments of 2k − 1 lead to a complexity of O(n^(3/2)) in
worst cases. Pratt's increments 2^i3^j lead to a O(nlog(n)log(n) performance
in worst cases. The existance of a sequence leading to a O(n log(n)) was
precluded by Poonen, Plaxton, and Suel. Thanks to this performance,
ShellSort is a valid candidate for array of several hundred thousands when
correctly implemented.

In our case, the array are ways too small to benefit of these
optimizations. If you ever need to do so, take as initial gap the biggest
value of the targeted serie still smaller than the array size, and then use
decreasing values of the serie.

Interesingly enough, determining the best gap sequence for shell sort turns
into a contemporary research issue in computer science. For example, an
article of 2001 introduces the following sequence, which seems to be optimal
in practice for arrays of size up to 10^5: {1, 4, 10, 23, 57, 132, 301, 701,
1750} (Marcin Ciura, Best Increments for the Average Case of Shellsort, 13th
International Symposium on Fundamentals of Computation Theory, LNCS 2001;
Vol. 2138).

If you've always wondered what computer science researchers do nowadays,
here is part of the answer: They improve fundamental algorithms so that
others can write efficient programs.

## Other variation of insertion sort ##
TreeSort builds a binary search tree to sort them. It leads to a O(n log(n))
on average, but O(n^2) in worst cases. We won't study this algorithm here
since unterstanding its behavior requires to know what a binary tree is,
what is beyond our present goals.

There is other variations over the insertion sort, such as PatienceSort
which builds piles of values and sort each pile afterward. This algorithm
presents a 0(n log(n)) timing worst case and a 0(n) space
complexity. LibrarySort (proposed in 2004) also trades a bit space in
exchange for time since it provide a time complexity of O(n log(n)) but
needs to store some more data.

Wikipedia provides a detailled description of all these algorithms we cannot
present here because of time constraints.

