import sys
import re

if sys.argv[2] == "common":
	headerF = open("""C:\codejam\CodeJam\lib\common.h""", 'r')
elif sys.argv[2] == "graph":
	headerF = open("""C:\codejam\CodeJam\lib\graph.h""", 'r')
else:
	headerF = open("""C:\codejam\CodeJam\lib\geom.h""", 'r')
sourceFile = open( sys.argv[1], 'r')
backupFile = open( sys.argv[1] + 'backup', 'w')

headerContents = headerF.read()
sourceContents = sourceFile.read()

#print(headerContents)

backupFile.write(sourceContents)

if sys.argv[2] != "common":
	replaceGeom = re.compile(r"""
(//STARTCOMMON)               # Start of a numeric entity reference
(.*)
(//STOPCOMMON)
""", re.VERBOSE | re.DOTALL | re.MULTILINE )
else:
	replaceGeom = re.compile(r"""
(//STARTGEOM)               # Start of a numeric entity reference
(.*)
(//STOPGEOM)
""", re.VERBOSE | re.DOTALL | re.MULTILINE )

#make sure backslashes stay that way
headerContents = headerContents.replace("\\", "\\\\")
replacement = '\\1\n' + headerContents + '\n\\3'
#print(replacement)
#print (sourceContents)
sourceContents = replaceGeom.sub( replacement,  sourceContents )

#print( replaceGeom.search(sourceContents).groups() )

sourceFile = open( sys.argv[1], 'w')
sourceFile.write( sourceContents )

#print(headerContents)
#print(sourceContents)