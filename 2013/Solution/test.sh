#!/bin/sh

COMPILE_BASE="dmcs -define:mono -define:LOGGING -define:LOGGING_INFO -debug+ -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll"
#COMPILE_BASE="dmcs -define:mono -optimize+ -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll"
OR_EXIT="|| { echo 'my_command failed' ; exit 1; }"



	
find . -name '*.cs' | grep -v Properties | grep 'Utils\/' > filesUtils.txt

eval "$COMPILE_BASE \$(cat filesUtils.txt) -out:Utils.dll $OR_EXIT"

if [ "" ]
then
	
find . -name '*.cs' | grep -v Properties | grep 'Round1B' > files1b.txt

eval "$COMPILE_BASE -r:Utils.dll \$(cat files1b.txt) -out:Round1B.dll || { echo 'my_command failed' ; exit 1; }"
fi

####  1C
if [ "a" ]
then
find . -name '*.cs' | grep -v Properties | grep 'Round1C' > files1c.txt

eval "$COMPILE_BASE -r:Utils.dll \$(cat files1c.txt) -out:Round1C.dll || { echo 'my_command failed' ; exit 1; }"
fi

if [ "" ]
then

find . -name '*.cs' | grep -v Properties | grep 'Round2' > files2.txt

eval "$COMPILE_BASE -r:Utils.dll \$(cat files2.txt) -out:Round2.dll || { echo 'my_command failed' ; exit 1; }"
fi

if [ "" ]
then
find . -name '*.cs' | grep -v Properties | grep 'Round3' > files3.txt

eval "$COMPILE_BASE -r:Utils.dll \$(cat files3.txt) -out:Round3.dll || { echo 'my_command failed' ; exit 1; }"
fi

find . -name '*.cs' | grep -v Properties | grep 'UnitTest' > filesUnitTest.txt

eval "$COMPILE_BASE -r:Utils.dll -r:Round3.dll -r:Round2.dll -r:Round1B.dll -r:Round1C.dll \$(cat filesUnitTest.txt) -out:UnitTest.dll $OR_EXIT"

export MONO_GC_PARAMS=max-heap-size=1520m
/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe -run=UnitTest.TestRunner UnitTest.dll
/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe -run=UnitTest.TestBinaryTree UnitTest.dll

#/usr/bin/mono /usr/lib/mono/4.0/nunit-console.exe -run=UnitTest.TestLost UnitTest.dll

#/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe UnitTest.dll


