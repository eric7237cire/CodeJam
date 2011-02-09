CC = g++
CFLAGS = -Wall
EXEC_NAME = star_wars.exe boolean_tree.exe triangle_area.exe
INCLUDES =
LIBS =
OBJ_FILES = star_wars.o simplex.o
INSTALL_DIR = /usr/bin

all : star_wars.exe boolean_tree.exe triangle_area.exe

clean :
	rm $(EXEC_NAME) $(OBJ_FILES)

boolean_tree.exe : boolean_tree.cxx
	$(CC) -o $@ $<

triangle_area.exe : triangle_area.cxx
	$(CC) -o $@ $<

star_wars.exe : star_wars.o simplex.o
	$(CC) -o $@ $< $(LIBS)
		
%.o: %.cxx
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $<


