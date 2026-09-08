def run():
    # BEGIN TEMPLATE
    def step(rule, colors):
        # BEGIN SOLUTION
        for i in range( len(colors) ):
            if getGroundColor() == colors[i]:
                if rule[i] == 'L':
                    left()
                elif rule[i] == 'R':
                    right()
                else:
                    seenError("Unknown command associated to i=%d: %s" %(i,rule[i]))
                newrank = (i+1) % (len(colors))
                #log("Switch from %s to %s ; %d->%d\n" % (getGroundColor(), colors[ newrank ], i, newrank))
                setBrushColor(colors[ newrank ])
                brushDown()
                brushUp()
                #log("Mark cell to %s and apply %d %s\n" %(getGroundColor(),i,rule[i]))
                forward()
                return
        seenError("Cannot find current color (%s) in my table\n" %(getGroundColor()))
        # END SOLUTION
    # END TEMPLATE
    
    allColors = [Color.white, Color.black, Color.blue, Color.cyan, Color.green, Color.orange, Color.red, Color.gray, Color.magenta, Color.darkGray, Color.pink, Color.lightGray]
    
    rule=getParamString(1)
    colors = []
    for i in range( len(rule) ):
        colors.append(allColors[i])
    
    for i in range(getParamInt(0)):
        step(rule,colors)
        stepDone()
