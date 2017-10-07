//RemoteSort

/* BEGIN TEMPLATE */
void combSort11() {
	/* BEGIN SOLUTION */
	int gap = getValueCount();
	int swapped;
	do {
		if (gap>1) {
			gap /= 1.3;
			if (gap == 10 || gap == 9)
				gap = 11;
		}
		swapped = 0;
		int i;
		for (i=0; i+gap<getValueCount(); i++)
			if (!isSmaller(i,i+gap)) {
				swap(i,i+gap);
				swapped =1;
			}
	} while (gap>1 || swapped);
	/* END SOLUTION */
}
/* END TEMPLATE */


void run(){
	combSort11();
}
