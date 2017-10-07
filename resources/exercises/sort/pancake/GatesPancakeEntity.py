# BEGIN SOLUTION

def getRankOf(size):
	for rank in range(getStackSize()):
		if getPancakeRadius(rank) == size:
			return rank
	return -99 # be robust to border cases

def isFree(pos):
	if pos == -99:
		return False
	radius = getPancakeRadius(pos)
	if pos>0 :
		nextRadius = getPancakeRadius(pos-1)
		if nextRadius == radius-1 or nextRadius == radius+1:
			return False	
	if pos<getStackSize()-1:
		nextRadius = getPancakeRadius(pos+1)
		if nextRadius == radius-1 or nextRadius == radius+1:
			return False	
	return True

def isFirst(pos):
	if pos == -99:
		return False
	radius = getPancakeRadius(pos)
	if pos>0 :
		nextRadius = getPancakeRadius(pos-1)
		if nextRadius == radius-1 or nextRadius == radius+1:
			return False	
	if pos<getStackSize()-1:
		nextRadius = getPancakeRadius(pos+1)
		if nextRadius == radius-1 or nextRadius == radius+1:
			return True	
	return False

def isLast(pos):
	if pos == -99:
		return False
	radius = getPancakeRadius(pos)
	if pos<getStackSize()-1:
		nextRadius = getPancakeRadius(pos+1)
		if nextRadius == radius-1 or nextRadius == radius+1:
			return False	
	if pos>0 :
		nextRadius = getPancakeRadius(pos-1)
		if nextRadius == radius-1 or nextRadius == radius+1:
			return True	
	return False

def blockLength():
	pos = 0
	radius = getPancakeRadius(pos)
	o = getPancakeRadius(pos+1) - radius
	if o != -1 and o != 1:
		print("Asked to compute the block length, but there is no block at the top of the stack. The length is then 1, but you are violating a precondition somehow")
		return 1
	while pos < getStackSize()-1 and getPancakeRadius(pos+1) == radius + o:
		pos += 1
		radius += o
	return pos+1

debug = False	
if debug:
	print("{")
	for rank in range(getStackSize()): 
		print(""+getPancakeRadius(rank)+", ")
	print("}\n")
		
while True:
	tRadius = getPancakeRadius(0)
	posTPlus  = getRankOf(tRadius+1) # returns -99 if non-existent, that is then ignored
	posTMinus = getRankOf(tRadius-1); 
	posT = 0
			
	if debug:
		println("t Radius: "+str(tRadius))
		for rank in range(getStackSize()):
			print("["+str(rank)+"]="+str(getPancakeRadius(rank))+"; ")
			if isFree(rank):
				print("Free;")
			else:
				print("NON-free;")
				
			if isFirst(rank):
				print("First;")
			else:
				print("NON-first;")
				
			if isLast(rank):
				print("last;")
			else:
				print("NON-last;")


			if rank == posTPlus:
				print("t+1; ")
			if rank == posTMinus:
				print("t-1; ");
			if (rank == posT):
				print("t;" );

			print("\n")
						
	if isFree(posT):
		if isFree(posTPlus):    # CASE A: t and t+o free 			
			if debug:
				println("case A+")
			flip(posTPlus)
		elif isFree(posTMinus): # CASE A: t and t-o free 
			if debug:
				println("case A-")
			flip(posTMinus)
		
		elif isFirst(posTPlus): # CASE B: t free, t+o first element
			if debug:
				println("case B+")
			flip(posTPlus)
		elif isFirst(posTMinus): # CASE B: t free, t-o first element 
			if debug:
				println("case B-")
			flip(posTMinus)

		elif posTPlus != -99 and posTMinus != -99: # CASE C: t free, but both t+o and t-o are last elements 
			if debug:
				println("case C")
			flip(min(posTPlus,posTMinus) )
			flip(min(posTPlus,posTMinus) - 1)
			flip(max(posTPlus,posTMinus) + 1)
			flip(min(posTPlus,posTMinus) - 1)
		else: 					
			if debug:
				println("case Cbis")
			flip(max(posTPlus,posTMinus) + 1)
			flip(max(posTPlus,posTMinus) )
				
	else: # t is in a block
		if blockLength() == getStackSize(): # Done!
			if tRadius != 1: # all reverse
				flip(getStackSize())
			break
				
		if isFree(posTPlus): # CASE D: t in a block, t+1 free 
			if debug:
				println("case D+")
			flip(posTPlus)
		elif isFree(posTMinus): # CASE D: t in a block, t-1 free 
			if debug:
				println("case D-")
			flip(posTMinus)
		elif isFirst(posTPlus): # CASE E: t in a block, t+1 first element 
			if debug:
				println("case E+")
			flip(posTPlus)

		elif isFirst(posTMinus): # CASE E: t in a block, t-1 first element 
			if debug:
				println("case E-")
			flip(posTMinus)

		elif isLast(posTPlus) and posTPlus != 1: # CASE F+: t in a block, t+1 last element 
			if debug:
				println("case F+")
			flip(blockLength())
			flip(posTPlus + 1)
			newPos = getRankOf(tRadius)
			if newPos>0:
				flip(newPos)
		elif isLast(posTMinus) and posTMinus != 1: # CASE F-: t in a block, t-1 last element 
			if debug:
				println("case F-")
			flip(blockLength())
			flip(posTMinus + 1)
			newPos = getRankOf(tRadius)
			if (newPos>0):
				flip(newPos)
		else:
			k = blockLength()-1
			o = getPancakeRadius(1) - tRadius
			pos = getRankOf(tRadius+(k+1)*o)
			if isFree(pos) or isFirst(pos):
				if debug:
					println("case G")
				flip(k+1)
				flip(pos)
			else:
				if debug:
					println("case H")
				flip(pos+1)
				flip(getRankOf(tRadius+k*o))
# END SOLUTION