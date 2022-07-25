# SQL unit testing sandbox
This project demonstrates how Java unit tests and an in-memory H2 database (https://www.h2database.com/) can be used to
run some basic unit tests against DLL and SQL scripts.

## To run the tests
```
./mvnw clean test
```

Any syntax errors in the DDL or SQL scripts will be picked up by the unit test which also asserts that the transformation
has joined the data from the two tables as expected.

The H2 in-memory database JDBC URL used in the test is:

```
jdbc:h2:mem:testdb
```


## H2 console access
If you wish to examine the data created by the tests using the H2 console you will need to modify the H2 URL to be a 
file based JDBC URL:

```
jdbc:h2:~/testdb
```

Then launch the console as follows.

1. Locate the H2 JARs. For example:

```
<USER HOME>/.m2/repository/com/h2database/h2/2.1.214
```

2. Run this command to launch th console:
```
java -jar h2*.jar
```

A new browser tab should launch at: http://localhost:8082/ displaying the H2 console

3. Use this JDBC URL in the console to connect to the file based database that the tests are using:

```
jdbc:h2:tcp://localhost/~/testdb
```

You will need to disconnect and stop the console to execute the tests again

