
## Writing more complex programs ##
Now that we know how to draw things on the board, we'll enjoy this ability and
draw a beautiful G on the board (check Objective panel for details on what's
expected).

When you write a quite complex program, it is sometimes useful to **add comments** to simplify the code reviews afterward. Here for example, it's
quite easy to get lost in the drawing process, and you may want to add
comments like *vertical bar done* or *finished drawing the G. Time to move back to initial position* . Commenting your code is almost mandatory if you
(or someone else) want to read it afterward, although overcommenting
(describing obvious stuff) is a bad idea as the important idea get lost in the noise.


<java>

There is three types of comments in Java, instructing the compiler
to not read the text you add for humans:

</java> 
<python>

There is two types of comments in Python, instructing the compiler
to not read the text you add for humans:

</python>   
  
*  **Comments on a single line** . When the compiler encounters the symbols //,
it ignores the end of the line.  
*  **Comments on several lines** . The compiler ignores anything placed
between the symbols /* and */ even if they are placed on differing lines.  
  
  
*  **Comments on a single line** . When the compiler encounters the symbol #,
it ignores the end of the line.  
*  **Comments on several lines** . The compiler ignores anything placed
between a line beginning with ''' and the next line ending with '''.  

<java> 
<pre> 
<comment> ( methodCallReadByTheCompiler();// all this is ignored) </comment> 
<comment> ( otherCall();/* This is) </comment> 
<comment> ( also ignored */) </comment> yetAnotherCall();</pre>
</java> 
<python> 
<pre> 
<comment> ( methodCallReadByTheCompiler()# all this is ignored) </comment> 
<comment> ( otherCall()''' This is) </comment> 
<comment> ( also ignored  ''') </comment> yetAnotherCall()</pre>
</python> 
<java>

There is a third kind of comments in Java, between /** and */, which are read by a
specific program called JavaDoc to generate automatically the documentation
explaining how to use the code. These comments must follow a very precise
formalism.

</java> 
<python>

The comments on several lines are often used to document how to use the code, while others
are more used to describe how this code works.

</python>

