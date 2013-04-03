# BuggleWorld #
This world was invented by Lyn Turbak, at Wellesley College. It is full of
Buggles, little animals understanding simple orders, and offers numerous
possibilities of interaction with the world: taking or dropping objects,
paint the ground, hit walls, etc. ## Methods understood by buggles ##
*Moving* *Moving back* *Set position* *Information on the buggle* *Set the color* *Look for a wall backward* *Set heading* *About the brush* *Get brush position* *Get the color of the brush* *Interacting with the world* *Get the color of the ground* *Drop a baggle* *Erase the message* ## Note on exceptions ##
Regular buggles throw a BuggleWallException exception if you ask them to
traverse a wall.  They throw a NoBaggleUnderBuggleException exception if you
ask them to pickup a baggle from an empty cell, or a
AlreadyHaveBaggleException exception if they already carry a baggle.  Trying
to drop a baggle on a cell already containing one throws an
AlreadyHaveBaggleException exception.

SimpleBuggles (ie, the one used in first exercises) display an error message
on problem so that you don't need to know what an exception is.

