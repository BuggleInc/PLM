
# Sorting World #
This world provides tools to experiment with the sorting algorithms. It can
be used in two different ways: the first one is naturally to write the
required sorting algorithms. But it is also possible to simply use the demo
mode of each exercise to observe the behavior of sorting algorithms. It
helps understanding the differences between each of them. 
## Methods available to sorting algorithms ##


<table border=1>
	<tr>
		<td > **Method** </td>
		<td > **Action** </td>
		<td > **Cost** </td>
	</tr>
	<tr>
		<td > int getValueCount() </td>
		<td > Returns the amount of values in the array </td>
		<td > none </td>
	</tr>
	<tr>
		<td > boolean isSmaller(int i, int j) </td>
		<td > Returns true if the content of cell i is smaller than the one of cell j </td>
		<td > two reads </td>
	</tr>
	<tr>
		<td > boolean isSmallerThan(int i, int val) </td>
		<td > Returns true if the content of cell i is smaller than value val </td>
		<td > one read </td>
	</tr>
	<tr>
		<td > void swap(int i, int j) </td>
		<td > Swaps the content of cell i and the one of cell j </td>
		<td > two reads, two writes </td>
	</tr>
	<tr>
		<td > void copy(int from, int to) </td>
		<td > Copy the content of cell 'from' into the cell 'to' </td>
		<td > one read, one write </td>
	</tr>
	<tr>
		<td > int getValue(int idx) </td>
		<td > Returns the value of cell idx </td>
		<td > one read </td>
	</tr>
	<tr>
		<td > void setValue(int idx,int val) </td>
		<td > Sets cell 'idx' to the value 'val' </td>
		<td > one write </td>
	</tr>
	<tr>
		<td > void sorted(int idx) </td>
		<td > Tells that the cell 'idx' was successfully sorted (only used for display) </td>
		<td > none </td>
	</tr>
</table>

