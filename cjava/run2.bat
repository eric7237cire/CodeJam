@echo off
mvn  --quiet exec:java -Dexec.mainClass="codejam.y2009.sokoban.Main" -Dexec.args="%1"

