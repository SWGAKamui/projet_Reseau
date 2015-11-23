all:
	javac $(shell find . -name \*.java)

runP1: all
	java pong.Main

runP2: all
	java pong.Main 0:0:0:0:0:0:0:0 7777

clean:
	rm $(shell find . -name \*.class)
