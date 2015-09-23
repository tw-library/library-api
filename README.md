# library-api [![Build Status](https://snap-ci.com/tw-library/library-api/branch/master/build_image)](https://snap-ci.com/tw-library/library-api/branch/master)


## Setup development environment

Goes to the library-app repository and follow the instructions to setup your environment.
Ps: It is not required, but will do most of the things automatically.

## Accessing the live application

`http://tw-library-api.herokuapp.com/`

## Running the api

1. At the project folder, run the following command:
  `gradle bootRun`
2. Access the web application:
  `http://localhost:8080/`
   This should list you the available endpoints.

## Testing

`APP_ENV=test gradle test`

## Remote Debugging

If you are using a virtual machine for development then you may need to debug it some time.
When that time comes, just create remote debug configuration on IntelliJ pointing to `localhost:8081`.
