@echo off
mvn  --quiet exec:java -DcommandlineArgs="-Xmx500m" -Dexec.mainClass="com.eric.codejam.Main" -Dexec.args="%1"

