## How to build and run 

### Build application 
mvn clean install

### Build docker image
docker build . -t flighter

### Run db and app
docker compose up

### Run only db
docker compose up mysql

### All in one
mvn clean install && docker build . -t flighter && docker compose up