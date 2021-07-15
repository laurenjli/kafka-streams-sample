# Dev Takehome Assignment

### Overview

This repository contains code that was written for the 3rivers dev-interview prompt (https://github.ibm.com/3rivers/dev-interview). 
In this repo, you will find an attempt to solve the prompt to consume from two topics with avro schemas, join them based on a foreign key, and produce the result to a new Kafka topic. 

Below, I discuss my approach, the challenges I faced, the folder/file structure, and the references I used.

### Approach
To begin, I created a Maven project using the online Spring Initializr. I tried to initialize the project with a few Kafka and Kafka Streams dependencies.

After initializing the project, I spent time looking through different references regarding how to consume from a Kafka topic that has a designated avro schema. 
There were a number of tutorials regarding this, however, it wasn't clear to me which serializer/deserializer was the best to use. I decided to try the SpecificAvroSerde suggested in the Confluent documentation. 
I faced a number of errors when adding these dependencies but was able to get them to work after some time.

Once the dependencies were working, my plan was to work in the following steps:

1. Get a Kafka Consumer working to get familiar with Kafka Streams functions and libraries. I did this without using spring boot since I saw that as an extra layer of complexity that I would try to approach later. The code for this is not included in this repo but can be shared.
   
2. Get the whole application running so that I understood the structure and the steps that needed to be taken. Again, I did this part without using spring boot. The code for this is not included in this repo but can be shared.
   
3. Convert my existing code into the spring boot framework. The code for this is shared in this repo.


### Challenges

#### Configuration/dependencies

I faced a number of difficulties when attempting this takehome assignment, primarily around setup/configuration which made it difficult to test the code that I was writing.

Throughout the time that I was coding a solution, I would come across new dependencies to add to the pom.xml file. 
However, oftentimes an error would occur when I would try to copy and paste one of the dependencies found on the Maven website into the pom.xml file (see screenshot below). This happened with a number of different dependencies. Sometimes one would work if I restarted IntelliJ or my computer, but then another might break. 
In the end, I was able to get the dependencies working and move forward with my code.

![Alt text](error.png?raw=true "Example error in pom.xml file")

I also was a bit confused about how to set up some application properties using the springboot framework. After some digging, I found some resources on how to set up
the application.yml file which ultimately made a big difference in getting my spring boot application to run.

#### Foreign key join

The foreign key join was also a challenge. I found a Kafka tutorial online that had an example of a foreign key join (https://kafka-tutorials.confluent.io/foreign-key-joins/kstreams.html). 
I tried to follow the steps, but I could not get my foreign key extractor working.

I decided to try a different method, which was to re-key the KTables to have the same key and then performing an inner join. I first tried to re-key the KStream using the .selectKey() method, but I found that it wasn't working.
After that, I decided to try the .map() method and returning a new KeyValue pair, then using groupByKey() and reduce() to have create a KTable. 
This is the method that ultimately worked for me.


### Folder structure

#### src/main/avro

This folder contains the avro schemas for the three topics: Balance, Customer, and CustomerBalance.
These avros were used to generate classes for each of the topics so that they could be imported in the streams code.

#### src/main/java/com/dev/takehome

This folder contains code that attempts to read from the Balance and Customer topic, join the two topics on accountId, and then produce the joined data to the CustomerBalance topic.
I tried to follow the PNC format and organize the classes into folders based on their usage. 

- config: This contains PropertiesConfig.java which instantiates the input/output topic names to be used in the application.

- constant: This contains Constants.java which has the constants used in the application.

- joiner: This contains CustomerBalanceJoin which is a joiner class which joins the Customer and Balance topics and sets the attributes that will be going to the CustomerBalance topic.

- kafka: This contains the SerDes.java and StoreConfig.java classes. The SerDes.java class initializes the different serializers/deserializers used in the application. The StoreConfig.java intializes the state stores that will be used.

- reducer: This contains the BalanceReducer.java and CustomerReducer.java. These classes are used when building the Balance and Customer KTables, respectively.

- stream: This contains the BuildBalanceTable.java, BuildCustomerTable.java, and BuildJoinStream.java classes. These classes are the ones that build the KStream -> KTable and ultimately perform the join between Customer and Balance.

#### src/main/java/com/ibm/gbs/schema
This folder contains the classes generated from the topics' avro schemas. I used maven to generate the objects.


## General references (specific references are noted in each class)

- https://medium.com/new-generation/apache-kafka-stream-avro-serialized-objects-in-6-steps-94c012f75588#0e3f
- https://kafka-tutorials.confluent.io/foreign-key-joins/kstreams.html
- https://docs.confluent.io/platform/current/schema-registry/serdes-develop/serdes-avro.html#
- https://docs.confluent.io/platform/current/streams/developer-guide/datatypes.html
- https://www.codota.com/code/java/methods/org.apache.kafka.streams.kstream.KStream/print
- other existing streams applications from the project 
  
  
