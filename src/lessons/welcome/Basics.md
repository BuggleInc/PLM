## Instructions ##
Congratulations! You just wrote your first program! You got the idea now:
programming is nothing more than giving simple instructions to the computer that
blindly apply them. The main difficulty is to explain stuff to someone as stupid
as a computer...

Programs are mainly suites of method calls, which are no more than a list of
simple order given to the machine. It is very similar to a recipe stating .
In your programs, such built instructions are called functions or methods, and you
should add parenthesis to invoke them, as in     nameOfTheMethod()

Java want to have the instructions separated by semi-columns (;).
The previous example would thus be written in a similar way:

    meltTheChocolatePieces();
    addSugar();
    coolMix();
    serve();

Python want to have the instructions
separated by either semi-columns (;) or by new lines. The previous example would thus
be written the following way.

    meltTheChocolatePieces()
    addSugar()
    coolMix()
    serve()

It could also be written in the following way, but it's generally considered as a bad
practice to group several instructions on the same line since it greatly hinders the
readability.

    meltTheChocolatePieces(); addSugar(); coolMix(); serve()

Of course, these specific methods do not exist in Java, but it may be possible
to define them by yourself (we'll see later how to define your how methods).

Of course, these specific methods do not exist in Python, but it may be possible
to define them by yourself (we'll see later how to define your how methods).

For now,
we'll simply go for the buggle instructions. There is a method for each button of the
interactive control panel. To achieve the same effect than the *forward* button
(making the buggle moving one step forward), you need to write the following in the
editor:     forward();
    forward()
Likewise, to achieve the same effect than the *backward* , *turn left* and *turn right* buttons, you need to use respectively:     backward();
    turnLeft();
    turnRight();
    backward()
    turnLeft()
    turnRight()
The *mark* button is a bit particular, since it correspond to two
methods: the first one moves the pen up while the second moves it down.     brushDown();
    brushUp();
    brushDown()
    brushUp()

The buggle offers other methods, that are presented from the "Help/about
this world" menu and will be introduced on need.

### Exercise goal ###

