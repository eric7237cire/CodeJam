@echo off
java -Xms512m -Xmx4024m  -jar .${symbol_escape}target${symbol_escape}${artifactId}-${version}-jar-with-dependencies.jar %1 %2 %3 
