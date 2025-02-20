# Overview
This project provides a simple Spring Boot application that uses Apache QPid to connect to an AMQ Artemis broker running in Openshift.

The connections is made using the AMQPS protocol over a TLS/SSL connection to port 443.

## Generating credentials for TLS connections

For TLS connections, AMQ requires a broker keystore, a client keystore, and a client truststore that includes the broker keystore. In the next section, we will create a broker keystore, export the broker certificate, create a client truststore, import the broker certificate into the client truststore, and then create a broker truststore.

### Create the broker keystore

First, generate a self-signed certificate for the broker keystore. When asked for a password, use `password`:

    keytool -genkey -alias broker -keyalg RSA -keystore broker.ks

Next, export the certificate so that it can be shared with clients:

    keytool -export -alias broker -keystore broker.ks -file broker_cert

Create a client truststore that imports the broker certificate:

    keytool -import -alias broker -keystore client.ts -file broker_cert

Generate a self-signed certificate for the broker trust store:

    keytool -genkey -alias broker -keyalg RSA -keystore broker.ts

Note: When you import the broker_cert make sure that you specify yes to the dialog: `Trust this certificate? [no]:  yes`. The default setting is `no`.

### Create the secret name

By default, the secret name has the following format:

    <CustomResourceName>-<AcceptorName>-secret

Following this format, we have named the secret `ex-aao-amqp-secret`. You can use whatever naming format you like. We will provide this secret name in the custom resource for ActiveMQ Artemis.

### Configure OpenShift

Log into thr OpenShift cluster as a system admin, create a project named amq-demo, and create a secret for the project.

#### Log into OpenShift:

    oc login <CLUSTER_API_URL>

#### Create a new project:

    oc new-project amq-demo

#### Create the secret:

    oc create secret generic ex-aao-amqp-secret \
    --from-file=broker.ks \
    --from-literal=keyStorePassword=password \
    --from-file=client.ts=broker.ts \
    --from-literal=trustStorePassword=password

Note: In the snippet `--from-file=client.ts=broker.ts`, we provide the `broker.ts`, which is correct. However, we're aliasing it in the secret as `client.ts`. The alias is the value that the broker image looks for in the secret.

### Configure the Artemis AMQ broker instance

Define an AMQP acceptor to enable AMQP connections that are external to OCP.

    spec:
      acceptors:
      - name: amqp
        protocols: amqp
        port: 5672
        sslEnabled: true
        sslSecret: ex-aao-amqp-secret
        verifyHost: false
        expose: true

When `sslEnabled` is true for an `acceptor`, you will need to specify the named secret that contains the keys `broker.ks`,  `client.ts`,  `keyStorePassword`,  and `trustStorePassword`. The broker will look for these in the named secret. If they're not present, OpenShift will fail to schedule the broker pod until it finds them.

## Run the application

Edit the file `application.properties` to reflect the appropriate values for your environment.

From a terminal, run the command:

    mvn spring-boot:run

This will start a simple web application that connects a Consumer and a Producer to the AMQ Artemis broker. It provides a web user interface at http://localhost:8080/send that allows you to generate and send a configurable number of messages.

The console log in the terminal window will show the results of sending and receiving the messages.