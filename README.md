[![CI tests status](https://github.com/staniakm/homeBudgetKotlinApi/actions/workflows/maven.yml/badge.svg)](https://github.com/staniakm/homeBudgetKotlinApi/actions/workflows/maven.yml)

Rest api build with Koltin and Spring Boot. Is used as bridge between postgres database and consumer applications:   
1. [react frontend application](https://github.com/staniakm/homeBudgetReact) at this moment it is not developed and some functionalities are not implemented      
2. [android app](https://github.com/staniakm/android_budget_app) at this moment it is main app to insert data into database


At this moment [desktop application](https://github.com/staniakm/HomeBudgetApp) is used to fill data in database.    


##How to run
### Run with local database installed
#### Run application code from IDE
 - provide credentials to database in application.properties file
 - run application with IDE with default profile

#### Run application inside container
 - provide credentials to database in application-docker.properties
 - build application docker image with jib 
    - run command `.\mvnw clean install jib:dockerBuild`
 - run docker-compose.yml
