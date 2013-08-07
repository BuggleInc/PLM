colors = [Color.white, Color(255,240,240), Color(255,220,220), Color(255,205,205),
             Color(255,190,190), Color(255,170,170), Color(255,150,150),
             Color(255,130,130), Color(255,110,110), Color(255,45,45),
             Color(255,5,5)]

def forward(i=-1):
    if i==-1:
      entity.forward()
      c = getGroundColor()
      for i in range(len(colors)-1):
          if colors[i] == c:
              c = colors[i+1]
              break
      setBrushColor(c)    
      brushDown()
      brushUp()
    else:
      errorMsg("Sorry Dave, I cannot let you use forward with argument")
def backward(i=-1):
    if i==-1:
      entity.backward()
    else:
      errorMsg("Sorry Dave, I cannot let you use backward with argument")
# BEGIN SOLUTION
for i in range(10):
    for side in range(4):
        for step in range(8):
            forward()
        left()
# END SOLUTION
