GRADLE_TARGET = jar

ifdef OS
	command = gradlew
else
	command = ./gradlew
endif

make:
	$(command) $(GRADLE_TARGET)
	make run

run:
	java -jar build/libs/githubminer-by-pirates-1.0-SNAPSHOT.jar