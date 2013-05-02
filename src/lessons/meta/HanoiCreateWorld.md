
# Creating a new JLM world #
Welcome to the JLM Meta-Lesson. Its goal is to teach you how to write
new universes in JLM. For that, we will reimplement the Hanoi world
(available in the recursion lesson) step-by-step. This lesson covers quite
advanced topics and suppose that you are fluent in Java and confident with the use
of the JLM framework already. If you are not used to the framework already, you
probably want to take another lesson before, like the welcome one or
(maybe more fun) the maze one.

Every JLM universe is composed of 4 main components:   
  
**The world** : it contains the state of the universe.  
**The view** : it allows to draw the world and the entities on
the screen.  
**The entity ancestor** : the code written by the student will
derive from this class, which contains some state specific to the
entity, and every accessor to let the entity interact with its world.  
**The entity interactive control panel** : this is the little
panel displayed under the world view in the JLM interface. It allows
to interactively control the selected entity. As you can see from the
existing universes, this is optional, and if you don't provide any,
the panel will remain blank, preventing the users from interactively
controlling the entities.  

In addition, worlds are used by exercises, themselves sorted in lessons.   
  
**Lessons**

In the future, we would like to have richer exercise ordering schema, where
the requirements to fulfill to be allowed to try an exercise could be more complex.
The dependencies would probably turn into a graph instead of a chain.
But this is still to be done.

The main element of each lesson is a class classically called Main, which simply
contains a constructor adding each exercise of the lesson in row.

  
**Eachexercise** object is in charge of instantiating the worlds used, set
them up (adding walls and baggles in BuggleWorld, or changing the elevation and
adding lamps in the LightBotWorld, etc), and populate them with entities correctly
setup (location, color, etc).  
**Most of the exercises also provide a specificanswer entity** able to solve the exercise.
It serve both to compute the initial content of the code editor, and to compute the
objective world. We will come back on this in a latter exercise of this meta-lesson.  

The very first element you want to write in a new JLM universe is a
partial world implementation including the internal state. In subsequent exercises, we
will complete the World class to provide entities ways to interact and modify their world,
and we will also implement the other elements of the universe.

Before you jump in writing your World implementation, you should understand the big JLM
picture, and how worlds are used internally.


## JLM worlds big picture ##
As you know, every JLM exercise can contain one or several worlds, each
containing one or several entities. The code written by the student is
executed in the entities, which must interact with their world to change
it from its initial state to its goal state.

This multiplication of worlds and entities is used to test the student
code in several conditions. It can thus be parallelized to test cases, aiming
at full testing coverage of the student code.

Technically, in a given exercise, for every world accessible from the
relevant combobox, there is three World objects. They live in jlm.lesson.Exercise: 
<pre> protected World [] initialWorld;
protected World [] currentWorld;
protected World [] answerWorld;</pre>
The line number of the combobox gives the index to use in these arrays.
Each ` initialWorld` is created by the exercise constructor.

` currentWorld` is the one
displayed in the "World" tab of the interface. At the beginning, it is a plain copy
of the initialWorld, but it gets modified when the student uses the interactive
controls or when the program gets run. The "Reset" button reset it to be a perfect
copy of the initial world.

` answerWorld` is the one displayed by the "Objective" world. Basically, it's a copy
of the initial world, on which we let the specific answer entity of this exercise run.
This default behavior can be overloaded by exercises, but you'll probably never have
to do so. We will come back on this in a subsequent exercise.


### Implementing basic Worlds ###
The internal use of Worlds hidden, but the important point is that every World object
has extend the jlm.universe.World class and define the following methods and constructors:   
  
**a copy constructor** 
<pre> which is used to copy initial worlds into currentWorld and answerWorld.
Its argument must be of the exact same type than the class itself, not Object (this is because we use
Java introspection mechanism to search for such a constructor of the class).
For example, if you create a TotoWorld, your copy constructor must be declared as this:public TotoWorld(TotoWorld other) {...} // Correct
public TotoWorld(Object other) {...} // FALSE
public TotoWorld(World other) {...} // also FALSE</pre>
` The content of this constructor is usually a simple call to thesuper(World w)` constructor, but
this constructor cannot be omitted.  
**One or severalregular constructors** ` initializing the state of the object. They will be used
by the exercise constructor to instantiate your world. Since you usually write both the world and the lessons,
you are completely free to specify the parameters you want to your constructor. It should use thesuper(String name)`

Optionally, you may also want to use the ` constructor to setup the very basic elements of your world.setDelay(int delay)` method to change the initial
animation delay. For example, the SortingWorld set this to 1 to speed animations up. This is the
delay in milliseconds between each animation step during a continuous run.

  
**Areset() method**

The length of this method naturally depends on the complexity of your world state. In hanoi, there is
only 3 slots containing a list of disc so that will be quite easy, but this can be quite complicated for
complex worlds such as BuggleWorld.

  

### Allowing the graphical rendering of Worlds ###
Some specific steps naturally must be taken so that JLM can graphically display your world. Usually, you must
write a specific class extending jlm.universe.WorldView, and provide some protected methods so that the View can
retrieve the state to display. In this particular exercise, the view is provided (you will write you own one
in next exercise), so you only have to implement the right accessor. 
## Implementing your HanoiWorld ##
The main task of this exercise is to modify the provided template to store the state of the world. Since the exercise
provides a view and automatically instantiate your world, you cannot change the instantiation constructor, nor the
rendering accessor (see the code template for there prototype). But you are completely free about how you actually
store the state in your object.

During the implementation of the Hanoi world, I changed my internal representation several times, to adapt to
the code needs. My first try was only simply three ArrayList ` , for each slots, while the final design used
an inner class called HanoiSlot, containing an integer array and providing friendly functions such aspush()` ` ,pop()` ` andtop()`

You probably want to add 3 fields to your World, one for each slot (we will never try to have worlds with more
than 3 slots), store the content of each slot in the instantiation constructor, and retrieve the content of a
specific slot in the rendering accessor ( ` Integer[] values(Integer i)` ).

That seems quite a lot of code to write compared to the other JLM exercises, but at the end, my HanoiWorld
implementation is less than 100 lines long, which is not that much.

