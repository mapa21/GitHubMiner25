THIS_FILE := $(lastword $(MAKEFILE_LIST))

make:
	./gradlew jar
	make run

run:
	java -jar build/libs/githubminer-by-pirates-1.0-SNAPSHOT.jar