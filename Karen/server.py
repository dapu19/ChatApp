import socket
import threading
import csv
import inference

# Listen for TCP connections on port 65432 on any interface.
HOST= '0.0.0.0'
PORT = 2346


def main():
    # Create a TCP server socket.
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((HOST, PORT))
    s.listen(1)

    while True:
        # Wait for a connection from a client.
        print("listening")
        conn, addr = s.accept()

        # Handle the session in a separate thread.
        print("found connection")
        Session(conn).start();

# Session is a thread that handles a client connection.
class Session(threading.Thread):
    def __init__(self, conn):
        threading.Thread.__init__(self)
        self.conn = conn

    def run(self):
        while True:
            # Read a string from the client.
            line = (self.conn.recv(256)).decode()
            if line == '':
                # No more data from the client.  We're done.
                break

            # Convert the line to all caps and send it back to the client.
            # self.conn.sendall(line.upper())
            line = line[:-1]
            print(str(line))

            with open('data/Pre_responce_q.csv', mode='r') as infile:
                reader = csv.reader(infile)
                AnswerDict = {rows[0].lower(): rows[1] for rows in reader}
            if str(line).lower() in AnswerDict:
                self.conn.sendall((AnswerDict[str(line).lower()] + "\r").encode())
            else:
                self.conn.sendall((inference.inference(str(line))["answers"][0] + "\r").encode())


            break


        # We're done with this connection, so close it.
        self.conn.close()


if __name__ == '__main__':
    main()
