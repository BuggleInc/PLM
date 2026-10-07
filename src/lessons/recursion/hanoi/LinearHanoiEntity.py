from RemoteHanoi import *

def run():
    # BEGIN TEMPLATE
    def linearHanoi(height, src,mid,dst):
        
        # Your code here
        # BEGIN SOLUTION
        if height != 0:
            linearHanoi(height-1, src,mid,dst)
            move(src,mid)
            linearHanoi(height-1, dst,mid,src)
            move(mid,dst)
            linearHanoi(height-1, src,mid,dst)
        # END SOLUTION
    # END TEMPLATE
    linearHanoi(getSlotSize(getParamInt(0)), getParamInt(0),getParamInt(1),getParamInt(2))