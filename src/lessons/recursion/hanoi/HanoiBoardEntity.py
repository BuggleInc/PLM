from RemoteHanoi import *

def run():
    # BEGIN TEMPLATE
    def hanoi(height, src,other,dst):
        
        # Your code here
        # BEGIN SOLUTION
        if height != 0:
            hanoi(height-1, src,dst,other);
            move(src,dst);
            hanoi(height-1, other,src,dst);
        # END SOLUTION
    # END TEMPLATE
    hanoi(getSlotSize(getParamInt(0)), getParamInt(0),getParamInt(1),getParamInt(2))