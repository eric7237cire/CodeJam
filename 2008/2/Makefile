CC = g++
CFLAGS = -O3
OLD = -Wall -g -g -O3
EXEC_NAME = star_wars.exe boolean_tree.exe triangle_area.exe
INCLUDES = -IC:\codejam\boost_1_52_0
LIBS = -LC:\codejam\boost_1_52_0\stage\lib\
#OBJ_FILES = star_wars.o simplex.o util.o

INSTALL_DIR = /usr/bin

CXX_FILES = $(wildcard *.cxx)

all : perm_rle.exe test_util.exe star_wars.exe boolean_tree.exe triangle_area.exe test_simplex.exe perm_rle.exe

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
	$(CC) $(CFLAGS) -o $@ $< util.o $(LIBS)
		
star_wars.exe : star_wars.o simplex.o util.o
	$(CC) -o $@ $< simplex.o util.o $(LIBS)
	
test_simplex.exe : simplex_test.o simplex.o util.o
	$(CC) ${LIBS} -static -o $@ $< simplex.o util.o -lboost_unit_test_framework-mgw47-1_52
	
test_util.exe : util_test.o util.o
	$(CC) ${LIBS} -static -o $@ $< util.o -lboost_unit_test_framework-mgw47-1_52
	
%.o: %.cxx
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $<


