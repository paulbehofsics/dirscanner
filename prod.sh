#!/usr/bin/fish

set -x POSTGRES_HOST localhost
set -x POSTGRES_PORT 5432
set -x POSTGRES_DB dirscanner
set -x POSTGRES_USER postgres
set -x POSTGRES_PASSWORD password
set -x DIRSCANNER_ROOT /home
set -x DIRSCANNER_DELAY_MINUTES 1
mvn -D skipTests clean package;
  and cp ./rest/target/*.jar app.jar;
  and java -jar app.jar
