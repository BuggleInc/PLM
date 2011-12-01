# BEGIN SOLUTION */
		for (int i = 1; i < getValueCount(); i++) {
			int value = getValue(i);
			int j = i;
			while ((j > 0) && (!compareTo(j-1,value))) {
				copy(j-1,j);
				j--;
			}
			setValue(j,value);
		}
		for (int i = 0; i < getValueCount(); i++) 
			sorted(i);
		checkme(); /* color everything in blue */
	}
# END TEMPLATE */
