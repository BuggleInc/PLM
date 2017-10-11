def withoutDoubles(die1, die2, noDoubles):
	if (noDoubles and (die1 == die2)):
		if (die1 == 6):
			return 1 + die2
		else:
			return die1 + 1 + die2
	else:
		return die1 + die2
