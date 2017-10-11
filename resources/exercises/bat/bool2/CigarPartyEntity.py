def cigarParty(cigars, isWeekend):
   return (isWeekend and cigars >= 40) or (not isWeekend and (cigars >= 40) and (cigars <= 60))
