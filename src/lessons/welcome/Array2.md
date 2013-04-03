# Knitting, Arrays and modulos #
This exercise is similar to the previous one: you have to reproduce the
color pattern of the first cell into the other ones.

The first difference is that the world is bordered of walls: you thus have
to slightly modify your trajectory to ensure that the buggle does not crash
into a wall. The simpler for that is to handle the first cell out of the loop and do only steps in
the loop.

The other difference is that the offset to apply between columns is not
fixed, but written on the first cell of each column. To get the info as an
integer, we can use:     int offset = Integer.parseInt(readMessage())
gets the message on the ground as a String, while transforms a String into an integer by it.

Then, to pick the right color, the easier is to use the (modulo) operator. For example, allows to
retrieve teh th cell of an array of size with an offset of .

You're up.

