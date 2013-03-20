## Storing and manipulating data ##

The programs we wrote so far are missing a fundamental point in computing.
Actually, it is all about processing *data* through specific *instructions* . In the buggle world, the main data are a bit hidden behind
the graphical representation, but that's no reason to never manipulate some
data explicitly. ### Data in Java ###
### Data in Python ###
In a program, you can use several of data, such as integers or strings
of chars. If you want to use a data several times, you need to store it
within a , which is a memory cell containing a value. It's not very
different from a shelve containing a book: you put your data (say '5') in the
variable (say 'length'), and you can retrieve it latter when you need it.

Java is said to be a language, which means that it is
only possible to store a value in a variable of the right type. Don't think
about storing the letters of your name into an integer variable. In other languages (such as Python)
allow you to store any kind of data in any variable without such restriction, but not in Java.

The Python language is said to , which means you can
store any type of data into a given variable. Other languages (such as Java) mandate
that each variable store only data of a given type, but there is no such
difficulties here.

To (ie, create) a variable, you just need to write its type,
a space, and the variable name. From the existing types, we can speak of *int* (for integers), *double* for dot numbers, *boolean* for
booleans (ie, values being either true or false) and *String* for char
strings. If you want, you can specify the initial value of the variable by
adding a equal sign (=) followed by the value after the declaration.

So, to create a variable named *x* intended to contain integers, one
can write:     int x;
If you want that the variable contains 5 as initial value, you should type:     int x = 5;

Later in the program, if you want to a new value to the
variable, that's really easy:     x = 3;

The syntax to create an integer variable with 4 as initial
value is the following:     int x = 4;

This quite the same story for strings, floating point
numbers and boolean values.

    boolean married=true;

(ie, creating) a variable in Python is
dead simple: you just need to give it an initial value by writing its name, the equal sign and the value.

So, to create a variable named *x* which initial value should be 5, you should type:     x = 5

Later in the program, if you want to a new value to the
variable, that's really easy:     x = 3

This quite the same story for strings, floating
point numbers and boolean values.

    

To the right of the equal symbol, you can put an expression containing constants,
variables and operations:     x = 3 + 2;
    x = 3 * x;
    greeting = "Hello "+name;
### Exercise goal ###
It is now time to do more challenging exercises, don't you think?
The objective is now to move forward until you find a baggle,
pick it up, and then move back to your initial location before dropping the
baggle. ### How to do this? ###

To solve this problem, you have to decompose it in easier sub-parts. For
example, you may want to do the following steps: 1.    Move forward until located over a baggle
1.    Pickup the baggle
1.    Move backward of the same amount of steps than done in first step
1.    Drop back the baggle

Naturally, it is impossible to do the right amount of steps backward at step
3 if you didn't count the amount of steps done in the first phase. You can
use a variable for that, which can be named .

Create a variable (of type ) before phase 1, initialize it
to 0, and each time you move one step forward, increment its value by one
( or ,
both syntaxes being equivalent).

Create a variable before phase 1, initialize it
to 0, and each time you move one step forward, increment its value by one
( ).

Then, phase 3 consists in simply creating a new integer variable initialized to 0, and do one step backward until equals , incrementing each time.

Please note that it is forbidden to use spaces in variable names. So
you can name you variable , but is not a valid name.

It's your turn now!

