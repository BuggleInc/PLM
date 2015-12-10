import javax.swing.JOptionPane;

colors = [Color.blue,    Color.cyan, Color.green,  Color.yellow,
          Color.orange,  Color.red,  Color.magenta,Color.pink]

step = -3
def forward(i=1):
    global step
    if i>1:
      errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
    else:
      entity.forward()
      if step<0 or step%2 == 1 or (step/2)>=len(colors):
          if step < 0:
              setBrushColor(Color.lightGray)
          elif (step/2)>=len(colors):
              setBrushColor(Color.pink)
          else:
              setBrushColor(colors[(step/2)%len(colors)])
          brushDown()
          brushUp()
      step += 1
def backward(i=1):
    if i>1:
      errorMsg("Sorry Dave, I cannot let you use backward with an argument in this exercise. Use a loop instead.")
    else:
      entity.backward()
      
# BEGIN SOLUTION
forward()
forward()
forward()
left()
for i in range(8): 
    forward()   
    right()
    forward()
    left()
        
right()
forward()
forward()
forward()
# END SOLUTION
