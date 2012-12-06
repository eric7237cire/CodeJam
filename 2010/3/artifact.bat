del /f /s /q %1
rmdir /s /q %1
mvn -o --offline archetype:generate -DarchetypeGroupId=com.eric.codejam -DarchetypeArtifactId=archetype -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=codejam  -DinteractiveMode=false -Dgoals=eclipse:eclipse -DartifactId=%1
