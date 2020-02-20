import socket
import csv
import colorama
import sys
import inference
import pickle


# Create a socket object
s = socket.socket()


port = 4467

s.bind(('', port))
# Define the port on which you want to connect





s.listen()

while True:
    print("Listening on port %d" % port)

    c, addr = s.accept()

    recievedQuestion = c.recv(1024)
    recievedQuestion = pickle.loads(recievedQuestion)

    print("connection found from %s" % addr)

    # Read in csv
    with open('data\\Pre_responce_q.csv', mode='r') as infile:
        reader = csv.reader(infile)
        AnswerDict = {rows[0].lower(): rows[1] for rows in reader}
        print(AnswerDict)

    # Interactive mode
    colorama.init()

    # Specified model
    if len(sys.argv) >= 2 and sys.argv[1]:
        checkpoint = inference.hparams['out_dir'] + str(sys.argv[1])
        inference.hparams['ckpt'] = checkpoint
        print("Using checkpoint: {}".format(checkpoint))

    # QAs

    question1 = recievedQuestion.lower()
    if question1 in AnswerDict:
        answer = AnswerDict[question1]
        c.sendall(pickle.dumps(answer))
    else:
        answers = inference.inference_internal(recievedQuestion)[0]
        if answers is None:
            print(colorama.Fore.RED + "! Question can't be empty" + colorama.Fore.RESET)
            c.sendall(pickle.dumps("! Question can't be empty"))
        else:
            for i, _ in enumerate(answers['scores']):
                print("{}- {}{} [{}] {}{}{}".format(colorama.Fore.GREEN if answers['scores'][i] == max(answers['scores']) and answers['scores'][i] >= inference.score_settings['bad_response_threshold'] else colorama.Fore.YELLOW if answers['scores'][i] >= inference.score_settings['bad_response_threshold'] else colorama.Fore.RED, answers['answers'][i], colorama.Fore.RESET, answers['scores'][i], colorama.Fore.BLUE, answers['score_modifiers'][i] if inference.score_settings['show_score_modifiers'] else '', colorama.Fore.RESET))
                c.sendall(pickle.dumps(("{}- {}{} [{}] {}{}{}".format(colorama.Fore.GREEN if answers['scores'][i] == max(answers['scores']) and answers['scores'][i] >= inference.score_settings['bad_response_threshold'] else colorama.Fore.YELLOW if answers['scores'][i] >= inference.score_settings['bad_response_threshold'] else colorama.Fore.RED, answers['answers'][i], colorama.Fore.RESET, answers['scores'][i], colorama.Fore.BLUE, answers['score_modifiers'][i] if inference.score_settings['show_score_modifiers'] else '', colorama.Fore.RESET))))

    c.close()
