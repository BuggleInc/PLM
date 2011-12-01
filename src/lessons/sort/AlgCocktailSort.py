# BEGIN SOLUTION 
		boolean swapped;
		do {
			swapped = false;
			for (int i=0; i<getValueCount()-1; i++)
				if (!compare(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}	
			for (int i=getValueCount()-2; i>=0; i--)
				if (!compare(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}	
		} while (swapped);
		checkme(); /* color everything in blue */
# END TEMPLATE 
