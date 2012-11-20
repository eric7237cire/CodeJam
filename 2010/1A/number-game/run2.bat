@echo off
mvn  -e --quiet exec:java -Dexec.mainClass="com.eric.codejam.Main" -Dexec.args="%1"

