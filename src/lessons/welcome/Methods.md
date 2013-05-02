
## Methods ##

We will now write our own methods. It somehow comes down to extending the
buggle vocabulary by learning it new tricks.

For example, we saw in previous exercise how to ask the buggle to go get the
baggle in front of it, and bring it back. If there is several baggles on the
board, and if we want to bring all of them on the bottom line, you have to
repeate this code several times, or include it in a loop. In any case, the
result may reveal unpleasant to read, and it would be better if the buggle
could obey an ` goAndGet()` order just like it understands a ` forward()` one.


<python>

The syntax to write a simple method called ` goAndGet` is the
following: 
<pre> def goAndGet():
actions();
to();
do();</pre>

<java>

The syntax to write a simple method called ` goAndGet` is the
following: 
<pre> public void goAndGet() {
actions();
to();
do();
}</pre>

<java>

The method body (between curly braces) can contain as many instructions as
you want, and any construction we saw so far (for, while, if, etc).

</java> 
<python>

The method body (the code that is indended) can contain as many instructions as
you want, and any construction we saw so far (for, while, if, etc).

</python> 
<java>

We will come back later on the exact signification of the ` public` keyword. Let's say for now that it means that everybody
can use this method.

</java> 
<java>

` void` means that the method does not return any result. We said
previously that the ` isOverBaggle()` method returns a boolean and
can thus be used as a condition in a if or a while. This means that it is
declared the following way: 
<pre> public boolean isOverBaggle() { ... }</pre>
We will introduce in next exercise how to do this kind of tricks. For now,
let's just write ` void` at this location.

</java> 
<java>

In Java, the convention is to use lower case for the first letter of a
method name and concatenate every word describing the method using a upper
case for each first letter. It is forbidden to use spaces or accentuated
letters in a method name. That is why we write "go and get" as goAndGet().

</java> 
<python>



</python> 
### Exercise goal ###
` The goal of this exercise is to write a method calledgoAndGet()` 
<python>

One particularity of this exercise is that all your code should be written in
this function, with nothing out of it. The code that calls your function will be automagically added
when you click on **which does the same than in previous exercises (move forward until over a baggle, pick it up, move back to initial position, drop baggle).Start** .

</python> 
<java>

One particularity of this exercise is that you shouldn't write directly the
code that the buggle should execute, but a method it should use. Actually,
in any previous exercise, you wrote the body of a specific method called ` run()` which gets invoked by the environment when you click on

</java> **Start** 
<java>

Don't try to define this method here, since the buggle already knows
it. This would cause trouble to the compiler, which would give you an error
message in exchange. For your information, here is the ` .run()` method of this exercise: 
<pre> public void run() {for (int i=0; i7; i++) {goAndGet();turnRight();forward();turnLeft();}
}</pre>


</java>

Your buggle will repeat 7 times (which matches the world's dimension)
sequence constituted of a call to the ` goAndGet()` method that you should
write, plus a move to get to the next row (turn right, move
forward, turn left). As you can see, the buggle will do one step right from
the right border of the world. It will bring it back to the left side since
its world is a torus.

You should now write this goAndGet() method.

