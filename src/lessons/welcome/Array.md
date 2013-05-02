
# Knitting and Arrays #
The goal of this exercise is to reproduce the pattern of the first row in
the other rows with a shift of one cell (see the Objective tab for
details). The biggest difference between this exercise and the other we had
on patterns is that you have to read the pattern (on first row) before
reproducing it. You cannot do otherwise because the same code will be
executed on three different worlds, each of them having its specific
pattern.

One solution is to read the next cell, and go copy it in position before
comming back to read the second cell. But since it is forbiden to use the
methods to teleport the buggle to a specific position ( ` setPos()` and similar), this approach will be a pain to implement.

The simplest is to store the whole color pattern in an **array** . 
## Arrays in Java ##
An array is a sequence of positions in which one can store values of the
same kind (one value per cell). It is thus a sequence of values of the same
kind:



<table border=0>
	<tr>
		<td > T </td>
		<td > 3 </td>
		<td > 7 </td>
		<td > 13 </td>
		<td > 8 </td>
		<td > 15 </td>
		<td > 6 </td>
		<td > 11 </td>
		<td > 10 </td>
		<td > 9 </td>
	</tr>
</table>

T is the array's name, T[0] is the name of the first cell, T[1] the name of
the second cell, T[2] the third one, etc. And yes, the first cell in
numbered T[0] and the last one of an array of size N is T[N-1].

We can use an integer variable *i* to access with T[i] to the array's
cells: when the value of *i* is 0, then T[i] accesses T[0], when the
value of *i* is 10, then T[i] accesses T[10]. *i* is said to be
the indice in the array T. 
## Initializing an array ##
Let ` T` be an array of 10 integer elements. It can then be
initialized this way: 
<pre> for (int i = 0; i10; i++) {
T[i] = 3;
}</pre>
` T[i]` can be used just like a variable. We can set a new value: 
<pre> T[i] = 78;</pre>

We can access its value: 
<pre> x = T[i];</pre>

We can test this value: 
<pre> if (T[i] > 0 ) {
// instructions...
}</pre>


### Declaring an array ###
An array can be declared the following way: 
<pre> int[] T;</pre>

` int` means that the elements of the array are of type integer; ` T` is the name of the array and ` []` means that we
are speaking of an array. It is also possible to declare the same array the
following way. Both writings are equivalent, but the first one is often
prefered in Java. 
<pre> int T[];</pre>


### Allocating an array ###
Declaring an array ` T` only reserve the name ` T` for
later use. But the array is not initialized yet: it does not have any
value. What would ` T[4]` mean if we didn't say that the array is
at least 5 cells long?

First and foremost, we have to give a value to ` T` : 
<pre> T = new int[10];</pre>
` new` means that we want to create something, and ` int[10]` means that it is an array of 10 integer values. In
return, an array of 10 integer cells is created in memory, and the ` T` variable reference this array.

The size of an array is fixed and cannot be changed after the creation of
the array. To know the size of a ` T` array, we can consult the
value of the variable ` T.length` .

It is forbidden to write: 
<pre> int T[10]; // WRONG!!!</pre>
You are required to use the ` new` instruction. On the other hand,
you perfectly can specify the size with a variable ` i` . 
<pre> T = new int[i];</pre>
In this case, the array's size will be set to the value of ` i` ` whennew` **gets called** . If the variable changes afterward,
it won't change the array's size. 
#### Declaration and allocation ####

<pre> int[] T = new int[10];</pre>
We declare and allocate the array on the same line. 
#### Declaration and initialization ####

<pre> int[] T = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };</pre>
We declare, allocate and initialize the array on the same line. To know the
size of the array to allocate, the compiler counts the provided values. This
code is equivalent to: 
<pre> int[] T = new int[10];
T[0] = 1;
T[1] = 2;
...
T[9] = 10;</pre>
It is also equivalent to: 
<pre> int[] T = new int[10];
for (int i=0; iT.length; i++) {
T[i] = i+1;
}</pre>

### Arrays and method parameters ###
It is perfectly ok to pass an array to a method as a parameter. The method
must have a prototype similar to: 
<pre> void myMethod(int[] values) {
// do something
}</pre>
On the caller side, that also very simple: 
<pre> int[] tab = new int[10];
// initalize the values
myMethod(tab);</pre>

We can also have methods returns arrays as results: 
<pre> int[] otherMethod() {
int[] result = new int[10];
// do something
return result;
}</pre>

## Goal of this exercise ##
The ` run()` method that you should write must declare an array of
colors ( ` Color[]` ) and allocate it. Beware, the first world is
6x6, but this is not the case of the others. Use the ` getWorldHeight()` method to retrieve the amount of lines in the
current world.

Once the array allocated, we have to fill it. For each cel of the row, read
the ground color (with ` getGroundColor()` ), and store it in the
right cell of the array.

Once hte array initialized, you have to reapply the pattern on every rows,
for example by excuting ` getWorldHeight()` times a method written
specifically for this.

You're up.

