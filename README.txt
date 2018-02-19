The task is developed using Spring Boot framework with IntelliJ.

The data persists in H2 database.
Calling endpoint /message_queue with the event type "full_sync" and itemId that does not exists in the database will create a new item in the database.
The response on /message_queue endpoint contains itemID, item's old count and new count (updated or adjusted depending on event type). 


Java and Spring Boot were picked up as the most comfortable for working. 

Open project in IntelliJ using pom.xml file.
To run tests - open Execute Maven Goal window and execute command: test -Ptest
To package the application - open Execute Maven Goal window and execute command: package
inventory-0.0.1-SNAPSHOT.jar will be generated in //inventory/target folder

To run application 
	in command prompt - 
	cd [PARENT_FOLDER]/inventory/target
	java -jar inventory-0.0.1-SNAPSHOT.jar

Use any REST client application (e.g. Postman) to call endpoints on localhost:9091
