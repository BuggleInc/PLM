# BEGIN SOLUTION 
afterBlue=0;
beforeWhite=getSize()-1;
beforeRed=getSize()-1;
while afterBlue <= beforeWhite:
	
	if (getColor(afterBlue) == BLUE):
		afterBlue += 1
	elif (getColor(afterBlue) == WHITE):
		swap(afterBlue, beforeWhite);
		beforeWhite -= 1
	else:
		swap(afterBlue, beforeWhite);
		swap(beforeRed, beforeWhite);
		beforeWhite -= 1
		beforeRed -= 1
# END SOLUTION
