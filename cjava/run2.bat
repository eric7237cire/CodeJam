@echo on
set MAVEN_OPTS=-Xmx752m -XX:MaxPermSize=256m
rem mvn  --quiet -e exec:java -Dexec.mainClass="codejam.Main"
mvn  --quiet -e exec:java -Dexec.mainClass="codejam.Main" -Dexec.args="%1"

