# BEGIN SOLUTION
def getRankOf(size):
    for rank in range(getStackSize()):
        if getPancakeRadius(rank) == size:
            return rank
    return -99 # be robust to border cases

debug = False
def showStack():
    if debug:
        s = "maxPos:"+str(maxPos)+" {"
        for rank in range(getStackSize()):
            if isPancakeUpsideDown(rank):
                s = s + "-"
            s = s + str(getPancakeRadius(rank)) + ", "
        s = s + "}"
        print(s)

maxPos = getStackSize()
keepGoing = True
while keepGoing:
    if isSelected() and debug:
        showStack()
        
    maxupside = -1
    maxupsidePos = -1
    sorted = True
    for pos in range(getStackSize()):
        if getPancakeRadius(pos) != pos+1 or isPancakeUpsideDown(pos):
            sorted = False
            
        # Search if we are in case 1 on the considered interval
        if (pos<maxPos and not isPancakeUpsideDown(pos) and (maxupside < getPancakeRadius(pos))):
                maxupside = getPancakeRadius(pos)
                maxupsidePos = pos;

    if sorted: # we are done, no need to continue
        if debug:
            print("It's sorted now. Get out of here\n");
        break;

    if maxupside != -1: # Case 1. 
        pPlus1 = getRankOf(maxupside+1)
        if maxupside == maxPos: # Case 1.C
            if debug:
                print("Case 1.C; maxupsidePos = "+str(maxupsidePos)+", maxupside = "+str(maxupside))
            if maxupsidePos+1 != maxPos:
                flip(maxupsidePos+1);
                flip(maxPos);
            maxPos -= 1
        elif pPlus1 > maxupsidePos:
            if debug:
                print("Case 1.A; maxupsidePos = "+str(maxupsidePos)+", maxupside = "+str(maxupside)+", pPlus1 = "+str(pPlus1))
            flip(pPlus1+1)
            flip(pPlus1-maxupsidePos);
        else:
            if debug:
                print("Case 1.B; maxupsidePos = "+str(maxupsidePos)+", maxupside = "+str(maxupside)+", pPlus1 = "+str(pPlus1));
            flip(maxupsidePos+1);
            flip(maxupsidePos-pPlus1);
    else: # Case 2. All pancakes are upside down.
        reverted = True   
        for pos in range(maxPos):
            if getPancakeRadius(pos)!=pos+1:
                reverted = False
                
        if reverted:
            if debug:
                print("Case 2.B")
            for i in range(maxPos):
                flip(maxPos)
                if maxPos>1:
                    flip(maxPos-1)
                showStack()
            keepGoing = False
        else:
            pPlus1 = getRankOf(getStackSize()+1)
            p = 0
            for radius in range(maxPos,0,-1):
                p = getRankOf(radius);
                if p>maxPos:
                    p=-99
                if (pPlus1!=-99 and pPlus1<p): # we've got the larger p such that p+1 is above p and both are upsideof
                    if debug:
                        print("Case 2.A; p="+str(p)+", radius="+str(radius)+", pPlus1="+str(pPlus1))
                    flip(p+1)
                    if pPlus1!=0:
                        flip(pPlus1+1)
                    radius = -1 # We're done with this iteration of the loop
                pPlus1 = p # look downward
# END SOLUTION