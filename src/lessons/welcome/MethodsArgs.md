
## Methods with parameters ##
Don't you get tired of writing again and again the code to move by a fixed
amount of steps? On the other hand, writting forward2() , forward3() , forward4() , as well as backward2() , backward3() , backward4() , and so on does not really help

Luckily, it is possible to pass **parameters** to your methods. You have
to specifiy their type and name between the parenthesis after the method
name. Then, you can use them in the method body as if it were variables
defined in there, and which initial value is what the caller specified. 
<pre> double divideByTwo(double x) {return x / 2;
}</pre>

As caller, you have to specify the initial value of this "variables" between
the call's parenthesis. 
<pre> double y = divideByTwo(3.14);</pre>

If you want several parameters, you need to separate them with comas (,)
both in the declaration and calls. 
<pre> double divide(double x, double y) {return x / y;
}</pre>

<pre> double y = divide(3.14 , 1.5);</pre>

In Java, you can declare several methods of the same name as long as they
don't have the same parameter types and number (they are said to have
different **signature** ). 
<pre> int max(int x, int y) {
if (x > y) {
return x;
}
return y;
}
int max(int x, int y, int z) {
if (x > y&&x > z) {
return x;
}
if (y > z) {
return y;
}
return z;
}</pre>

Observe that we omitted the else branches of each if . It
works anyway because a return interrupts the method execution. If
we arrive to the last line of ` max(int,int)` , we know that ` x=y` because on the other case, the return of line
2 would have stopped the execution.


### Exercise goal ###
This time, you have to write a ` move(int stepCount,boolean
forward)` method which move forward of ` stepCount` if ` forward` is true, and move back of that amount of steps if the
boolean is false. The buggle will use some methods we did not introduce yet
to guess its position and orientation in order to determine the amount of
steps to do and their direction, but it is not relevant here.

This time, there is only one world, and seven buggles. But it does not
change anything for you, since the same code is used for any buggles.

The code of the method to write should not be really problematic for you.


## What's coming next? ##

You now know the very basics of Java Programming. At least, we introduced all the important
concepts, and you should be able to read most Java code by now. If you want to play safe, you
should proceed to the next exercises of this lesson to solidify your knowledge by reusing
these concepts in various simple situations. After taking them, you'll master what's called
"Tactical programming", meaning that you will master the Java syntax enough to not have any
issue with it, allowing you to focus on the fundamental problems of what you want to solve
instead of struggling with syntaxic difficulties. Some of these exercises are even fun to do ;)

If you are in a hurry and want more, you can skip these exercises and proceed directly
to more interesting challenges. For example, the *Labyrinths* lesson will teach you
about maze escaping algorithms, which are not rocket science but require several improvements
to work for any kind of maze. The *Lightbot* lesson is a little programming game where
you control a little robot wanting to light the world. Since it is not programmed in Java
but graphically, the first exercises can be used as an introduction activity to what
programming means for real beginners, but the last exercises constitute challenges and
brain teaser even to professional programmers.

