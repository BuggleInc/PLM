# BEGIN TEMPLATE
def alarmClock(day, vacation):
  # BEGIN SOLUTION
  if not vacation:
    if (day >= 1 and day <= 5):
      return '7:00'
    else:
      return '10:00'
  else:
    if (day >= 1 and day <= 5):
      return '10:00'
    else:
      return 'off'

# END SOLUTION
# END TEMPLATE
