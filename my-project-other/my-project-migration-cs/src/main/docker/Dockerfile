FROM liquibase/liquibase:4.31.1

# Копирование конфигурации и миграций (excluding liquibase.properties since connection params come from env vars)
COPY ./src/main/liquibase/changelog /liquibase/changelog

# Установка правильного JDBC-драйвера
ENV CASSANDRA_EXT_VERSION=4.31.1
RUN mkdir -p /liquibase/lib && cd /liquibase/lib \
    && wget \
    https://repo1.maven.org/maven2/org/liquibase/ext/liquibase-cassandra/${CASSANDRA_EXT_VERSION}/liquibase-cassandra-${CASSANDRA_EXT_VERSION}.jar

# Copy pre-downloaded JDBC wrapper (downloaded on host to avoid SSL issues in Docker)
COPY cassandra-jdbc-wrapper-4.14.0-bundle.jar /liquibase/lib/

CMD ["liquibase", "--changelog-file=changelog/changelog.xml", "update"]
#CMD ["/usr/bin/bash", "-c", "sleep 3600"]