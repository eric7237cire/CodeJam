CC = g++
CFLAGS = -Wall -g
OLD = -g -O3
EXEC_NAME = star_wars.exe boolean_tree.exe triangle_area.exe
INCLUDES =
LIBS =
OBJ_FILES = star_wars.o simplex.o util.o
INSTALL_DIR = /usr/bin

CXX_FILES = $(wildcard *.cxx)

all : pockets.exe test_util.exe


#.depend: depend



depend: $(CXX_FILES)
				rm -f ./.depend
				$(CC) $(CFLAGS) -MM $^>>./.depend;

include .depend
				
clean :
	rm $(EXEC_NAME) $(OBJ_FILES)

pockets.exe : pockets.o util.o
	$(CC) $(CFLAGS) -o $@ $< util.o $(LIBS)
		
test_util.exe : util_test.o util.o
	$(CC) -static -o $@ $< util.o -lboost_unit_test_framework
	
%.o: %.cxx
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $<


