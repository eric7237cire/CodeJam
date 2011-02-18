CC = g++
CFLAGS = -Wall 
OLD = -g -O3
EXEC_NAME = star_wars.exe boolean_tree.exe triangle_area.exe
INCLUDES =
LIBS =
OBJ_FILES = star_wars.o simplex.o util.o
INSTALL_DIR = /usr/bin

CXX_FILES = $(wildcard *.cxx)

all : star_wars.exe boolean_tree.exe triangle_area.exe test_simplex.exe perm_rle.exe

#.depend: depend

include .depend

depend: $(CXX_FILES)
				rm -f ./.depend
				$(CC) $(CFLAGS) -MM $^>>./.depend;

clean :
	rm $(EXEC_NAME) $(OBJ_FILES)

boolean_tree.exe : boolean_tree.cxx
	$(CC) -o $@ $<

triangle_area.exe : triangle_area.cxx
	$(CC) -o $@ $<

perm_rle.exe : perm_rle.o util.o
	$(CC) -o $@ $< util.o $(LIBS)
		
star_wars.exe : star_wars.o simplex.o util.o
	$(CC) -o $@ $< simplex.o util.o $(LIBS)
	
test_simplex.exe : simplex_test.o simplex.o util.o
	$(CC) -static -o $@ $< simplex.o util.o -lboost_unit_test_framework
	
%.o: %.cxx
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $<


