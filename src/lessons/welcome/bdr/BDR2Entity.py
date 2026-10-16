from RemoteBuggle import *
def run():
    def getIndication():
    	if isOverMessage():
    		return readMessage()[0]
    	else:
    		return ' '
    	
    # BEGIN SOLUTION
    def doR(): right(); step()
    def doL(): left();  step()
    def doI(): back();  step()


    finished = False
    while not finished:
    	c = getIndication()
    	if c == 'R':
    		doR()
    	elif c == 'L':
    		doL()
    	elif c == 'I':
    		doI()
    	elif c == 'A':
    		step()
    	elif c == 'B':
    		forward(2)
    	elif c == 'C':
    		forward(3)
    	elif c == 'D':
    		forward(4)
    	elif c == 'E':
    		forward(5)
    	elif c == 'F':
    		forward(6)
    	elif c == 'Z':
    		stepBack()
    	elif c == 'Y':
    		backward(2)
    	elif c == 'X':
    		backward(3)
    	elif c == 'W':
    		backward(4)
    	elif c == 'V':
    		backward(5)
    	elif c == 'U':
    		backward(6)
    	else:        
    		finished = True



    #danceOneStep = { 
    #  'R':doR,
    #  'L':doL,
    #  'I':doI,
    #  'A':step(),
    #  'B':forward(2),
    #  'C':forward(3),
    #  'D':forward(4),
    #  'E':forward(5),
    #  'F':forward(6),
    #
    #  'Z':stepBack(),
    #  'Y':backward(2),
    #  'X':backward(3),
    #  'W':backward(4),
    #  'V':backward(5),
    #  'U':backward(6),
    #  
    #  default: log("parse error: '"+getIndication()+"'")
    #}
    #
    #
    #while getIndication() != ' ':
    #	log("Seen "+getIndication())
    #	danceOneStep[getIndication()]()
    # END SOLUTION
