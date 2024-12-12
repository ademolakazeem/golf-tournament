
# Golf Tournament Project #

This project is an application/service designed to model a fixed layout of a golf course as an array of par scores. In this exercise, the course will have the following layout of par scores for its 18 holes: {4,5,3,4,5,4,4,3,4,4,4,4,4,5,4,3,5,3}.
This application is developed with Level 2 of the [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html) in mind.

## Contact ##
 
 * [Ademola Kazeem](mailto:w.ademola.kazeem@gmail.com)

## Uris ##
 * http://localhost:8080/swagger-ui/index.html#/
 * https://localhost:8080/api/tournament/scores
 * https://localhost:8080/api/tournament/leaderboard

## Architecture ##
The Architecture shows the high level visual display of how each component work together, 
but this project only contains the Spring boot REST API how with Apache Kafka Implementation. 
The frontend is not implemented, but could be implemented with React.js, Angular or VUE.js frontend or any other frontend frameworks whenever needed.

diagram here.

The system is scalable and can handle multiple concurrent score submissions while maintaining consistency in the leaderboard updates.

## Key Design Principles Demonstrated:

* Single Responsibility Principle :
  * Each section handles one specific aspect of score processing 
  * Clear separation of concerns between validation, calculation, and updates

* Dependency Injection :
  * Uses GolfCourseService for course-related operations 
  * Promotes loose coupling and testability

* Thread Safety :
  * Uses ConcurrentHashMap for thread-safe player management 
  * Important for concurrent score submissions

* Error Handling :
  * Validates input before processing 
  * Throws appropriate exceptions for invalid data

* Real-time Updates :
  * Maintains current state 
  * Publishes updates for real-time viewer experience

* Immutable State :
  * Score history is preserved 
  * Each update adds to the history rather than modifying it

* Event-Driven Architecture :
  * Publishes events for significant occurrences 
  * Allows for loose coupling between components

This design of this application demonstrates design with consideration for:

* Concurrency 
* Data consistency 
* Real-time updates 
* Separation of concerns 
* Maintainability 
* Scalability


## Prerequisites for building ##
 * Java 17
 * Kafka 2.x

## Scope of the Project ##
The following contains the list of the functionalities of the application:

* This application maintains a concurrent map of players and their scores by letting you create players of the tournament with information including: `name`, `holesCompleted`, `scoreRelativeToPar` etc. 
* It processes score updates and calculates relative-to-par scores
* Players will enter their scores after completing each hole
* If player exists, the scores just gets added until the hole is completed.
* You can create scores for each players with information including `score`, `holeNumber` etc.
* Whenever a player scores -1 or lower on a hole, alert all users viewing the leaderboard.
* Leaderboard is maintained in REAL TIME showing the players ranked by their scores relative to par.
* The leaderboard could be viewed at any time during the input of the scores to see who's leading.
* Whenever a player scores under par, an alert is sent to the users viewing the leaderboard.
* Once a player has completed all 18 holes show their total score on the leaderboard.
* It uses Kafka for event distribution.
* It provides REST endpoints for score submission and leaderboard retrieval.
* It supports WebSocket for real-time updates

## Out of Scope ##
Because of time constraints:
* This project does not have database connection. Typically, the score is usually stored in a database e.g. DynamoDB, MongoDB etc and can be viewed overtime. 
* If desired, the database to keep scores and players and tournament information could be added.
* This project does not batch process scores
* This project does not keep track of the tournament names, just broadcasting of the players information including: names, scores, leaderboard and notifications/alerts to viewers, among others

## Requirements (Prerequisites) ##

* Maven
* Kafka
* Java

## Installing Kafka 2.x ##

* The version of Kafka used in this project is `kafka_2.13-3.9.0`
* To install this version, simply go to [Apache Software Foundation](https://www.apache.org/dyn/closer.cgi?path=/kafka/3.9.0/kafka_2.13-3.9.0.tgz) to download this version or later version 
* Run the following commands:

    tar kafka_2.13-3.9.0.tgz 
    cd kafka_2.13-3.9.0

* Now open another commandline interface and run the following commands to start Kafka with Zookeeper: 


    # Start the ZooKeeper service
    bin/zookeeper-server-start.sh config/zookeeper.properties

* Open another terminal session again and run: 


    # Start the Kafka broker service
    bin/kafka-server-start.sh config/server.properties

* You can also run the following commands to see the consumer in real time in your command prompt


    $ bin/kafka-console-consumer.sh --topic leaderboard-updates --from-beginning --bootstrap-server localhost:9092

* You can also run the following commands to see the consumer in real time in your command prompt


    $ bin/kafka-console-consumer.sh --topic golf-alerts --from-beginning --bootstrap-server localhost:9092


* Where `leaderboard-updates` and `golf-alerts` respectively are the topics that you subscribed to and you will see information there in real time.

## Deployment ##

* Clone or Download this project 
* Make sure the `Installing Kafka 2.x` section above is carried out first.
* Run `$ mvn clean install` in order to build the project 
* Next, Run `$ mvn spring-boot:run` to run the Spring Boot application
If everything goes as planned, your application should be able to open in port: 8080
Please do not forget to add swagger-ui.html in front of the localhost, i.e: http://localhost:8080/swagger-ui/index.html#/

## How to use this Application ##

* Players visits http://localhost:8080/swagger-ui/index.html#/, then submit scores via POST requests.
* The system processes scores and updates the leaderboard.
* Kafka implementation in the application distributes updates and alerts 
* Clients receive real-time updates via WebSocket 
* The leaderboard shows players sorted by their scores relative to par

