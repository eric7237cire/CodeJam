@echo off
java -Xms1400m  -XX:MaxPermSize=512m -Xmx2800m -jar .\target\square-math-1.0-SNAPSHOT-jar-with-dependencies.jar %1 %2 %3 
