import javax.swing.JOptionPane;

colors = [Color.blue,    Color.cyan, Color.green,  Color.yellow,
          Color.orange,  Color.red,  Color.magenta,Color.pink]

inTeerNal_Steep_Count = -3
def forward(need=1):
    global inTeerNal_Steep_Count
    for i in range(need):
      entity.forward()
      if inTeerNal_Steep_Count<0 or inTeerNal_Steep_Count%2 == 1 or (inTeerNal_Steep_Count/2)>=len(colors):
          if inTeerNal_Steep_Count < 0:
              setBrushColor(Color.lightGray)
          elif (inTeerNal_Steep_Count/2)>=len(colors):
              setBrushColor(Color.pink)
          else:
              setBrushColor(colors[(inTeerNal_Steep_Count/2)%len(colors)])
          brushDown()
          brushUp()
      inTeerNal_Steep_Count += 1
      
# BEGIN SOLUTION
forward(3)
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
