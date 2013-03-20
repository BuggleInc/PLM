## Buggle Dance Revolution 2 ##
### (BDR2) ###
BDR is cool, but it's a bit chaotic. First, the buggles giggle in any
directions, and then the code you had to write to let them move is rather
difficult to read. Here is a new BDR world where the buggle will dance a
gentle circle. We will benefit this tranquillity to clean up a bit our code
thanks to the new constructs we will introduce. ### conditionals ###
The hardest part of previous code is certainly the conditional
cascading. Somewhere in your code, you certainly had something similar to:     if (getIndication() == 'R') {
    turnRight();
    forward();
    } else if (getIndication() == 'L') {
    turnLeft();
    forward();
    } else if (getIndication() == 'I') {
    turnBack();
    forward();
    /* other else if */
    } else {
    return;
    }
When you review this code, it may not be clear at the first glance that it
is simply a choice with 4 branches depending on the value of
getIndication(). To improve this, we will use a construct, which Java syntax is the following: *switch (expression* *) { casefirstValue* *:whatToDoIfExpressionEqualsFirstValue();* *break; casesecondValue* *:whatToDoIfExpressionEqualsSecondValue();* *break; casethirdValue* *:whatToDoIfExpressionEqualsThirdValue();* *break; /* as much similar cases as you want */ default:whatToDoIfExpressionDoesNotEqualsAnySeenValues();*     }
Observe that each branch of a must be ended by a . If you forget this, the machine keeps going and execute
the next branch in the list after the branch it jumped to. There is even
some *rare* cases where this behavior reveals helpful.

It is then possible to rewrite previous BDR code in a cleaner way using the construct:

    switch (getIndication()) {
    case 'R':
    turnRight();
    forward();
    break;
    case 'L':
    turnLeft();
    forward();
    break;
    case 'U':
    turnBack();
    forward();
    break;
    default:
    return;
    }
## Variables shared between methods ##
Another issue in your code is that it begins to be a bit long to be written
as a single method. We would like to split it up in two methods: *    would take care of achieving a single dance step
*    would take care of the dance as a whole. It would do the
steps while we didn't encounter a cell not asking any further move.

The difficulty is to make sure that keeps informed that there is no further dance step to achieve. The
simpler solution is to have a boolean function visible from both methods
indicating whether there is more steps to do or if we're done. For that, we
have to write out the following of any method:     boolean moreMusic = true;

Note that it is possible to write variable declarations out of any methods,
but that instructions must be in a method. In Java such variables are called *fields* .

Then, the must be changed to update this variable to when there is nothing more to do. For that, simply add before any .

It is then possible to use the following method:     public void run() {
    while (moreMusic)
    danseOneStep();
    }
### Exercise goal ###

You don't have to write the method since the buggle already
know it. If you put it anyway, the compiler will complain about this
multiple definition without noticing that both declarations match. Simply
declare the variable and the method.

Here are the ground indications to use for BRD2. Note that we can now move a
bugle up to 6 cells in one dance step. 

In any other case, you should stop

When you program works again, proceed to next exercise.

