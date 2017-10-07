import javax.swing.JOptionPane;

colors = [Color(0,155,0), Color(50,155,0), Color(100,155,0), Color(140,155,0),
          Color(160,155,0), Color(180,155,0), Color(200,155,0), Color(210,155,0), Color.red]

def forward(i=1):
    if i>1:
      errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
    else:
        if not haveSeenError():
            entity.forward()
        c = getGroundColor()
        if c == Color.blue:
            if not haveSeenError():
                javax.swing.JOptionPane.showMessageDialog(None, "You fall into water.", "Test failed", javax.swing.JOptionPane.ERROR_MESSAGE);
            seenError();
        else:
          for i in range(len(colors)-1):
              if colors[i] == c:
                  if i == len(colors)-1:
                      c = colors[i]
                  else:
                      c = colors[i+1]
                  break
          setBrushColor(c)    
          brushDown()
          brushUp()
def backward(i=1):
    if i>1:
      errorMsg("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.")
    else:
      errorMsg("Sorry Dave, you cannot run backward that way. Exercising is hard enough -- please don't overplay.")
# BEGIN SOLUTION
for i in range(7):
    for side in range(4):
        for step in range(4):
            forward()
        left();
        for step in range(2):
            forward()
        right();
        for step in range(4):
            forward()
        right()
        forward()
        forward()
        left()
        for step in range(4):
            forward()
        left()
# END SOLUTION
