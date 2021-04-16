FROM mysql
COPY files/*.sql /docker-entrypoint-initdb.d
COPY files/custom_mysql.cnf /etc/mysql/conf.d
EXPOSE 3306