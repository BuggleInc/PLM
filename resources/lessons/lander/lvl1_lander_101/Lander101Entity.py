# BEGIN TEMPLATE
def step():
# BEGIN SOLUTION
    if getSpeedY() < -9:
        setDesiredThrust(4)
    else:
        setDesiredThrust(3)
# END SOLUTION
# END TEMPLATE 

while isFlying():
  step()
  simulateStep()
