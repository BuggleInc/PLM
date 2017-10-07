//RemoteSort

/* BEGIN TEMPLATE */
void cocktailSort2() {
	/* BEGIN SOLUTION */
	int swapped;
	int begin=0;
	int end=getValueCount()-2;
	do {
		swapped = 0;
		int i;
		for (i=begin; i<=end; i++)
			if (!isSmaller(i,i+1)) {
				swap(i,i+1);
				swapped =1;
			}
		end--;
		for (i=end; i>=begin; i--)
			if (!isSmaller(i,i+1)) {
				swap(i,i+1);
				swapped =1;
			}
		begin++;
	} while (swapped && end-begin>1);
	/* END SOLUTION */
}
/* END TEMPLATE */


void run(){
	cocktailSort2();
}
