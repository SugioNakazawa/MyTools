docker run --rm --name docker_oracle_11202 --shm-size=1g -p 1521:1521 \
-p 8080:8080 -e ORACLE_PWD=password oracle/database:11.2.0.2-xe
