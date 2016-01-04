all:
	javac $(shell find . -name \*.java)

runP1: all
	java pong.Main 7777

runP2: all
	java pong.Main 7778 0:0:0:0:0:0:0:0 7777

runP3: all
	java pong.Main 7779 0:0:0:0:0:0:0:0 7777

runP4: all
	java pong.Main 7780 0:0:0:0:0:0:0:0 7777

run: all
	java pong.Main 7777

test: all
	java test.Test

clean:
	rm $(shell find . -name \*.class)
