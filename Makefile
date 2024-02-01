CC = javac
RUNTIME = java
ENTRY = Main

### Project Sources ###
SRC_FILES = $(wildcard *.java)
CLASS_FILES = $(patsubst %.java,%.class,$(SRC_FILES))

all: $(CLASS_FILES)

run: all
	$(RUNTIME) $(ENTRY)

clean:
	@rm $(CLASS_FILES)

%.class: %.java
	$(CC) $^
