This project is controlled by [Maven](https://maven.apache.org/).

To build the project use `mvn install` from the root directory.  
To run the game use `mvn exec:java -pl controller` from the root directory.
You can run it with the following parameters:
```
field.width=30
field.height=30
frog.scale=1
frog.number=3
snake.part.scale=1
snake.length=3
snake.movement.speed=50
logging.level.root=info
```
Values for specified parameters set by default. 
You can change them like that `mvn exec:java -pl controller -Dsnake.length=100 -Dfrog.number=100`.
