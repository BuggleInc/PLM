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
methods to teleport the buggle to a specific position ( and similar), this approach will be a pain to implement.

The simplest is to store the whole color pattern in an *array* . ## Arrays in Java ##
An array is a sequence of positions in which one can store values of the
same kind (one value per cell). It is thus a sequence of values of the same
kind:

T is the array's name, T[0] is the name of the first cell, T[1] the name of
the second cell, T[2] the third one, etc. And yes, the first cell in
numbered T[0] and the last one of an array of size N is T[N-1].

We can use an integer variable to access with T[i] to the array's
cells: when the value of is 0, then T[i] accesses T[0], when the
value of is 10, then T[i] accesses T[10]. is said to be
the indice in the array T. ## Initializing an array ##
Let be an array of 10 integer elements. It can then be
initialized this way:     for (int i = 0; i10; i++) {
    T[i] = 3;
    }
can be used just like a variable. We can set a new value:     T[i] = 78;

We can access its value:     x = T[i];

We can test this value:     if (T[i] > 0 ) {
    // instructions...
    }

### Declaring an array ###
An array can be declared the following way:     int[] T;

means that the elements of the array are of type integer; is the name of the array and means that we
are speaking of an array. It is also possible to declare the same array the
following way. Both writings are equivalent, but the first one is often
prefered in Java.     int T[];

### Allocating an array ###
Declaring an array only reserve the name for
later use. But the array is not initialized yet: it does not have any
value. What would mean if we didn't say that the array is
at least 5 cells long?

First and foremost, we have to give a value to :     T = new int[10];
means that we want to create something, and means that it is an array of 10 integer values. In
return, an array of 10 integer cells is created in memory, and the variable reference this array.

The size of an array is fixed and cannot be changed after the creation of
the array. To know the size of a array, we can consult the
value of the variable .

It is forbidden to write:     int T[10]; // WRONG!!!
You are required to use the instruction. On the other hand,
you perfectly can specify the size with a variable .     T = new int[i];
In this case, the array's size will be set to the value of *gets called* . If the variable changes afterward,
it won't change the array's size. #### Declaration and allocation ####
    int[] T = new int[10];
We declare and allocate the array on the same line. #### Declaration and initialization ####
    int[] T = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
We declare, allocate and initialize the array on the same line. To know the
size of the array to allocate, the compiler counts the provided values. This
code is equivalent to:     int[] T = new int[10];
    T[0] = 1;
    T[1] = 2;
    ...
    T[9] = 10;
It is also equivalent to:     int[] T = new int[10];
    for (int i=0; iT.length; i++) {
    T[i] = i+1;
    }
### Arrays and method parameters ###
It is perfectly ok to pass an array to a method as a parameter. The method
must have a prototype similar to:     void myMethod(int[] values) {
    // do something
    }
On the caller side, that also very simple:     int[] tab = new int[10];
    // initalize the values
    myMethod(tab);

We can also have methods returns arrays as results:     int[] otherMethod() {
    int[] result = new int[10];
    // do something
    return result;
    }
## Goal of this exercise ##
The method that you should write must declare an array of
colors ( ) and allocate it. Beware, the first world is
6x6, but this is not the case of the others. Use the method to retrieve the amount of lines in the
current world.

Once the array allocated, we have to fill it. For each cel of the row, read
the ground color (with ), and store it in the
right cell of the array.

Once hte array initialized, you have to reapply the pattern on every rows,
for example by excuting times a method written
specifically for this.

You're up.

