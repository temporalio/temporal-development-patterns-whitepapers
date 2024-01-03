# Implementing a Temporal Workflow for a Publishing Use Case using the Java SDK

The purpose of the project is to demonstrate how to implement a workflow under Temporal for a document publishing use case. The project uses the [Temporal Java SDK](https://docs.temporal.io/docs/java/introduction).

The workflow has three activities:

- CopyEdit
- Graphic Edit
- Publish

The Copy Edit and Graphic Edit activities execute concurrently. Upon completion of both activities, the Publish activity executes as illustrated in the figure below.

![statemachine-03](https://github.com/reselbob/publishing-statemachine/assets/1110569/488b624d-c8fb-46bc-9e6b-85c0e9ebea2f)


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

## (2) Download and install the Temporal CLI (which includes the server)

If you do not have the Temporal server installed, click the link below to go to the Temporal documentation that has the
instructions for installing the Temporal CLI.

[https://docs.temporal.io/cli/#installation](https://docs.temporal.io/cli/#installation)

The Temporal development server ships with the CLI.

---

## (3) Start the Temporal Server

Here is the command for starting the Temporal Server on a local Ubuntu machine. Execute the command in a terminal
window.

```bash
temporal server start-dev --dynamic-config-value frontend.enableUpdateWorkflowExecution=true
```

---

## (4) Do some maven housecleaning

Run the following command in a new terminal window to create a fresh Maven environment:

```bash
mvn clean package install
```

## (5) Start the application

In that same terminal window run:

```bash
mvn exec:java -Dexec.mainClass="publishingdemo.App"
```

You'll see output similar to the following:

```text                                                                                                                                                                                $ mvn exec:java -Dexec.mainClass="publishingdemo.App"
$ mvn exec:java -Dexec.mainClass="publishingdemo.App"
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------------< publishingdemo:app >-------------------------
[INFO] Building app 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.1.0:java (default-cli) @ app ---
Enter the TASK QUEUE name: 

You did not enter a value for TASK QUEUE to we'll use the default value: PublishingDemo
[publishingdemo.App.main()] INFO io.temporal.serviceclient.WorkflowServiceStubsImpl - Created WorkflowServiceStubs for channel: ManagedChannelOrphanWrapper{delegate=ManagedChannelImpl{logId=1, target=127.0.0.1:7233}}
[publishingdemo.App.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Workflow Poller taskQueue="PublishingDemo", namespace="default", identity=27063@bobs-mac-mini.lan}
[publishingdemo.App.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Activity Poller taskQueue="PublishingDemo", namespace="default", identity=27063@bobs-mac-mini.lan}
[publishingdemo.App.main()] INFO publishingdemo.App - The worker has started and is listening on task queue: PublishingDemo.
Enter 'exit' to exit or any other key to add a new Document URL: 

Enter Document URL: 

You did not enter a value for Document URL so we'll use the default value: https://learn.temporal.io/getting_started/#set-up-your-development-environment
Enter 'exit' to exit or any other key to add a new Document URL: 
[workflow-method-33a8c54e-7e82-4cd7-b229-23690a29ff26-ac1fa5e5-caf0-4413-abf0-0ab5426c5d01] INFO publishingdemo.PublicationWorkflowImpl - Starting Workflow for Publishing
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Copy Edit the document id: 33a8c54e-7e82-4cd7-b229-23690a29ff26 at URL https://learn.temporal.io/getting_started/#set-up-your-development-environment. STARTING COPY EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Graphic Edit the document id: 33a8c54e-7e82-4cd7-b229-23690a29ff26 at URL https://learn.temporal.io/getting_started/#set-up-your-development-environment. STARTING GRAPHIC EDIT NOW!
[workflow-method-33a8c54e-7e82-4cd7-b229-23690a29ff26-ac1fa5e5-caf0-4413-abf0-0ab5426c5d01] INFO publishingdemo.PublicationWorkflowImpl - Copy edit complete
[workflow-method-33a8c54e-7e82-4cd7-b229-23690a29ff26-ac1fa5e5-caf0-4413-abf0-0ab5426c5d01] INFO publishingdemo.PublicationWorkflowImpl - Graphic edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Publish the document id: 33a8c54e-7e82-4cd7-b229-23690a29ff26 at URL https://learn.temporal.io/getting_started/#set-up-your-development-environment. STARTING PUBLISH NOW!
[workflow-method-33a8c54e-7e82-4cd7-b229-23690a29ff26-ac1fa5e5-caf0-4413-abf0-0ab5426c5d01] INFO publishingdemo.PublicationWorkflowImpl - Publishing complete

Enter Document URL: 
https://en.wikipedia.org/wiki/Workflow_pattern     
Enter 'exit' to exit or any other key to add a new Document URL: 
[workflow-method-870a4300-3681-4694-a314-7b78563ed928-3a07093e-1551-436a-b0c5-ea7f6cb8bdb7] INFO publishingdemo.PublicationWorkflowImpl - Starting Workflow for Publishing
[Activity Executor taskQueue="PublishingDemo", namespace="default": 3] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Copy Edit the document id: 870a4300-3681-4694-a314-7b78563ed928 at URL https://en.wikipedia.org/wiki/Workflow_pattern. STARTING COPY EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 4] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Graphic Edit the document id: 870a4300-3681-4694-a314-7b78563ed928 at URL https://en.wikipedia.org/wiki/Workflow_pattern. STARTING GRAPHIC EDIT NOW!
[workflow-method-870a4300-3681-4694-a314-7b78563ed928-3a07093e-1551-436a-b0c5-ea7f6cb8bdb7] INFO publishingdemo.PublicationWorkflowImpl - Copy edit complete
[workflow-method-870a4300-3681-4694-a314-7b78563ed928-3a07093e-1551-436a-b0c5-ea7f6cb8bdb7] INFO publishingdemo.PublicationWorkflowImpl - Graphic edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 3] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Publish the document id: 870a4300-3681-4694-a314-7b78563ed928 at URL https://en.wikipedia.org/wiki/Workflow_pattern. STARTING PUBLISH NOW!
[workflow-method-870a4300-3681-4694-a314-7b78563ed928-3a07093e-1551-436a-b0c5-ea7f6cb8bdb7] INFO publishingdemo.PublicationWorkflowImpl - Publishing complete
exit
[publishingdemo.App.main()] INFO io.temporal.worker.WorkerFactory - shutdown: WorkerFactory{identity=27063@bobs-mac-mini.lan}
[publishingdemo.App.main()] INFO io.temporal.internal.worker.Poller - shutdown: Poller{name=Workflow Poller taskQueue="PublishingDemo", namespace="default", identity=27063@bobs-mac-mini.lan}
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 1] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 3] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 5] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 2] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 4] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[publishingdemo.App.main()] INFO io.temporal.internal.worker.Poller - shutdown: Poller{name=Activity Poller taskQueue="PublishingDemo", namespace="default", identity=27063@bobs-mac-mini.lan}
[Activity Poller taskQueue="PublishingDemo", namespace="default": 1] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 4] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 3] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 2] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 5] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[publishingdemo.App.main()] INFO publishingdemo.App - The worker has been shutdown. That's all folks!

```
