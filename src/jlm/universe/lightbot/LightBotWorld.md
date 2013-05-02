
# LightBotWorld #
This world introduces a little programming puzzle which can somehow be used to introduce programmation to non-reading kids since it is programmed graphically.
The goal of each board is to light up all the lights. Your robot understands the following orders: 

<table border=1>
	<tr>
		<td > **Order** </td>
		<td > **Meaning** </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/forward.png" /> </td>
		<td > **Move forward** Cannot be done if the destination cell is of another height than source cell </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/jump.png" /> </td>
		<td > **Jump forward** Can only be done if the destination cell is one step higher than source cell, or if destination is lower than source. Cannot be used for plain moves. </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/left.png" /> </td>
		<td > **Turn left** . </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/right.png" /> </td>
		<td > **Turn right** . </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/light.png" /> </td>
		<td > **Switch the light** . Turn it on if it was off, and off if it was on. No effect if the cell does not contain any light. </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/f1.png" /> </td>
		<td > **Call function 1** . </td>
	</tr>
	<tr>
		<td > <img src="resources/lightbot/f2.png" /> </td>
		<td > **Call function 2** . </td>
	</tr>
</table>

Please note that this world is not completely suited to small kids since the main difficulty comes from the fact that your are highly limited in the amount of instructions you can give to your robot. Advanced levels thus require to write sound functions, and are often above the capacities of small kids.

This game is heavily inspirated from a flash game of the same name, which can for example be played on kongregate.com. It was written by Danny Yaroslavski (Coolio-Niato), the original idea being of Matt Chase.

