# Prime Number Service

This is a Prime Number Service application which generates the prime numbers till the initial given number.  
This application is developed using RESTFul API service with Spring Boot.

## Overview

This application exposes an endpoint in <http://localhost:9000/primes/{number}> that can be used to retrieve all prime numbers up
to the given initial value.

The health of the application can be accessed on <http://localhost:9000/actuator/health>.

## How to compile and run the application

### Run the following command to compile

`./mvnw clean install`

### Run the following command to run the tests

`./mvnw test`

### Run the following command to package the application

`./mvnw package`

### Run the following command to run the application
This command will run the application as a standalone NOT IN A CONTAINER.

`java -jar ./prime-number-service-core/target/prime-number-service-core-1.0-SNAPSHOT.jar`

> **_NOTE:_**: To simplify the stuff, have added a `build.sh` file at the root of the project, which contains all the
> necessary steps such as: clean, test, build docker image and finally run the application in a docker environment.
>
> To trigger the script, navigate to the root of the project and run `./build.sh`.
>
> Upon trigger, script will perform all the actions and at the end application will be up and running on the port
`9000`.
>
> You can access the running app on `http://localhost:9000/actuator`

## Solution API design

The API customers are expecting is quite simple.

This app contains two endpoints.

1. GET /actuator/health. This API endpoint must return an HTTP 200/OK, which means app is now ready to accept requests.


2. GET /primes/{number}?algorithm=bruteForce. This API endpoint receives the parameters `number`,`number` must be a
   positive integer,`algorithm` must be a configured algorithm application, which we will be using it to calculate the prime
   number.
   The application will be capturing these two parameters and generating the prime numbers based on the initial number and as
   per the input algorithm.
   `algorithm` parameter is an optional field. However, if not given, default would be **heuristic**.
    - An HTTP 200/OK reply when the application respond back the requested primes. The body can be a JSON or XML object
      with the primes data returned by the application. User can select the type of response application should return,
      default is `JSON`, but user can also request the response in `XML` as well, by passing an extra header
      `Accept: application/xml`.
   
    - An HTTP 400/BAD REQUEST reply when the application not able to understand the incoming request or parameters.

## API Description

### HEALTH Endpoint

- Health endpoint available at `http://localhost:9000/actuator/health`

```json
{
  "status": "UP"
}
```

### Generate Prime Numbers Endpoint
- Generate Prime numbers endpoint available at  
  Request:  
  `http://localhost:9000/primes/{number}`  
  header 'Accept: application/json' (default)  

```json

{
  "initial": 12,
  "primes": [
    1,
    2,
    3,
    5,
    7,
    11
  ]
}
```

- Generate the prime and accept the response in `xml` format  
  Request:  
  `http://localhost:9000/primes/12`  
  header 'Accept: application/xml'  
  Response:

```xml
<PrimeNumberResult>
    <initial>12</initial>
    <primes>
        <primes>1</primes>
        <primes>2</primes>
        <primes>3</primes>
        <primes>5</primes>
        <primes>7</primes>
        <primes>11</primes>
    </primes>
</PrimeNumberResult> 
```

> NOTE: Whilst making the request to generate the prime, user has the choice to select the algorithm as per their choice
> by passing an extra request parameter field as `algorithm`.
> Example: http://localhost:9000/primes/{number}?algorithm={algorithm}

### Endpoint Using Different Algorithm

- Generate prime numbers by providing parameter field as algorithm

- For `Heuristic`(Its default, if not provided the application will still use the algorithm by default) -http://localhost:9000/primes/{number}?algorithm=heuristic
- For `Brute Force` -http://localhost:9000/primes/{number}?algorithm=bruteForce

## Supported Algorithm

Currently, this application supports two algorithm which will allow the user to select it while making the request.
However, if needed, new algorithm can be developed and plugged into the application at any time, with very minimal
changes. 

Algorithms

- Brute Force
- Heuristic

## Performance

To increase the performance of the application and to reduce the load, we have implemented the `cache` in the
application. This cache will store the
prime number result init and if the user requesting to generate the same prime number within 60 minutes, application
will retrieve it from the cache instead of performing the calculation again.

Also, at the same time, to reduce the overload from the application memory and cache, we are also clearing (evicting)
the cache after every 60 minutes.

