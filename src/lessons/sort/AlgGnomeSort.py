# BEGIN SOLUTION */
		int i=0;
		while (i<getValueCount()-1) {
			if (compare(i,i+1))
				i++;
			else {
				swap(i,i+1);
				i--;
			}
			if (i==-1)
				i=0;
		}
		checkme(); /* color everything in blue */
# END TEMPLATE */
