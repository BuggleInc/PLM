def getIndication():
	if isOverMessage():
		return readMessage()[0]
	else:
		return ' '
	
# BEGIN SOLUTION
def doR(): turnRight(); forward()
def doL(): turnLeft();  forward()
def doI(): turnBack();  forward()

danceOneStep = { 
  'R':doR,
  'L':doL,
  'I':doI,
  'A':forward(1),
  'B':forward(2),
  'C':forward(3),
  'D':forward(4),
  'E':forward(5),
  'F':forward(6),

  'Z':backward(1),
  'Y':backward(2),
  'X':backward(3),
  'W':backward(4),
  'V':backward(5),
  'U':backward(6),
}


while getIndication() != ' ':
	danceOneStep[getIndication()]()
# END TEMPLATE
