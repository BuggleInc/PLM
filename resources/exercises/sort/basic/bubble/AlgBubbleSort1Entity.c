//RemoteSort

/* BEGIN TEMPLATE */
void bubbleSort()  {
	/* BEGIN SOLUTION */
	int swapped;
	do {
		swapped = 0;
		int i;
		for (i=0; i<getValueCount()-1; i++)
			if (!isSmaller(i,i+1)) {
				swap(i,i+1);
				swapped =1;
			}
	} while (swapped);
	/* END SOLUTION */
}
/* END TEMPLATE */

void run(){
	bubbleSort();
}
