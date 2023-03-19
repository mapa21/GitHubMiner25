THIS_FILE := $(lastword $(MAKEFILE_LIST))

make:
	./gradlew jar
	@$(MAKE) -f $(THIS_FILE) run

run:
	java -jar build/libs/githubminer-by-pirates-1.0-SNAPSHOT.jar