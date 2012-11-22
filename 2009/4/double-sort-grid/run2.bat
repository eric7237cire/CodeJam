@echo off
set MAVEN_OPTS=-Xmx1212m -XX:MaxPermSize=256m
mvn  --quiet exec:java -DcommandlineArgs="-Xmx1400m" -Dexec.mainClass="com.eric.codejam.Main" -Dexec.args="%1"

