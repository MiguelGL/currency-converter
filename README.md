# Currency Converter - Demo Exercise Project

A project I wrote to demonstrate several facets. Other than pure
curiosity or examination, there is little real-world value of the
built-in features.

- Java EE 7 compliant application.
- Writen using Java 8 features.
- Consumes data from [Open Exchange
  Rates](https://openexchangerates.org/signup/free) via their REST
  API.
- Exposes currency conversion endpoints in a REST API.
- Uses PostgreSQL as database.
- Autogenerates API spec. and documentation at build time via Swagger
  Maven Plugin.
- Bundles with Swagger UI for examining and testing the implemented
  API.

## Stuff/APIs Worth a List

- [Lombok](https://projectlombok.org) is used extensively.
- Java EE 7: CDI, JPA (via Hibernate), EJB, JAX-RS (server and client
  specs.), Bean Validation.
- [QueryDSL](http://querydsl.com/) as a substitute to plain JPQL and
  the JPA Criteria API.
- [Swagger](http://swagger.io/) for API spec.
- [RestAssured](https://github.com/rest-assured/rest-assured) for
  Integration Tests.


## Prerequisites

- Linux or MacOS machine. The development was done on MacOS El Capitan.
- JDK 8. For development the MacOS 64 bits distribution from Oracle was used.
- Apache Maven 3.3.*. Installable via homebrew/macports on Mac OS and linux
  package managers.
- Download the WildFly 10.0.0 distribution zip from:
  [http://download.jboss.org/wildfly/10.0.0.Final/wildfly-10.0.0.Final.zip](http://download.jboss.org/wildfly/10.0.0.Final/wildfly-10.0.0.Final.zip)
  and extract it somewhere in your machine where the current user has
  write access.
- A PostgreSQL server version 9.3 or higher running on the machine and
  access as a user with privileges to create databases. Installable
  via homebrew/macports on Mac OS and linux package managers. Setting
  up authentication for accessing the PostgreSQL database is done via
  the pg_hba.conf file. A tradeoff solution could be to temporarily
  allow usecure access by means of a line in the form:

        host    all             all             127.0.0.1/32            md5


## Instructions

The following will be assumed from now on:

- This repo has been cloned into a directory named
  `currency-converter`. 
- `<WildFlyDir>` will be the directory where the wildfly-10.0.0.Final.zip file
  has been extracted. In my machine, this for example is `~/opt/wildfly-10.0.0.Final`.


### To setup the database

       $ cd currency-converter/deploy/db

Edit the `pg-db.conf` file and set the `PG_USER` to the name of the
administrative user for postgres. It will be used to create the
database. For Debian-based distros this typically is `postgres`. Also,
if a value different than `localhost` is to be used to connect to the
database, the `DB_HOST` variable must be set. This also applies to the
above Debian-based distros (and probably others): a value of
`/var/run/postgresql` is valid.

Also, edit the same file and set `PG_PATH` variable to the base path
where the PostgreSQL command-line utilities lie in your system.  There
are some commented samples for some linux distributions.

         $ ./00_setup-db.sh

When prompted for the password for the `currencyconverter` user,
please use _currencyconverter_ as well to simplyfy the deployment. The
password will need to be entered twice.

You should see something like, meaning everything went fine.

        miguelgl@MacBook-Pro:SQL$ ./00_setup-db.sh
        ---> Creating DB user 'currencyconverter' ...
        Enter password for new role:
        Enter it again:
        ---> Done
        ---> Creating DB 'currencyconverter' owned by 'currencyconverter' ...
        ---> Done
        ---> Creating DB 'currencyconverter' tables ...
         setval
        --------
              1
        (1 row)
        
         setval
        --------
              1
        (1 row)
        
        ---> Done


### Start the Application Server

      $ cd <WildFly>/bin
      $ ./standalone.sh

Please leave this terminal running the program and outputing the
server logs and open a new terminal.
  

### To setup the Application Server:

       $ cd currency-converter/deploy

Edit `defaults.conf` and set the `WILDFLY_BASE_DIR` variable to point to
your <WildFlyDir> in your machine. If you changed nothing else (just
followed these instructions), leave all other parameters unchanged.

       $ ./provision-wildfly.sh

You should see an output of the like, which means everything went
fine:

        miguelgl@MacBook-Pro:Deployment$ ./provision-wildfly.sh
        [standalone@127.0.0.1:9990 /] module add --name=org.postgres
          --resources=postgresql-9.4.1208.jre7.jar
          --dependencies=javax.api,javax.transaction.api
        [standalone@127.0.0.1:9990 /]
        [standalone@127.0.0.1:9990 /]
        /subsystem=datasources/jdbc-driver=postgres:add(
        driver-name="postgres",
        driver-module-name="org.postgres",
        driver-class-name="org.postgresql.Driver",
        driver-xa-datasource-class-name="org.postgresql.xa.PGXADataSource")
        {"outcome" => "success"}
        [standalone@127.0.0.1:9990 /]
        [standalone@127.0.0.1:9990 /] xa-data-source add
        --name=CurrencyConverterXADS
        --driver-name=postgres
        --jndi-name=java:jboss/datasources/CurrencyConverterXADS
        --user-name=currencyconverter
        --password=currencyconverter
        --use-java-context=true
        --use-ccm=true
        --min-pool-size=10
        --max-pool-size=100
        --pool-prefill=true
        --allocation-retry=1
        --prepared-statements-cache-size=32
        --share-prepared-statements=true
        --xa-datasource-properties=ServerName=localhost,PortNumber=5432,DatabaseName=currencyconverter
        --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker
        --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter
        [standalone@127.0.0.1:9990 /] xa-data-source enable --name=CurrencyConverterXADS
        operation-requires-reload: true
        process-state:             reload-required
        [standalone@127.0.0.1:9990 /] reload
        [standalone@127.0.0.1:9990 /]


### To Compile the project

       $ cd currency-converter
       $ mvn clean install -DskipTests=true

This may take a very long the first time while maven downloads
everything.


### To Deploy the solution

       $ cd currency-converter/server/api-server
       $ mvn wildfly:deploy -DskipTests=true

You should see the previous terminal dedicated to running the
application server outputing lots of logs during the deployment.

Upon success, maven will report something like:

        miguelgl@MacBook-Pro:api-server$ mvn wildfly:deploy -DskipTests=true
        ...
        INFO: JBoss Remoting version 4.0.9.Final
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 16.584 s
        [INFO] Finished at: 2016-04-22T22:23:45+02:00
        [INFO] Final Memory: 37M/358M
        [INFO] ------------------------------------------------------------------------


### To Run the Unit Tests (Integration Tests, really)

       $ cd currency-converter
       $ mvn test

Upon success, maven will report something like:

        ...
        Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
        ...
        Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
        
        [INFO] ------------------------------------------------------------------------
        [INFO] Reactor Summary:
        [INFO]
        [INFO] CURRCONV :: --- .................................... SUCCESS [  0.435 s]
        [INFO] CURRCONV :: Providers :: --- ....................... SUCCESS [  0.005 s]
        [INFO] CURRCONV :: Providers :: API ....................... SUCCESS [  0.435 s]
        [INFO] CURRCONV :: Providers :: OXR ....................... SUCCESS [  3.854 s]
        [INFO] CURRCONV :: Server :: --- .......................... SUCCESS [  0.003 s]
        [INFO] CURRCONV :: Server :: API Server ................... SUCCESS [  6.666 s]
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 11.818 s
        [INFO] Finished at: 2016-04-22T22:25:55+02:00
        [INFO] Final Memory: 30M/284M
        [INFO] ------------------------------------------------------------------------


### To access the served components

- API Doc:       [http://localhost:8080/converter/api-doc/api-doc.html](http://localhost:8080/converter/api-doc/api-doc.html)
- API Tester UI: [http://localhost:8080/converter/swagger-ui/index.html](http://localhost:8080/converter/swagger-ui/index.html)


### Other considerations

- Running `mvn wildfly:undeploy` will undeploy the solution from the
  application server.
- The application server can be stopped with a `kill -SIGINT <pid>`.
- There are other scripts in the `deploy/db` dir to cleanup the created
  database and user. 
