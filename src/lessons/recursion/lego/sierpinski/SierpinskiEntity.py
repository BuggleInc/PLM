from RemoteTurtle import *

def run():
    # BEGIN TEMPLATE
    def sierpinski(level, length):
        # BEGIN SOLUTION
        if (level >= 0):
          for i in range(3):
             sierpinski(level-1,length/2);
             forward(length);
             right(120);
        # END SOLUTION
    # END TEMPLATE
    sierpinski(getParamInt(0), getParamDouble(1))
