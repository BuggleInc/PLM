//RemoteSort

/* BEGIN TEMPLATE */
void insertionSort() {
	/* BEGIN SOLUTION */
	int i;
	for (i = 1; i < getValueCount(); i++) {
		int value = getValue(i);
		int j = i;
		while ((j > 0) && (!isSmallerThan(j-1,value))) {
			copy(j-1,j);
			j--;
		}
		setValue(j,value);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */



void run(){
	insertionSort();
}
