def forward(i=1):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
    entity.forward()
def backward(i=1):
    if i>1:
        errorMsg("Sorry Dave, I cannot let you use forward with an argument in this exercise. Use a loop instead.")
    entity.backward()
def isOverOrange():
    return getGroundColor() == Color.orange

def estSurOrange(): # BINDINGS TRANSLATION
    return isOverOrange()

# BEGIN SOLUTION
baggle = 0
orange = 0
while 2 * baggle != orange + 1:
    forward()
    if isOverBaggle():
        baggle += 1
    if isOverOrange():
        orange += 1
# END SOLUTION 
