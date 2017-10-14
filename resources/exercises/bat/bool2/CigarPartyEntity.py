# BEGIN TEMPLATE
def cigarParty(cigars, isWeekend):
   # BEGIN SOLUTION
   return (isWeekend and cigars >= 40) or (not isWeekend and (cigars >= 40) and (cigars <= 60))

# END SOLUTION
# END TEMPLATE
