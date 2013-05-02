
## Storing and manipulating data ##

The programs we wrote so far are missing a fundamental point in computing.
Actually, it is all about processing **data** through specific **instructions** . In the buggle world, the main data are a bit hidden behind
the graphical representation, but that's no reason to never manipulate some
data explicitly. 
<java> 
### Data in Java ###
</java> 
<python> 
### Data in Python ###
</python> In a program, you can use several *types* of data, such as integers or strings
of chars. If you want to use a data several times, you need to store it
within a *variable* , which is a memory cell containing a value. It's not very
different from a shelve containing a book: you put your data (say '5') in the
variable (say 'length'), and you can retrieve it latter when you need it. 
<java>

Java is said to be a *typed* language, which means that it is
only possible to store a value in a variable of the right type. Don't think
about storing the letters of your name into an integer variable. In other languages (such as Python)
allow you to store any kind of data in any variable without such restriction, but not in Java.

</java> 
<python>

The Python language is said to *not typed* , which means you can
store any type of data into a given variable. Other languages (such as Java) mandate
that each variable store only data of a given type, but there is no such
difficulties here.

</python> 
<java>

To *declare* (ie, create) a variable, you just need to write its type,
a space, and the variable name. From the existing types, we can speak of **int** (for integers), **double** for dot numbers, **boolean** for
booleans (ie, values being either true or false) and **String** for char
strings. If you want, you can specify the initial value of the variable by
adding a equal sign (=) followed by the value after the declaration.

</java> 
<java>

So, to create a variable named **x** intended to contain integers, one
can write: 
<pre> int x;</pre>
If you want that the variable contains 5 as initial value, you should type: 
<pre> int x = 5;</pre>


</java> 
<java>

Later in the program, if you want to *affect* a new value to the
variable, that's really easy: 
<pre> x = 3;</pre>

<java>

The syntax to create an integer variable ` x` with 4 as initial
value is the following: 
<pre> int x = 4;</pre>


</java> 
<java>

This quite the same story for strings, floating point
numbers and boolean values.

</java> 
<java> 
<pre> 
<comment> ( String name = "Martin Quinson";
double height=1.77;// in meters) </comment> boolean married=true;</pre>
</java> 
<python>

*Declaring* (ie, creating) a variable in Python is
dead simple: you just need to give it an initial value by writing its name, the equal sign and the value.

</python> 
<python>

So, to create a variable named **x** which initial value should be 5, you should type: 
<pre> x = 5</pre>


</python> 
<python>

Later in the program, if you want to *affect* a new value to the
variable, that's really easy: 
<pre> x = 3</pre>

<python>

This quite the same story for strings, floating
point numbers and boolean values.

</python> 
<python> 
<pre> 
<comment> ( firstName = "Martin"
lastName = 'Quinson'# both single and double quote work here) </comment> 
<comment> ( motto = "I never finish anyth' (but I keep trying)"# having single quote within double quote is fine) </comment> 
<comment> ( height=1.77# in meters) </comment> 
<comment> ( married=True# the contrary would be written False) </comment> </pre>
</python> 

To the right of the equal symbol, you can put an expression containing constants,
variables and operations: 
<pre> x = 3 + 2;
x = 3 * x;
greeting = "Hello "+name;</pre>

### Exercise goal ###
It is now time to do more challenging exercises, don't you think?
The objective is now to move forward until you find a baggle,
pick it up, and then move back to your initial location before dropping the
baggle. 
### How to do this? ###

To solve this problem, you have to decompose it in easier sub-parts. For
example, you may want to do the following steps:   
  
Move forward until located over a baggle  
Pickup the baggle  
Move backward of the same amount of steps than done in first step  
Drop back the baggle  

Naturally, it is impossible to do the right amount of steps backward at step
3 if you didn't count the amount of steps done in the first phase. You can
use a variable for that, which can be named ` stepAmount` .


<java>

Create a variable (of type ` int` ) before phase 1, initialize it
to 0, and each time you move one step forward, increment its value by one
( ` stepAmount = stepAmount + 1;` or ` stepAmount++;` ,
both syntaxes being equivalent).

</java> 
<python>

Create a variable before phase 1, initialize it
to 0, and each time you move one step forward, increment its value by one
( ` stepAmount = stepAmount + 1` ).

</python> 

Then, phase 3 consists in simply creating a new integer variable ` doneSteps` initialized to 0, and do one step backward until ` doneSteps` equals ` stepAmount` , incrementing ` doneSteps` each time.

Please note that it is forbidden to use spaces in variable names. So
you can name you variable ` stepAmount` , but ` step
Amount` is not a valid name.

It's your turn now!

