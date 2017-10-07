# BEGIN SOLUTION
for rank in range(getStackSize()-1,-1,-1):
	if getPancakeRadius(rank)!=rank+1 or isPancakeUpsideDown(rank) : # this pancake is not sorted yet
		indexBigPancake = -1
		for currentPancake in range(rank+1):
			if getPancakeRadius(currentPancake) == rank+1:
				indexBigPancake = currentPancake
				break
		if indexBigPancake != 0:
			flip(indexBigPancake+1)   # Move that pancake to the top
			
		if rank != 0:
			if not isPancakeUpsideDown(0):
				flip(1)                   # Show your dark side to the moon
			flip(getPancakeRadius(0)) # Move away	
		else:
			if isPancakeUpsideDown(0):
				flip(1)                   # Show your dark side to the moon
# END SOLUTION