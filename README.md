# Version Information
- Java version: 17
- Gradle version: 8.10

# Execute Instructions
- Install all files as a zip, then extract to a folder of your choice.
- Navigate to the folder in the command line by using `cd`.
- Run `gradle clean build`
- Run `gradle run`
  - To run the program without the gradle output, run `gradle run -q --console=plain` instead.

# Test Instructions
- Tests should automatically be done when `gradle build` is run.
- Nevertheless, tests can be run again by running `gradle test`
- Coverage report can be found in `build/reports/jacoco/test/html/index.html`
