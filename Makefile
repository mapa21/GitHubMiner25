GRADLE_TARGET = jar

ifdef OS
	command = gradlew
else
	command = ./gradlew
endif

make:
	$(command) $(GRADLE_TARGET)
	make run

debug:
	$(command) $(GRADLE_TARGET)
	make run-debug

run:
	java -jar build/libs/githubminer-by-pirates-1.0-SNAPSHOT.jar

run-debug:
	java -jar build/libs/githubminer-by-pirates-1.0-SNAPSHOT.jar -d