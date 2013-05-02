
# Knitting, Arrays and modulos #
This exercise is similar to the previous one: you have to reproduce the
color pattern of the first cell into the other ones.

The first difference is that the world is bordered of walls: you thus have
to slightly modify your trajectory to ensure that the buggle does not crash
into a wall. The simpler for that is to handle the first cell out of the ` for` loop and do only ` getWorldHeight()-1` steps in
the loop.

The other difference is that the offset to apply between columns is not
fixed, but written on the first cell of each column. To get the info as an
integer, we can use: 
<pre> int offset = Integer.parseInt(readMessage())</pre>
` readMessage()` gets the message on the ground as a String, while ` Integer.parseInt()` transforms a String into an integer by *reading* it.

Then, to pick the right color, the easier is to use the ` %` (modulo) operator. For example, ` (i + 5) % size` allows to
retrieve teh ` i` th cell of an array of size ` size` with an offset of ` 5` .

You're up.

