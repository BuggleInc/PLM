//RemoteSort

/* BEGIN TEMPLATE */
void bubbleSort3()  {
	/* BEGIN SOLUTION */
	int i,j;
	for (i = getValueCount()-1; i>=0; i--) {
		int swapped = 0;
		for (j = 0; j<i; j++) {
			if (!isSmaller(j,j+1)) {
				swap(j,j+1);
				swapped=1;
			}
		}
		if (!swapped) {
			return;
		}
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

void run(){
	bubbleSort3();
}
