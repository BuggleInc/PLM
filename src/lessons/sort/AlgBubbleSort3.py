# BEGIN SOLUTION 
		for (int i = getValueCount()-1; i>=0; i--) {
		    boolean swapped = false;
			for (int j = 0; j<i; j++) {
				if (!compare(j,j+1)) {
					swap(j,j+1);
					swapped=true;
				}
			}
			sorted(i);
			if (!swapped) {
				checkme(); /* color everything in blue */
				return;	
			}
		}
		/* END SOLUTION */
# END TEMPLATE 
