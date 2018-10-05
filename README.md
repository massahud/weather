Weather
=======

## Running

To run you need to create the file `/src/main/resources/application-development.yaml`
and add your OpenWeatherMap api key to `owm.key` property. Like bellow:

```yaml
owm:
    key: <YOUR API KEY>
``` 

After setting up the file, just run the following command
```
    ./gradlew bootRun
``` 

Go to `http://localhost:8080/api/data/Lisbon` to test if it worked. 

## API Specification

After running, the API swagger specification can be found on the "/" path. (http://localhost:8080/)

## Configuring IDE

### Lombok

for faster development, this project uses Lombok, so 
you need to add the corresponding plugin for your IDE, 
and also enable annotation processing.

#### IntelliJ Idea

for IntelliJ Idea add the lombok plugin in the 
plugins configuration, and enable annotation processing at 
preferences `[Build, Execution, Deployment] > [Compiler] > [Annotation Processors]` 