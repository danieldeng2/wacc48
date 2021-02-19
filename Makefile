# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler
# Locations
ANTLR_DIR	:= antlr_config
SOURCE_DIR	:= src
OUTPUT_DIR	:= build

# Tools
ANTLR	:= antlrBuild
RM	:= rm -rf

all:
	cd $(ANTLR_DIR) && ./$(ANTLR)
	./gradlew jar

clean:
	$(RM) $(OUTPUT_DIR) $(SOURCE_DIR)/main/java/antlr *.s

.PHONY: all clean
