@echo off
rem set MAVEN_OPTS=-Xmx1212m -XX:MaxPermSize=256m
mvn  --quiet exec:java -Dexec.mainClass="com.eric.codejam.Main" -Dexec.args="%1"

