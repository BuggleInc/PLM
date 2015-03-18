# BEGIN SOLUTION
swapped = True

while swapped:
    swapped = False 

    # Check all pancakes
    for rank in range(0, getStackSize() - 1):
        # When the pancake above is bigger than the next one...
        if getPancakeRadius(rank) > getPancakeRadius(rank + 1):
            swapped = True # We have to check all the pancakes again next time

            flip(rank + 2); # Flip all the pancakes to get the two on top
            flip(2); # Flip the two pancakes to sort
            flip(rank + 2); # Flip all the pancakes back in place


# END SOLUTION