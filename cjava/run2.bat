@echo off
set MAVEN_OPTS=-Xmx752m -XX:MaxPermSize=256m
mvn  --quiet exec:java -Dexec.mainClass="codejam.Main" -Dexec.args="%1"

