@echo off
mvn  --quiet exec:java -Dexec.mainClass="com.eric.codejam.Main" -Dexec.args="%1"

