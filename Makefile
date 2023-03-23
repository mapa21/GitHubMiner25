GRADLE_TARGET = jar

ifdef OS
	command = gradlew
else
	command = ./gradlew
endif

make:
	$(command) $(GRADLE_TARGET)
	make run

make_debug:
	$(command) $(GRADLE_TARGET)
	make run_debug

run:
	java -jar build/libs/githubminer-by-pirates-1.0-SNAPSHOT.jar

run_debug:
	java -jar build/libs/githubminer-by-pirates-1.0-SNAPSHOT.jar -d