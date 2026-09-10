from RemoteHanoi import *

def run():
    # BEGIN TEMPLATE
    def move3(height, src, mid, dst):
        # Your code here
        # BEGIN SOLUTION
        if (height>0):
            move3(height-1, src, dst, mid)
            move(src,dst)
            move(src,dst)
            move(src,dst)
            move3(height-1, mid, src, dst)
        # END SOLUTION
    # END TEMPLATE
    move3(getSlotSize(getParamInt(0))//3, getParamInt(0),getParamInt(1),getParamInt(2))
