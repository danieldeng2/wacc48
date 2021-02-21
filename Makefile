all:
	./gradlew jar

clean:
	rm -rf build *.s

.PHONY: all clean
