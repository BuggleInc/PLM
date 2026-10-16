from RemotePancake import *
def run():
    # BEGIN SOLUTION
    def getRankOf(radius):
        for rank in range(getStackSize()):
            if getPancakeRadius(rank) == radius:
                return rank
        return -1

    def joined(above, below):
        """Whether the pancakes at these consecutive ranks are joined: p above p+1, or -(p+1) above -p"""
        top = -getPancakeRadius(above) if isPancakeUpsideDown(above) else getPancakeRadius(above)
        bottom = -getPancakeRadius(below) if isPancakeUpsideDown(below) else getPancakeRadius(below)
        return bottom == top + 1

    # Joined pancakes are never separated: each block of joined pancakes behaves as a single pancake.
    # The pancakes from maxPos to the bottom are joined to the floor, and never moved again.
    maxPos = getStackSize()
    while True:
        while maxPos > 0 and getPancakeRadius(maxPos - 1) == maxPos and not isPancakeUpsideDown(maxPos - 1):
            maxPos -= 1
        if maxPos == 0:
            return

        largestUp = -1
        for rank in range(maxPos):
            if not isPancakeUpsideDown(rank) and getPancakeRadius(rank) > largestUp:
                largestUp = getPancakeRadius(rank)

        if largestUp != -1: # Case 1: p is the block ending with the largest rightside up pancake
            p = getRankOf(largestUp)
            if largestUp == maxPos: # Case 1.C
                flip(p + 1)
                flip(maxPos)
            else:
                pPlus1 = getRankOf(largestUp + 1) # bottom of the next block, which is upside down
                if pPlus1 > p: # Case 1.A
                    flip(pPlus1 + 1)
                    flip(pPlus1 - p)
                else: # Case 1.B
                    flip(p + 1)
                    flip(p - pPlus1)
        else: # Case 2: all pancakes are upside down
            # Case 2.A: the largest radius such that -(radius+1) is above -radius without being joined to it
            radius = maxPos - 1
            while radius > 0 and getRankOf(radius + 1) >= getRankOf(radius) - 1:
                radius -= 1
            if radius > 0:
                flip(getRankOf(radius + 1) + 1)
                flip(getRankOf(radius))
            else: # Case 2.B: the blocks form -I. Once per block, flip them all, then all but the bottom one
                blocks = 1 + sum(1 for rank in range(1, maxPos) if not joined(rank - 1, rank))
                for i in range(blocks):
                    flip(maxPos)
                    bottom = maxPos - 1
                    while bottom > 0 and joined(bottom - 1, bottom):
                        bottom -= 1
                    if bottom > 0:
                        flip(bottom)
    # END SOLUTION
