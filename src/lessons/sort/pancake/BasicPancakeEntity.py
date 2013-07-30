# BEGIN SOLUTION
for pancakeToSort in range(getStackSize()-1, -1, -1):
	if not getPancakeRadius(pancakeToSort)==pancakeToSort+1: # Current position is not sorted yet
		indexBigPancake = -1
		for currentPancake in range(pancakeToSort+1):
			if getPancakeRadius(currentPancake) == pancakeToSort+1:
				indexBigPancake = currentPancake
				break # Gotcha!
		if indexBigPancake != 0:
			flip(indexBigPancake+1) # move this large pancake to the top
		if pancakeToSort != 0:
			flip(getPancakeRadius(0)) # hit the bottom
# END SOLUTION