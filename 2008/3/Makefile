CC = g++
CFLAGS = -Wall -g
OLD = -g -O3
EXEC_NAME = pockets.exe test_util.exe portal.exe classroom.exe
INCLUDES =
LIBS =
OBJ_FILES = star_wars.o simplex.o util.o
INSTALL_DIR = /usr/bin


# C++ compile flags
#CFLAGS+=-DFTLOG
#CFLAGS+=-DFTLOG="${PROG}"
#CFLAGS+=-DETLOG
CFLAGS+=-DTLOG
#CFLAGS+=-DCLEANTLOG


CXX_FILES = $(wildcard *.cxx)

all : $(EXEC_NAME)


#.depend: depend



depend: $(CXX_FILES)
				rm -f ./.depend
				$(CC) $(CFLAGS) -MM $^>>./.depend;

include .depend
				
clean :
	rm $(EXEC_NAME) $(OBJ_FILES)

%.exe : %.o util.o tri_logger.o
	$(CC) $(CFLAGS) -o $@ $< util.o tri_logger.o $(LIBS)
	
test_util.exe : util_test.o util.o
	$(CC) -static -o $@ $< util.o -lboost_unit_test_framework
	
%.o: %.cxx
	$(CC) $(CFLAGS) $(INCLUDES) -o $@ -c $<


