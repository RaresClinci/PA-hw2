.PHONY: build clean

build: Numarare.class Trenuri.class Drumuri.class Scandal.class

run-p1:
	java Numarare
run-p2:
	java Trenuri
run-p3:
	java Drumuri
run-p4:
	java Scandal

Numarare.class: Numarare.java
	javac $^
Trenuri.class: Trenuri.java
	javac $^
Drumuri.class: Drumuri.java
	javac $^
Scandal.class: Scandal.java
	javac $^


clean:
	rm -f *.class
