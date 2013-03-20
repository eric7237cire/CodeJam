import sys
import re

headerF = open("""C:\codejam\CodeJam\lib\geom.h""", 'r')
sourceFile = open( sys.argv[1], 'r')
backupFile = open( sys.argv[1] + 'backup', 'w')

headerContents = headerF.read()
sourceContents = sourceFile.read()

backupFile.write(sourceContents)


replaceGeom = re.compile(r"""
(//STARTGEOM)               # Start of a numeric entity reference
(.*)
(//STOPGEOM)
""", re.VERBOSE | re.DOTALL | re.MULTILINE )

replacement = '\\1\n' + headerContents + '\n\\3'
#print(replacement)
sourceContents = replaceGeom.sub( replacement,  sourceContents )

#print( replaceGeom.search(sourceContents).groups() )

sourceFile = open( sys.argv[1], 'w')
sourceFile.write( sourceContents )

#print(headerContents)
#print(sourceContents)