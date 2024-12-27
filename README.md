# Ecoflow Rest API implementation in Java

This is Ecoflow Rest API implementation in Java. It requires AccessToken and SecretToken to make requests.
Get tokens first on Ecoflow developers website: http://developer-eu.ecoflow.com

## Caution
It's an alpha version with basic API implemented. The API is not stable and can be changed.

## Installation

Maven:

```xml

<dependency>
    <groupId>io.github.tess1o</groupId>
    <artifactId>ecoflow-api</artifactId>
    <version>0.0.2</version>
</dependency>
```

Gradle:
`implementation group: 'io.github.tess1o', name: 'ecoflow-api', version: '0.0.1'`
or
`implementation 'io.github.tess1o:ecoflow-api:0.0.1'`

## Usage example

```java
HttpRestClient httpRestClient = new EcoflowHttpRestClient("accessKey", "secretKey");
EcoflowClient ecoflowClient = new EcoflowClient(httpRestClient);
//get all linked devices
List<EcoflowDevice> devices = ecoflowClient.getDevices();
// get all parameters for given device
Map<String, Object> deviceParameters = ecoflowClient.getDeviceAllParameters("device serial number");
// get specified parameters only
Map<String, Object> deviceSpecifiedParameters = ecoflowClient.getDeviceParameters("device serial number", List.of("inv.invOutAmp")); 
```

