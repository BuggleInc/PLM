# BEGIN SOLUTION
		PancakeWorld myWorld = (PancakeWorld) this.world;
		int stackSize = this.getStackSize();
		boolean currentPancakeAlreadySorted;
		for ( int pancakesToSort = stackSize-1 ; pancakesToSort != -1 && !myWorld.isSorted() ; pancakesToSort-- )
		{
			currentPancakeAlreadySorted= (this.getPancakeSize(pancakesToSort)==pancakesToSort+1 
					&& !this.isPancakeUpsideDown(pancakesToSort) ) ;
			if ( !currentPancakeAlreadySorted)
			{
				int indexBigPancake =-1;
				boolean found = false;
				for ( int currentPancake = 0 ; currentPancake < pancakesToSort+1 && !found; currentPancake++)
				{
					if ( this.getPancakeSize(currentPancake) == pancakesToSort+1)
					{
						indexBigPancake = currentPancake;	// gotcha !
						found = true;
					}
				}
				if ( indexBigPancake != 0 )
				{
					this.flip(indexBigPancake+1);	// putting the pancake at the top
				}	
				if ( ! ( pancakesToSort == 0) )
				{
					if ( !this.isPancakeUpsideDown(0))
					{
						this.flip(1);	// show your dark side to the world
					}
					this.flip(this.getPancakeSize(0));	// hit the bottom !
				}
				else
				{
					if ( this.isPancakeUpsideDown(0))
					{
						this.flip(1);
					}
				}

			}	
		}
# END TEMPLATE