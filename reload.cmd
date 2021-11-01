mvnw clean install &
docker build -t backend-budget-app:1.0 . &
docker rmi $(docker images -f “dangling=true” -q) &
docker-compose up -d