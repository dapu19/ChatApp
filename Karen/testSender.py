import pickle
import socket

question = "How are you?"


notify = pickle.dumps(question)
s = socket.socket()
s.bind(('', 7745))
s.connect(("localhost", 4467))
s.sendall(notify)


while True:
    print("Listening on " + str(7745))

    c, addr = s.accept()

    print("Notification recieved")

    notification = c.recv(1024)
    notification = pickle.loads(notification)
    print(notification.getData())

    c.sendall(pickle.dumps(notification))


    s.close()


s.close()