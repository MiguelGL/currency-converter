#!/bin/bash

# Good niceties: 
# http://developer-should-know.tumblr.com/post/109755962862/how-to-add-oracle-xa-data-source-in-jboss-and

. defaults.conf

$WILDFLY_BASE_DIR/bin/jboss-cli.sh --connect controller=$WILDFLY_MANAGEMENT << EOF
module add --name=org.postgres \
           --resources=postgresql-9.4.1208.jre7.jar \
           --dependencies=javax.api,javax.transaction.api
EOF

$WILDFLY_BASE_DIR/bin/jboss-cli.sh --connect controller=$WILDFLY_MANAGEMENT << EOF
/subsystem=datasources/jdbc-driver=postgres:add( \
    driver-name="postgres", \
    driver-module-name="org.postgres", \
    driver-class-name="org.postgresql.Driver", \
    driver-xa-datasource-class-name="org.postgresql.xa.PGXADataSource")
EOF

$WILDFLY_BASE_DIR/bin/jboss-cli.sh --connect controller=$WILDFLY_MANAGEMENT << EOF
xa-data-source add \
    --name=CurrencyConverterXADS \
    --driver-name=postgres \
    --jndi-name=java:jboss/datasources/CurrencyConverterXADS \
    --user-name=${DB_USER} \
    --password=${DB_PASSWORD} \
    --use-java-context=true \
    --use-ccm=true \
    --min-pool-size=10 \
    --max-pool-size=100 \
    --pool-prefill=true \
    --allocation-retry=1 \
    --prepared-statements-cache-size=32 \
    --share-prepared-statements=true \
    --xa-datasource-properties=ServerName=${DB_HOST},PortNumber=${DB_PORT},DatabaseName=${DB_NAME} \
    --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker \
    --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter
xa-data-source enable --name=CurrencyConverterXADS
reload
EOF

exit 0
