from RemoteHanoi import *

def run():
    # BEGIN TEMPLATE
    def linearTwinHanoi(height, src,mid,dst):    
        # Your code here
        # BEGIN SOLUTION
        gather(height-1,src,mid,dst)
        move(src,mid)
        moveDouble(height-1, dst, mid, src)
        move(dst,mid)
        moveDouble(height-1, src, mid, dst)
        move(mid, src)
        moveDouble(height-1, dst, mid, src)
        move(mid, dst)
        scatter(height-1, src, mid, dst)
            
    def gather(height, src, mid, dst):
        if (height >0):
            gather(height-1,src,mid,dst)
            move(src,mid)
            moveDouble(height-1, dst,mid,src)
            move(mid,dst)
            moveDouble(height-1, src, mid, dst)
    def scatter(height, src, mid, dst):
        if (height>0):
            moveDouble(height-1, src, mid, dst)
            move(src,mid)
            moveDouble(height-1, dst, mid, src)
            move(mid,dst)
            scatter(height-1,src,mid,dst)
    def moveDouble(height, src, mid, dst):
        if (height>0):
            moveDouble(height-1, src, mid, dst)
            move(src,mid)
            move(src,mid)
            moveDouble(height-1, dst, mid, src)
            move(mid,dst)
            move(mid,dst)
            moveDouble(height-1, src, mid, dst)
        # END SOLUTION
    # END TEMPLATE
    linearTwinHanoi(getSlotSize(getParamInt(0)), getParamInt(0),getParamInt(1),getParamInt(2))