trash target

ant -f build.xml

trash target

mvn install:install-file -Dfile=./target/ant/dwr.jar -DgroupId=org.directwebremoting -DartifactId=dwr -Dversion=3.0.3-RELEASE -Dpackaging=jar
mvn install:install-file -Dfile=./target/ant/dwr-src.jar -DgroupId=org.directwebremoting -DartifactId=dwr -Dversion=3.0.3-RELEASE -Dpackaging=jar -Dclassifier=sources


