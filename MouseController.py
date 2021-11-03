import pyautogui as pg
import socket
import sys
import threading

pg.FAILSAFE = False

sizeX, sizeY = pg.size()
sizeX -= 4
sizeY -= 4

def moveCursor(val):
    val = val.split()
    posX, posY = pg.position()
    if((posX > 4 and posX < sizeX) or (posY > 4 and posY < sizeY)):
        pg.moveRel(-float(val[1]), -float(val[0]))
    elif(posX < 4 and posY < 4):
        pg.moveTo(8, 8)
    elif(posX > sizeX and posY < 4):
        pg.moveTo(sizeX-4, 8)
    elif(posX > sizeX and posY > sizeY):
        pg.moveTo(sizeX-4, sizeY-4)
    else:
        pg.moveTo(8, sizeY-4)

def execute(command):
    if(command == 'L'):
        pg.click()
    elif(command == 'R'):
        pg.click(button='right')
    elif(command == 'M'):
        pg.click(button='middle')
    elif(command == 'U'):
        pg.scroll(20)
    elif(command == 'D'):
        pg.scroll(-20)
    else:
        moveCursor(command)

def get_ip_address():

    print("Make sure your PC and Phone is connected to same network")
    print("If connected press enter to continue")

    notConnected = True

    while(notConnected):
        input()

        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            s.connect(("8.8.8.8", 80))
            notConnected = False
        except:
            print("Your PC is not connected to any network")
            print("Please try again !!!")
            print()
            print("If connected press enter to continue")
            notConnected = True
            # pg.sleep(3)
            # sys.exit()

        if(notConnected == False):
            break
        
    return s.getsockname()[0]



HOST = get_ip_address()
PORT = 5000
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# print('IP: {0}'.format(socket.gethostbyname(HOST)))
print("IP: {0}".format(HOST))

try:
    s.bind((HOST, PORT))
except socket.error as err:
    print("Connection Failed!!!")
    sys.exit()

print('Bind Succesfull')

s.listen(10)
print('Now listening....')

while 1:
    conn, addr = s.accept()
    buf = conn.recv(1023).decode('utf-8')
    threading.Thread(target=execute, args=(buf,)).start()
    # print(buf)

# s.close()