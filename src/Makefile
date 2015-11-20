all:
	javac $(shell find . -name \*.java)

run: all
	java pong.Main
	
test: all
	java test.Test

clean:
	rm $(shell find . -name \*.class)
