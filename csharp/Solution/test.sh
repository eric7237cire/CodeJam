#!/bin/sh

COMPILE_BASE="dmcs -define:mono -define:LOGGING -define:LOGGING_INFO -define:DEBUG -debug+ -checked+ -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll"
#COMPILE_BASE="dmcs -define:mono -define:LOGGING_INFO -optimize+ -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll"

#All loging
#COMPILE_BASE="dmcs -define:mono -define:LOGGING -define:LOGGING_INFO -define:LOGGING_DEBUG -define:LOGGING_TRACE -debug+ -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll"

#COMPILE_BASE="dmcs -define:mono -optimize+ -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll"
OR_EXIT="|| { echo 'my_command failed' ; exit 1; }"



	
find . -name '*.cs' | grep -v Properties | grep 'Utils\/' > filesUtils.txt

eval "$COMPILE_BASE \$(cat filesUtils.txt) -out:Utils.dll $OR_EXIT"

if [ "a" ]
then
	
find . -name '*.cs' | grep -v Properties | grep '2013' > files2013.txt

eval "$COMPILE_BASE -r:Utils.dll \$(cat files2013.txt) -out:2013.dll || { echo 'my_command failed' ; exit 1; }"
fi

if [ "a" ]
then
find . -name '*.cs' | grep -v Properties | grep '2014' > files2014.txt

eval "$COMPILE_BASE -r:Utils.dll \$(cat files2014.txt) -out:2014.dll || { echo 'my_command failed' ; exit 1; }"
fi


find . -name '*.cs' | grep -v Properties | grep 'UnitTest' > filesUnitTest.txt

eval "$COMPILE_BASE -r:Utils.dll -r:2013.dll -r:2014.dll \$(cat filesUnitTest.txt) -out:UnitTest.dll $OR_EXIT"

eval "dmcs -define:mono -define:LOGGING -define:LOGGING_INFO -debug+ -r:System.Numerics -target:winexe -r:2013.dll -r:2014.dll -r:Utils.dll ./Main/Launcher.cs -out:go.exe $OR_EXIT"

export MONO_GC_PARAMS=max-heap-size=1020m

#mono --debug go.exe

#/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe -run=UnitTest.TestRunner.runAllTestFiles UnitTest.dll

#/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe -run=UnitTest.TestXSpot.TestSmallAngle UnitTest.dll

#/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe -include=current UnitTest.dll

#/usr/bin/mono /usr/lib/mono/4.0/nunit-console.exe -run=UnitTest.TestPogo UnitTest.dll

/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe UnitTest.dll


