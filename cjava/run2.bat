@echo on
set MAVEN_OPTS=-Xmx3752m -XX:MaxPermSize=512m
rem mvn  --quiet -e exec:java -Dexec.mainClass="codejam.Main"
mvn  --quiet -e exec:java -Dexec.mainClass="codejam.Main" -Dexec.args="%1"

