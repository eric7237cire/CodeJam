#!/bin/sh

if [ "all" = "all2" ]
then
	
find . -name '*.cs' | grep -v Properties | grep 'Utils\/' > filesUtils.txt

dmcs -define:mono -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll -debug+ $(cat filesUtils.txt) -out:Utils.dll || { echo 'my_command failed' ; exit 1; }

find . -name '*.cs' | grep -v Properties | grep 'Round1B' > files1b.txt

dmcs -define:mono -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll -r:Utils.dll -debug+ $(cat files1b.txt) -out:Round1B.dll || { echo 'my_command failed' ; exit 1; }

find . -name '*.cs' | grep -v Properties | grep 'Round1C' > files1c.txt

dmcs -define:mono -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll -r:Utils.dll -debug+ $(cat files1c.txt) -out:Round1C.dll || { echo 'my_command failed' ; exit 1; }

find . -name '*.cs' | grep -v Properties | grep 'Round2' > files2.txt

dmcs -define:mono -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll -r:Utils.dll -debug+ $(cat files2.txt) -out:Round2.dll || { echo 'my_command failed' ; exit 1; }

find . -name '*.cs' | grep -v Properties | grep 'Round3' > files3.txt

dmcs -define:mono -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll -r:Utils.dll -debug+ $(cat files3.txt) -out:Round3.dll || { echo 'my_command failed' ; exit 1; }


find . -name '*.cs' | grep -v Properties | grep 'UnitTest' > filesUnitTest.txt

dmcs -define:mono -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll -r:Utils.dll -r:Round3.dll -r:Round2.dll -r:Round1B.dll -r:Round1C.dll -debug+ $(cat filesUnitTest.txt) -out:UnitTest.dll || { echo 'my_command failed' ; exit 1; }

fi

find . -name '*.cs' | grep -v Properties | grep 'Round1B' > files1b.txt

dmcs -define:mono -define:LOGGING -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll -r:Utils.dll -debug+ $(cat files1b.txt) -out:Round1B.dll || { echo 'my_command failed' ; exit 1; }

find . -name '*.cs' | grep -v Properties | grep 'UnitTest' > filesUnitTest.txt

dmcs -define:mono -r:System.Numerics -r:System.Xml.Linq.dll -target:library -r:nunit.framework.dll -r:Utils.dll -r:Round3.dll -r:Round2.dll -r:Round1B.dll -r:Round1C.dll -debug+ $(cat filesUnitTest.txt) -out:UnitTest.dll || { echo 'my_command failed' ; exit 1; }

export MONO_GC_PARAMS=max-heap-size=1020m
/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe -run=UnitTest.TestLost UnitTest.dll

#/usr/bin/mono --debug $MONO_OPTIONS /usr/lib/mono/4.0/nunit-console.exe -run=Priority_Queue_Tests go.exe
