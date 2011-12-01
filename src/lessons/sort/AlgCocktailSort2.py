# BEGIN SOLUTION 
		boolean swapped;
		int begin=0;
		int end=getValueCount()-2;
		do {
			swapped = false;
			for (int i=begin; i<=end; i++)
				if (!compare(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}	
			sorted(end+1);
			end--;
			for (int i=end; i>=begin; i--)
				if (!compare(i,i+1)) {
					swap(i,i+1);
					swapped =true;
				}
			sorted(begin);
			begin++;
		} while (swapped && end-begin>1);
		checkme(); /* color everything in blue */
# END TEMPLATE 
