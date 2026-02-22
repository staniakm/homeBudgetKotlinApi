[![CI tests status](https://github.com/staniakm/homeBudgetKotlinApi/actions/workflows/maven.yml/badge.svg)](https://github.com/staniakm/homeBudgetKotlinApi/actions/workflows/maven.yml)

Rest api build with Koltin and Spring Boot. Is used as bridge between postgres database and consumer applications:   
1. [react frontend application](https://github.com/staniakm/homeBudgetReact) at this moment it is not developed and some functionalities are not implemented      
2. [android app](https://github.com/staniakm/android_budget_app) at this moment it is main app to insert data into database


At this moment [desktop application](https://github.com/staniakm/HomeBudgetApp) is used to fill data in database.    

## Requirements

- Java 21
- Gradle Wrapper (`./gradlew` / `./gradlew.bat`)


## How to run
### Run with local database installed
#### Run application code from IDE
 - provide credentials to database in application.properties file
 - run application with IDE with default profile

#### Run application inside container
 - provide credentials to database in application-docker.properties
 - build application docker image with jib 
    - run command `.\mvnw clean install jib:dockerBuild`
 - run docker-compose.yml

#### Copy docker jib image to another machine
- run in cmd `docker save home-budget-api:<version> | ssh <user>@<host>> docker load`
- update docker-compose.yml with new image version on remote machine
- restart docker container on remote machine

## Running tests locally

- Run fast unit tests only:
  - `./gradlew unitTest`
  - On Windows: `./gradlew.bat unitTest`

- Run full quality gate (integration tests + coverage verification + disabled-test check):
  - `./gradlew check`
  - On Windows: `./gradlew.bat check`

- Generate and inspect coverage reports:
  - `./gradlew test jacocoTestReport`
  - JaCoCo HTML report: `build/reports/jacoco/test/html/index.html`
  - JaCoCo XML report: `build/reports/jacoco/test/jacocoTestReport.xml`
  - Test report: `build/reports/tests/test/index.html`
