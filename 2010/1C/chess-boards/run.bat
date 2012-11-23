@echo off
java -Xms1400m  -XX:MaxPermSize=512m -Xmx3500m -jar .\target\chess-boards-1.0-SNAPSHOT-jar-with-dependencies.jar %1 %2 %3 
