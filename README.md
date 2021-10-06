Koltin and Spring Boot rest api used as bridge between SQL Server database and [react frontend application](https://github.com/staniakm/homeBudgetReact).     
Database is filled with help of [desktop application](https://github.com/staniakm/HomeBudgetApp) created with C#


### How to run with docker
1. Run command `./mvnw clean install` to build jar file
2. Run command `docker build -t backend-budget-app .` to build docker image
3. Run command `docker-compose up -d` to run application with postgres database

Docker app is run with `docker` profile so properties are load from `application-docker.properties` file
