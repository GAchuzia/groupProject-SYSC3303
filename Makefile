CC = javac
RUNTIME = java
ENTRY = Main

### Project Sources ###
SRC_FILES = $(wildcard *.java)
SRC_FILES := $(filter-out %Test.java,$(SRC_FILES))

CLASS_FILES = $(patsubst %.java,%.class,$(SRC_FILES))
TEST_FILES = $(wildcard *Test.java)
TEST_CLASS_FILES = $(patsubst %.java,%.class,$(TEST_FILES))

all: $(CLASS_FILES)
	$(info $(SRC_FILES))

run: all
	$(RUNTIME) $(ENTRY)

test: $(TEST_CLASS_FILES)
	$(RUNTIME) $^

clean:
	@rm $(CLASS_FILES)

%.class: %.java
	$(CC) $^
