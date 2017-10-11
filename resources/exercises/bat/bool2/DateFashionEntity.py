def dateFashion(you, date):
	if (you <= 2 or date <= 2):
		return 0
	elif (you >= 8 or date >= 8):
		return 2
	else:
		return 1
