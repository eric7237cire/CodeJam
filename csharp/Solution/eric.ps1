$strString = "Hello World"
write-host $strString

Function GetFiles ($subDirName)
{
return gci $subDirName -rec -filter *.cs | ?{ $_.fullname -notmatch "\\obj\\?" } | Select-Object -Unique FullName | % { $_.FullName } 
}



write-host $files
$nunitlib = 'C:\Program Files (x86)\NUnit 2.6.3\bin\framework\nunit.framework.dll'
$csc = 'C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe /platform:x64 /lib:.\build /define:INCLUDE_SLOW /optimize+ /reference:$nunitlib ' 
#$csc = 'C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe /platform:x64 /lib:.\build /define:INCLUDE_SLOW /define:DEBUG /checked ' 
$nunit = "C:\Program Files (x86)\NUnit 2.6.3\bin\nunit-console.exe"

$sysNum  = 'C:\Windows\Microsoft.NET\Framework\v4.0.30319\System.Numerics.dll'

$files = GetFiles '.\Utils'
$command = "$csc -out:.\build\Utils.dll -r:$sysNum /target:library $files  "
iex $command

$files = GetFiles '.\2013'
$command = "$csc -out:.\build\2013.dll -r:$sysNum -r:Utils.dll /target:library $files  "
iex $command

$files = GetFiles '.\2014'
$command = "$csc -out:.\build\2014.dll -r:$sysNum -r:Utils.dll /target:library $files  "
iex $command


$files = GetFiles '.\UnitTest'

write-host $command
$command = "$csc -out:.\build\UnitTest.dll /reference:$sysNum   -r:2013.dll  -r:Utils.dll /target:library $files"
iex $command

$files = GetFiles '.\Main'
write-host $command
$command = "$csc -out:.\build\Main.exe -r:$sysNum -r:2013.dll -r:2014.dll -r:Utils.dll /target:exe $files  "

#write-host $files

iex $command

#& $nunit @("--help")
& $nunit @("/framework:net-4.5", ".\build\UnitTest.dll")

#& $nunit @("/framework:net-4.5", ".\build\UnitTest.dll") #, "/run:UnitTest.TestStory.TestCountDP")