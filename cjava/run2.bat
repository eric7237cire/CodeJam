@echo off
mvn  --quiet exec:java -Dexec.mainClass="codejam.Main" -Dexec.args="%1"

