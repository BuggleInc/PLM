//RemotePancake

int blockLength();
int isLast(int pos);
int isFirst(int pos);
int isFree(int pos);
int getRankOf(int size);

int min(int a, int b){
	if(a<b){
		return a;
	}else{
		return b;
	}
}

int max(int a, int b){
	if(a>b){
		return a;
	}else{
		return b;
	}
}

/* BEGIN HIDDEN */
int getRankOf(int size) {
	int rank;
	for (rank=0;rank<getStackSize();rank++)
		if (getPancakeRadius(rank) == size)
			return rank;
	return -99; // Well, be robust to border cases
}

int isFree(int pos) {
	if (pos == -99)
		return 0;
	int radius = getPancakeRadius(pos);
	if (pos>0) {
		int nextRadius = getPancakeRadius(pos-1);
		if (nextRadius == radius-1 || nextRadius == radius+1)
			return 0;
	}
	if (pos<getStackSize()-1) {
		int nextRadius = getPancakeRadius(pos+1);
		if (nextRadius == radius-1 || nextRadius == radius+1)
			return 0;
	}
	return 1;
}

int isFirst(int pos) {
	if (pos == -99)
		return 0;
	int radius = getPancakeRadius(pos);
	if (pos>0) {
		int nextRadius = getPancakeRadius(pos-1);
		if (nextRadius == radius-1 || nextRadius == radius+1)
			return 0;
	}
	if (pos<getStackSize()-1) {
		int nextRadius = getPancakeRadius(pos+1);
		if (nextRadius == radius-1 || nextRadius == radius+1)
			return 1;
	}
	return 0;
}

int isLast(int pos) {
	if (pos == -99)
		return 0;
	int radius = getPancakeRadius(pos);
	if (pos<getStackSize()-1) {
		int nextRadius = getPancakeRadius(pos+1);
		if (nextRadius == radius-1 || nextRadius == radius+1)
			return 0;
	}
	if (pos>0) {
		int nextRadius = getPancakeRadius(pos-1);
		if (nextRadius == radius-1 || nextRadius == radius+1)
			return 1;
	}
	return 0;
}
int blockLength() {
	int pos = 0;
	int radius = getPancakeRadius(pos);
	int o = getPancakeRadius(pos+1) - radius;

	if (o != -1 && o != 1) {
		printf("Asked to compute the block length, but the step o is %d instead of +1 or -1. The length is then 1, but you are violating a precondition somehow\n",o);
		return 1;
	}

	while (pos < getStackSize()-1 && getPancakeRadius(pos+1) == radius + o) {
		pos++;
		radius += o;
	}
	return pos+1;
}
int debug=0; // 0: silence; 1: which cases; 2: all details
/* END HIDDEN */

/* BEGIN TEMPLATE */
void solve() {
	/* BEGIN SOLUTION */
	/* cruft to search for an instance exercising all transformations */
	int doneA=0;
	int doneB=0;
	int doneC=0;
	int doneD=0;
	int doneE=0;
	int doneF=0;
	int doneG=0;
	int doneH=0;
	int* origSizes = (int*)malloc(sizeof(int)*getStackSize());
	int i;
	for (i=0;i<getStackSize();i++)
		origSizes[i] = getPancakeRadius(i);
	/* end of this cruft */

	int stackSize = getStackSize();

	if (debug>0) {
		printf("{\n");
		int rank;
		for (rank=0; rank < stackSize; rank++){
			printf("%d\n",getPancakeRadius(rank));
		}
		printf("}\n");
	}

	while (1) {
		int tRadius = getPancakeRadius(0);
		int posTPlus  = getRankOf(tRadius+1); // returns -99 if non-existent, that is then ignored
		int posTMinus = getRankOf(tRadius-1);
		int posT = 0;

		if (debug>1) {
			printf("t Radius: %d\n",tRadius);
			int rank;
			for (rank=0; rank < stackSize; rank++) {
				printf("[%d]=%d; ",rank,getPancakeRadius(rank));

				if (isFree(rank))
					printf("free;");
				else
					printf("NON-free;");

				if (isFirst(rank))
					printf("first; ");
				else
					printf("NON-first; ");

				if (isLast(rank))
					printf("last; ");
				else
					printf("NON-last; ");


				if (rank == posTPlus)
					printf("t+1; ");
				if (rank == posTMinus)
					printf("t-1; ");
				if (rank == posT)
					printf("t;" );

				printf("\n");
			}
		}

		if (isFree(posT)) {
			if (isFree(posTPlus)) { /* CASE A: t and t+o free */
				if (debug>0)
					printf("Case A+\n");
				flip(posTPlus);
				doneA = 1;
			} else if (isFree(posTMinus)) { /* CASE A: t and t-o free */
				if (debug>0)
					printf("Case A-\n");
				flip(posTMinus);
				doneA = 1;

			} else if (isFirst(posTPlus)) { /* CASE B: t free, t+o first element */
				if (debug>0)
					printf("Case B+\n");
				flip(posTPlus);
				doneB = 1;
			} else if (isFirst(posTMinus)) { /* CASE B: t free, t-o first element */
				if (debug>0)
					printf("Case B-\n");
				flip(posTMinus);
				doneB = 1;

			} else if (min(posTPlus,posTMinus) != -99) { /* CASE C: t free, but both t+o and t-o are last elements */
				if (debug>0)
					printf("Case C\n");
				flip(min(posTPlus,posTMinus) );
				flip(min(posTPlus,posTMinus) - 1);
				flip(max(posTPlus,posTMinus) + 1);
				flip(min(posTPlus,posTMinus) - 1);
				doneC = 1;

			} else {
				if (debug>0)
					printf("Case Cbis\n");
				flip(max(posTPlus,posTMinus) + 1);
				flip(max(posTPlus,posTMinus) );
				doneC = 1;
			}

		} else { // t is in a block
			if (blockLength() == stackSize) { // Done!
				if (tRadius != 1) // all reverse
					flip(stackSize);
				if (doneA && doneB && doneC && doneD && doneE && doneF && doneG && doneH && wasRandom()) {
					printf("BINGO! This instance is VERY interesting as it experiences every cases of the algorithm.\nPLEASE REPORT IT. PLEASE DONT LOSE IT.\n");
					printf("{\n");
					int rank;
					for (rank=0; rank < stackSize; rank++)
						printf("%d, ",origSizes[rank]);
					printf("}\n");
				}
				free(origSizes);
				return;
			}

			if (isFree(posTPlus)) {          /* CASE D: t in a block, t+1 free */
				if (debug>0)
					printf("Case D+\n");
				flip(posTPlus);
				doneD = 1;

			} else if (isFree(posTMinus)) {  /* CASE D: t in a block, t-1 free */
				if (debug>0)
					printf("Case D-\n");
				flip(posTMinus);
				doneD = 1;

			} else if (isFirst(posTPlus)) {  /* CASE E: t in a block, t+1 first element */
				if (debug>0)
					printf("Case E+\n");
				flip(posTPlus);
				doneE = 1;

			} else if (isFirst(posTMinus)) { /* CASE E: t in a block, t-1 first element */
				if (debug>0)
					printf("Case E-\n");
				flip(posTMinus);
				doneE = 1;

			} else if (isLast(posTPlus) && posTPlus != 1) { /* CASE F+: t in a block, t+1 last element */
				doneF = 1;
				if (debug>0)
					printf("Case F+\n");
				flip(blockLength());
				flip(posTPlus + 1);
				int newPos = getRankOf(tRadius);
				if (newPos>0)
					flip(newPos);

			} else if (isLast(posTMinus) && posTMinus != 1) { /* CASE F-: t in a block, t-1 last element */
				doneF = 1;
				if (debug>0)
					printf("Case F-\n");
				flip(blockLength());
				flip(posTMinus + 1);
				int newPos = getRankOf(tRadius);
				if (newPos>0)
					flip(newPos);
			} else {
				int k = blockLength()-1;
				int o = getPancakeRadius(1) - tRadius;
				int pos = getRankOf(tRadius+(k+1)*o);
				if (isFree(pos) || isFirst(pos)) {
					doneG = 1;
					if (debug>0)
						printf("Case G\n");
					flip(k+1);
					flip(pos);
				} else {
					doneH = 1;
					if (debug>0)
						printf("Case H\n");
					flip(pos+1);
					flip(getRankOf(tRadius+k*o));
				}
			}
		}
	}
	free(origSizes);
	/* END SOLUTION */
}
/* END TEMPLATE */

void run(){
	solve();
}
