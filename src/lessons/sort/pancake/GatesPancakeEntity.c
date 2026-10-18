#include "../../../../target/classes/resources/langages/c/RemotePancake.h"

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

/* BEGIN SOLUTION */
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
/* END SOLUTION */

/* BEGIN TEMPLATE */
void solve() {
  /* BEGIN SOLUTION */
  int stackSize = getStackSize();

  while (1) {
    int tRadius   = getPancakeRadius(0);
    int posTPlus  = getRankOf(tRadius + 1); // returns -99 if non-existent, that is then ignored
    int posTMinus = getRankOf(tRadius - 1);
    int posT      = 0;

    if (isFree(posT)) {
      if (isFree(posTPlus)) { /* CASE A: t and t+o free */
        flip(posTPlus);
      } else if (isFree(posTMinus)) { /* CASE A: t and t-o free */
        flip(posTMinus);

      } else if (isFirst(posTPlus)) { /* CASE B: t free, t+o first element */
        flip(posTPlus);
      } else if (isFirst(posTMinus)) { /* CASE B: t free, t-o first element */
        flip(posTMinus);

      } else if (min(posTPlus, posTMinus) != -99) { /* CASE C: t free, but both t+o and t-o are last elements */
        flip(min(posTPlus, posTMinus));
        flip(min(posTPlus, posTMinus) - 1);
        flip(max(posTPlus, posTMinus) + 1);
        flip(min(posTPlus, posTMinus) - 1);

      } else {
        flip(max(posTPlus, posTMinus) + 1);
        flip(max(posTPlus, posTMinus));
      }

    } else {                            // t is in a block
      if (blockLength() == stackSize) { // Done!
        if (tRadius != 1)               // all reverse
          flip(stackSize);
        return;
      }

      if (isFree(posTPlus)) { /* CASE D: t in a block, t+1 free */
        flip(posTPlus);

      } else if (isFree(posTMinus)) { /* CASE D: t in a block, t-1 free */
        flip(posTMinus);

      } else if (isFirst(posTPlus)) { /* CASE E: t in a block, t+1 first element */
        flip(posTPlus);

      } else if (isFirst(posTMinus)) { /* CASE E: t in a block, t-1 first element */
        flip(posTMinus);

      } else if (isLast(posTPlus) && posTPlus != 1) { /* CASE F+: t in a block, t+1 last element */
        flip(blockLength());
        flip(posTPlus + 1);
        int newPos = getRankOf(tRadius);
        if (newPos > 0)
          flip(newPos);

      } else if (isLast(posTMinus) && posTMinus != 1) { /* CASE F-: t in a block, t-1 last element */
        flip(blockLength());
        flip(posTMinus + 1);
        int newPos = getRankOf(tRadius);
        if (newPos > 0)
          flip(newPos);
      } else {
        int k   = blockLength() - 1;
        int o   = getPancakeRadius(1) - tRadius;
        int pos = getRankOf(tRadius + (k + 1) * o);
        if (isFree(pos) || isFirst(pos)) {
          flip(k + 1);
          flip(pos);
        } else {
          flip(pos + 1);
          flip(getRankOf(tRadius + k * o));
        }
      }
    }
  }
  /* END SOLUTION */
}
/* END TEMPLATE */

void run(){
	solve();
}
