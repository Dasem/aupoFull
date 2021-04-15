@echo off
docker build -t mysql/bs .
docker run -e MYSQL_ROOT_PASSWORD=hard_pwd -p 3306:3306 mysql/bs
