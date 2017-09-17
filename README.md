# newrelic-data-transformers

## Execution details
Create a json file with tranform commands as below. Also have your input data file handy.
**NOTE:** Column for DATE transformation will need comma separated value of <date column>,<time column>
```
{
  "spec_version": 1,
  "transforms": [
    {
      "operation": "hst-to-unix",
      "column": "RecordedDate,TimeSunRise"
    },
    {
      "operation": "slugify",
      "column": "RecordLocation"
    },
    {
      "operation": "f-to-c",
      "column": "Temperature"
    },
    {
      "operation": "hst-to-unix",
      "column": "RecordedDate,timesunSet"
    }
  ]
}
```
### Project requirements to build
Java 8, Maven

### Build project
Build project with below command.
`mvn clean install`

### Execute application
Project can be executed using below command.
`java -jar target/newrelic-data-transformers-0.0.1-SNAPSHOT.jar <PATH-TO-JSON-WITH-TRANSFORM-COMMANDS> <PATH-TO-INPUT-DATA-FILE> <OUTPUT-FILE-PATH>`

### Key Highlights 

#### Architecture
1. Spring boot application.
2. Maintains the order of operations as specified in the input json.
3. All data transform operations implement same interface there by exposing same behavior abstraction.
4. Performs line by line processing of input thereby keeps output file in the same order as input file.

#### TESTING
1. Integration test validates functionality of the entire application. This reads a sample commands, data file and verifies the generated output file contents.
2. Unit tests validate individual functionality.

#### Production level solution for the problem.
Since documentation mentioned the program should be executable as a simple command line program i picked the current approach.
Ideally a scalable solution would be to implement using spark.  
