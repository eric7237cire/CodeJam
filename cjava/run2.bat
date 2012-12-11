@echo on
set MAVEN_OPTS=-Xmx752m -XX:MaxPermSize=256m
mvn  --quiet -e exec:java -Dexec.mainClass="codejam.Main"
rem mvn  --quiet -e exec:java -Dexec.mainClass="codejam.Main" -Dexec.args="%1"

