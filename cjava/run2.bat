@echo on
rem set MAVEN_OPTS=-Xmx1052m -Xss129m 
rem -XX:MaxPermSize=256m
rem mvn  --quiet -e exec:java -Dexec.mainClass="codejam.Main"
mvn  --quiet -e exec:java -Dexec.mainClass="codejam.Main" -Dexec.args="%1"

