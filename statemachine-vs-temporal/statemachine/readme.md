# State Machine for Document Publication Use Case
The purpose of this project is to demonstrate how to implement a message driven state machine. This example is a Maven project.
The code uses a simple message queue that ships with the project.

The use case the statement machine implements is illustrated in the following diagram:

![statemachine-02](https://github.com/reselbob/publishing-statemachine/assets/1110569/a44328bf-259a-4a74-8d9e-fbbd2d905a17)

# Running the code:

The [Java Virtual Machine](https://openjdk.org/) and [Maven](https://maven.apache.org/install.html) need to be installed
on the host computer.

## (1) Confirm that Java and Maven are installed on the host machine

Confirm that Java is installed:

```bash
java --version
```

You'll get output similar to the following:

```bash
openjdk 18.0.2-ea 2022-07-19
OpenJDK Runtime Environment (build 18.0.2-ea+9-Ubuntu-222.04)
OpenJDK 64-Bit Server VM (build 18.0.2-ea+9-Ubuntu-222.04, mixed mode, sharing)
```
T
Confirm that Maven is installed:

```bash
mvn --version
```

```bash
Maven home: /usr/share/maven
Java version: 18.0.2, vendor: Oracle Corporation, runtime: /usr/lib/jvm/jdk-18.0.2
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.19.0-46-generic", arch: "amd64", family: "unix"
```

---

## (2) Do some maven housecleaning

Run the following command in a new terminal window to create a fresh Maven environment:

```bash
mvn clean package install
```

## (3) Start the application

In that same terminal window run:

```bash
mvn exec:java -Dexec.mainClass="demo.pubstatemachine.Client"
```

You'll get output similar to the following:

```text
Controller running
Client: Sending event: EVENT_EDITABLE
Sending message: EVENT_EDITABLE
Client: Sent event to controller: EVENT_EDITABLE
Controller received message: EVENT_EDITABLE
Now in the Editable state
Editable is now updating EVENT_EDITABLE
Controller received message: COMMAND_GRAPHIC_EDIT
Now in the GraphicEdit state
GraphicEdit is now updating COMMAND_GRAPHIC_EDIT
Controller received message: COMMAND_COPY_EDIT
Now in the CopyEdit state
CopyEdit is now updating COMMAND_COPY_EDIT
Controller received message: EVENT_AWAIT_COPY_EDIT
Now in the AwaitingEdits state
AwaitingEdits is now updating EVENT_AWAIT_COPY_EDIT
Doing copy edit behavior
Controller received message: EVENT_AWAIT_GRAPHIC_EDIT
Now in the AwaitingEdits state
AwaitingEdits is now updating EVENT_AWAIT_GRAPHIC_EDIT
Doing graphic edit behavior
Controller received message: COMMAND_PUBLISH
Now in the Publish state
Publish is now updating COMMAND_PUBLISH
Controller received message: EVENT_PUBLISHED
Document published
```
