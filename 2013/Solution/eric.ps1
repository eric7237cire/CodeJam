$strString = "Hello World"
write-host $strString

Function GetFiles ($subDirName)
{
return gci $subDirName -rec -filter *.cs | ?{ $_.fullname -notmatch "\\obj\\?" } | Select-Object -Unique FullName | % { $_.FullName } 
}



write-host $files

$csc = 'C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe /platform:x64 /lib:.\build /define:INCLUDE_SLOW /optimize+ ' 
$nunit = "C:\Program Files (x86)\NUnit 2.6.3\bin\nunit-console.exe"

$sysNum  = 'C:\Windows\Microsoft.NET\Framework\v4.0.30319\System.Numerics.dll'

$files = GetFiles '.\Utils'
$command = "$csc -out:.\build\Utils.dll -r:$sysNum /target:library $files  "
iex $command

$files = GetFiles '.\Round1B'
$command = "$csc -out:.\build\Round1B.dll -r:$sysNum -r:Utils.dll /target:library $files  "
iex $command

$files = GetFiles '.\Round1C'
$command = "$csc -out:.\build\Round1C.dll -r:$sysNum -r:Utils.dll /target:library $files  "
iex $command

$files = GetFiles '.\Round2'
$command = "$csc -out:.\build\Round2.dll -r:$sysNum -r:Utils.dll /target:library $files  "
iex $command

$files = GetFiles '.\Round3'
$command = "$csc -out:.\build\Round3.dll -r:$sysNum -r:Utils.dll /target:library $files  "
iex $command

$files = GetFiles '.\RoundFinal'
$command = "$csc -out:.\build\RoundFinal.dll -r:$sysNum -r:Utils.dll /target:library $files  "
iex $command

$files = GetFiles '.\UnitTest'
$nunitlib = 'C:\Program Files (x86)\NUnit 2.6.3\bin\framework\nunit.framework.dll'
write-host $command
$command = "$csc -out:.\build\UnitTest.dll /reference:$sysNum  /reference:'$nunitlib' -r:Round2.dll -r:Round1B.dll -r:Round1C.dll -r:Round3.dll -r:Utils.dll -r:RoundFinal.dll /target:library $files"
iex $command

$files = GetFiles '.\Main'
write-host $command
$command = "$csc -out:.\build\Main.exe -r:$sysNum -r:Round2.dll -r:Round1B.dll -r:Round1C.dll -r:Round3.dll -r:Utils.dll -r:RoundFinal.dll /target:exe $files  "

#write-host $files

iex $command

#& $nunit @("--help")
#& $nunit @("/framework:net-4.5", ".\build\UnitTest.dll")