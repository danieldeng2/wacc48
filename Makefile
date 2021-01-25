# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler
# Locations
ANTLR_DIR	:= antlr_config
SOURCE_DIR	:= src
OUTPUT_DIR	:= build

# Tools

ANTLR	:= antlrBuild
RM	:= rm -rf

# the make rules

all: rules

# runs the antlr build script then attempts to compile all .java files within src
rules:
	cd $(ANTLR_DIR) && ./$(ANTLR)
	./gradlew jar

clean:
	$(RM) rules $(OUTPUT_DIR) $(SOURCE_DIR)/antlr

.PHONY: all rules clean
