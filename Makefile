all:
	./gradlew jar

clean:
	rm -rf build *.s


pluginlib:
	./gradlew clean jar
	cp ./build/libs/wacc-1.0-SNAPSHOT.jar ./plugin/lib/
	cp ./build/libs/wacc-1.0-SNAPSHOT.jar ./plugin/src/main/resources/shell/shell.jar

.PHONY: all clean
