import java.awt.Color
def isGroundWhite():
    return getGroundColor().equals(java.awt.Color.white)

def estSurBlanc(): # BINDINGS TRANSLATION
    return isGroundWhite() 

# BEGIN SOLUTION
more = True
while more:
    forward()
    if isGroundWhite():
  	     more = False 
# END SOLUTION
