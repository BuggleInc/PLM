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

    do:
    For each i in [0,len-2]
    If cells i and i+1 must be swapped, do it
    while we swapped something during last traversal
## BubbleSort2 ##

If you look carefully at the behavior of BubbleSort, a first easy
optimization appears: after one traversal, the last element of the array
must be the biggest of all since the traversal moved it up like a bubble to
its position. More generally, after N traversal, we know that the N last
elements of the array are already sorted. It is thus not necessary to
compare them again during the subsequent traversals. For now, we will have
as many traversal as there is in the array.

The pseudo-code of the BubbleSort2 algorithm is the following:

    For all i in [len-2,0] (traversing from biggest to smallest)
    For all j in [0, i]
    If cells j and j+1 must be swapped, do it

When we run this algorithm, it is quite disappointing to see that it runs
approximately at the same speed than the basic version of BubbleSort. This
is a graphical effect only since only value changes are graphically
represented. Since this variation avoids some useless comparisons, it does
exactly the same amount of swaps that the basic version. It is thus quite
logical that the graphical interface draws this version at the same pace
than the base version. But the statistics on the amount of reads show that we
saved about the fourth of the amount of reads, which is not bad.

From the asymptotic complexity point of view, there is absolutely no
difference: this variation is still in O(n^2) on average (our gain is only
on the constant term, ignored when computing the asymptotic complexity).

## BubbleSort3 ##

Let's now reintroduce the little optimization we removed at previous step:
if a traversal does not swap any element, it means that the array is already
sorted.

The pseudo-code of the BubbleSort3 algorithm is the following:

    For all i in [len-2,0] (traversing from biggest to smallest)
    For all j in [0, i]
    If cells j and j+1 must be swapped, do it
    If traversal on j did not swap anything, return from the function

This optimization is even more disappointing: it only provide a gain of a few
percents on the amount of reads over BubbleSort2.

## CocktailSort ##

To improve further the algorithm, we need to look closer its behavior. One
can notice that big elements are moved very quickly in position while small
ones move very slowly to their destination. They are thus traditionally
referred to as "rabbits" and "turtles" respectively for big fast values and
small slow ones.

To help the turtles moving faster, the cocktail sort traverse alternatively
the array from right to left and from left to right. Here is the
pseudo-code:

    Do
    For all i in [0,len-2], do:
    if i and i+1 must be swapped, do it
    For all i in [len-2,0] (downward), do:
    if i and i+1 must be swapped, do it
    while at least one of the traversal swapped an element

One can see that cocktail sort achieves exactly the same amount of swaps
than the bubble sort, but improves slightly on read amount. It is however
still worse than BubbleSort2 to that extend.

## CocktailSort2 ##

We will now apply to CocktailSort the same optimization than BubbleSort2 did
to BubbleSort. We must remember the limits of the array part not being
sorted yet, and traverse it alternatively from left to right and from right
to left:

    beg=0; end=len-2
    do
    For all Pour i in [beg,end], do:
    If cells i and i+1 must be swapped, do it
    end--
    For all Pour i in [beg,end] (downwards), do:
    If cells i and i+1 must be swapped, do it
    beg++
    while at least one of the traversal swapped an element
## CocktailSort3 ##

Even if the asymptotic complexity of CocktailSort2 is the same than the one
of BubbleSort, it seem to perform better in practice. It is even possible to
improve a bit further by stopping it if the first traversal didn't found
anything to swap, without achieving the downwards traversal. Likewise, we
can stop if the upward traversal found something to swap, but not the
downwards one.

## CombSort ##

We saw that CocktailSort improve a bit for turtles (i.e. small values near to
the end of the array), but it is still possible to achieve better. ComboSort
comes down to providing them a short cut: instead of comparing adjacent
values, we compare values separated by a gap bigger than 1. That way,
turtles we traverse cells at each traversal. Naturally, we have
to apply the algorithm with decreasing gaps, and finish with to
ensure that the array is correctly sorted afterward. Choosing the right gap
and how to decrease it is a difficult question, but in practice, dividing it
by 1.3 after each traversal leads to good performance. Here is the
corresponding pseudo-code:

    gap = len;
    do
    if gap>1 then
    gap = gap / 1.3
    i = O
    while i+gaplen do:
    if i and i+gap must be swapped, do it
    increase i by the gap
    while the gap is bigger than 1 or the last traversal swapped at least one pair

This algorithm was invented by Wlodek Dobosiewicz in 1980, and later
rediscovered and popularized by Stephen Lacey and Richard Box, who described
it in Byte Magazine in April 1991.

## CombSort11 ##

The authors of this algorithm observed that the performance is increased if
we make sure that the last values of the gap are (11, 8, 6, 4, 3, 2, 1)
rather than (9, 6, 4, 3, 2, 1) or (10, 7, 5, 3, 2, 1). Rework the code of
CombSort to ensure just after the gap update that if it is 9 or 10, we
should use 11 instead.

## GnomeSort ##

The Gnome sort is similar to insertion sort, but the elements are moved in
position by a serie of swaps just like in bubble sort. It is named after the
supposed behavior of garden gnomes when they sort flower pots. Here is a
description of the algorithm by its author:

Gnome Sort is based on the technique used by the standard Dutch Garden Gnome
(Du.: tuinkabouter). Here is how a garden gnome sorts a line of flower
pots. Basically, he looks at the flower pot next to him and the previous
one; if they are in the right order he steps one pot forward, otherwise he
swaps them and steps one pot backwards. Boundary conditions: if there is no
previous pot, he steps forwards; if there is no pot next to him, he is
done.

