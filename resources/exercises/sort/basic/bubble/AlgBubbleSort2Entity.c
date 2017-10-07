//RemoteSort

/* BEGIN TEMPLATE */
void bubbleSort2()  {
	/* BEGIN SOLUTION */
	int i,j;
	for (i = getValueCount()-1; i>=0; i--) {
		for (j = 0; j<i; j++)
			if (!isSmaller(j,j+1))
				swap(j,j+1);
	}
	/* END SOLUTION */
}
/* END TEMPLATE */

void run(){
	bubbleSort2();
}
