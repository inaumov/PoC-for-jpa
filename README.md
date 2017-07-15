This is set of example applications of some not trivial cases of ORM usage.

RUNNING THE APPLICATION:

- Download and install Maven 3 (http://maven.apache.org/download.html#Installation). If you have already installed Maven 3, you can skip this step.
- Go the root directory of any sub-project (The one which contains the pom.xml file)

RUNNING TESTS:

- You can run unit tests by using this command: mvn test

Used libs:
- lombok 1.16.8
- openjpa 2.2.0
- junit 4.10
- dbunit 2.4.8
- derby 10.8.2.2
  
In case of 
```
org.apache.openjpa.persistence.ArgumentException: This configuration disallows runtime optimization, but the following listed types were not enhanced at build time or at class load time with a javaagent:
```
follow instructions from http://djitz.com/neu-mscs/getting-started-using-apache-openjpa/
