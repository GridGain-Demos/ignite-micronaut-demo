# Demo: Getting Started With Apache Ignite and Micronaut

The demo shows how to create a simple Micronaut-powered application that processes HTTP requests and queries data from
an Ignite cluster using SQL APIs. The app is deployed in Docker and uses a lightweight Ignite thin client connection
to communicate with the cluster.

## Start Ignite Cluster and Load Sample Database

Before deploying the application, let's boot up an Ignite cluster and load it with sample data.

Start a two-node Ignite cluster in Docker using configuration settings of the `ignite-cluster.yaml` file:
```
docker-compose -p cluster -f ignite-cluster.yaml up --scale ignite_server_node=2 -d
```

Create the World database schema that is shipped with every Ignite release and load sample data into the cluster:

1. Connect to the first Ignite server node container:
    ```bash
    docker exec -it cluster_ignite_server_node_1 bash
    ```

2. Go to the `bin` folder of the Ignite installation directory:
    ```bash
    cd apache-ignite/bin/
    ```

3. Connect to the cluster with the SQLLine tool: 
    ```bash
    ./sqlline.sh --verbose=true -u jdbc:ignite:thin://127.0.0.1/
    ```

4. Load the World database using SQLLine: 
    ```bash
    !run ../examples/sql/world.sql
    ```bash

5. Disconnect from the container by closing the SQLLine connection (`!q`) and the bash session (`exit`)

## Start Micronaut Application

Similarly to the cluster, you deploy the application in Docker with a single port open for HTTP requests processing.

Create the application's Docker image:

1. Build the application with Maven:
    ```bash
    mvn clean package
    ```

2. Produce a Docker image named `ignite-micronaut-app`:
    ```bash
    docker build -t ignite-micronaut-app .
    ```

Start the application passing an IP address of one of the Ignite server nodes:

1. Get the IP address of the first server node:
    ```bash
    docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' cluster_ignite_server_node_1
    ```

2. Replace the `<YOUR_IP_ADDRESS>` with the IP address returned by the previous command and launch the application in Docker:
    ```bash
    docker run -p 8080:8080 --env igniteServerAddress=<YOUR_IP_ADDRESS>:10800 --name ignite-micronaut-app --network cluster_ignite_net ignite-micronaut-app
    ```

The application will connect to the cluster using the `<YOUR_IP_ADDRESS>:10800` endpoint and will be ready to process your
HTTP requests on port `8080`.

## Request Most Populated Cities

With the application running, you can request a list of the most populated cities by opening the URL below in a browser or
with `curl`:
```bash
curl http://localhost:8080/cities?population=8000000
```

The previous request returns all the cities with the population equal to or bigger than 8 million. Micronaut intercepts this
request and queries the Ignite cluster to prepare the response. Note, you can change
the value of the `population` parameter to get cities with a different population.

## Terminate Demo

Use the commands below to shutdown the demo and free up resources:

* Stop the application: 
    ```bash
    docker container stop ignite-micronaut-app
    ```

* Remove the application container:
    ```bash
    docker container rm ignite-micronaut-app
    ```
 
* Shutdown the Ignite cluster:
    ```bash
    docker-compose -p cluster -f ignite-cluster.yaml down
    ```
