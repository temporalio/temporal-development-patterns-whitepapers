# Implementing a Temporal Workflow for a Document Publishing Use Case Using the Temporal Java SDK

The purpose of the project is to demonstrate how to implement a fictitious document publishing workflow use case in Temporal. The project uses the [Temporal Java SDK](https://docs.temporal.io/docs/java/introduction).

The workflow has three activities:

- Copy Edit
- Graphic Edit
- Publish

The Copy Edit and Graphic Edit activities execute concurrently. Upon completion of both activities, the Publish activity executes as illustrated in the figure below.

![statemachine-03](https://github.com/reselbob/publishing-statemachine/assets/1110569/488b624d-c8fb-46bc-9e6b-85c0e9ebea2f)

The thing to notice about the Temporal code is that the sequence of activities is defined in the
file [PublicationWorkflowImpl.java](src/main/java/publishingdemo/PublicationWorkflowImpl.java). However, the code for each
activity in the workflow is defined in the file [PublishingActivitiesImpl.java](src/main/java/publishingdemo/PublishingActivitiesImpl.java).

Programmatically separating the workflow from its activities is a key feature of Temporal. The separation of concerns makes it possible to change  the sequence
of activities in the workflow without having to change the code for the activities. Also, the separation of concerns – workflow from activities –
makes it possible to add new activities to the workflow easily. For example, if a developer wanted to add a new activity to the workflow, say, `LegalReview`,
the developer adds a declaration for an activity method named `legalReview()` to the [`PublishingActivities`](src/main/java/publishingdemo/PublishingActivities.java) interface 
file annotating the method with an
[`@ActivityMethod`](https://docs.temporal.io/dev-guide/java/foundations#develop-activities) annotation. Then, the developer
implements the activity behavior in the `PublishingActivitiesImpl` class. The only thing that left to do is to modify the sequence of activities to the
workflow file [PublicationWorkflowImpl.java](src/main/java/publishingdemo/PublicationWorkflowImpl.java).

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
[[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------------< publishingdemo:app >-------------------------
[INFO] Building app 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.1.0:java (default-cli) @ app ---
[publishingdemo.App.main()] INFO io.temporal.serviceclient.WorkflowServiceStubsImpl - Created WorkflowServiceStubs for channel: ManagedChannelOrphanWrapper{delegate=ManagedChannelImpl{logId=1, target=127.0.0.1:7233}}
[publishingdemo.App.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Workflow Poller taskQueue="PublishingDemo", namespace="default", identity=39369@mycomputer.lan}
[publishingdemo.App.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Activity Poller taskQueue="PublishingDemo", namespace="default", identity=39369@mycomputer.lan}
[publishingdemo.App.main()] INFO publishingdemo.App - The worker has started and is listening on task queue: PublishingDemo.
[workflow-method-650beab5-caaf-4a2c-9897-748443057252-b833cc04-14b3-4a07-ae48-7d39c9f48aec] INFO publishingdemo.PublicationWorkflowImpl - Starting workflow for publishing document: https://learn.temporal.io/getting_started/#set-up-your-development-environment
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Copy Edit the document id: 650beab5-caaf-4a2c-9897-748443057252 at URL https://learn.temporal.io/getting_started/#set-up-your-development-environment. STARTING COPY EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - Copy edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Graphic Edit the document id: 650beab5-caaf-4a2c-9897-748443057252 at URL https://learn.temporal.io/getting_started/#set-up-your-development-environment. STARTING GRAPHIC EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - Graphic edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Publish the document id: 650beab5-caaf-4a2c-9897-748443057252 at URL https://learn.temporal.io/getting_started/#set-up-your-development-environment. STARTING PUBLISH NOW!
[workflow-method-650beab5-caaf-4a2c-9897-748443057252-b833cc04-14b3-4a07-ae48-7d39c9f48aec] INFO publishingdemo.PublicationWorkflowImpl - Publishing complete for document: https://learn.temporal.io/getting_started/#set-up-your-development-environment
[workflow-method-f2c4312c-8ec5-437b-84da-997c7f1e6414-854c3306-f3c1-401b-b4c3-d0d8fff043ae] INFO publishingdemo.PublicationWorkflowImpl - Starting workflow for publishing document: https://docs.temporal.io/docs/java/hello-world
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Copy Edit the document id: f2c4312c-8ec5-437b-84da-997c7f1e6414 at URL https://docs.temporal.io/docs/java/hello-world. STARTING COPY EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - Copy edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Graphic Edit the document id: f2c4312c-8ec5-437b-84da-997c7f1e6414 at URL https://docs.temporal.io/docs/java/hello-world. STARTING GRAPHIC EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - Graphic edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Publish the document id: f2c4312c-8ec5-437b-84da-997c7f1e6414 at URL https://docs.temporal.io/docs/java/hello-world. STARTING PUBLISH NOW!
[workflow-method-f2c4312c-8ec5-437b-84da-997c7f1e6414-854c3306-f3c1-401b-b4c3-d0d8fff043ae] INFO publishingdemo.PublicationWorkflowImpl - Publishing complete for document: https://docs.temporal.io/docs/java/hello-world
[workflow-method-3e184e45-34a2-4a46-b7a2-3a34b9a6d568-a79f635d-bf73-4817-a310-35f823d2cf3b] INFO publishingdemo.PublicationWorkflowImpl - Starting workflow for publishing document: https://docs.temporal.io/docs/server/production-deployment
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Graphic Edit the document id: 3e184e45-34a2-4a46-b7a2-3a34b9a6d568 at URL https://docs.temporal.io/docs/server/production-deployment. STARTING GRAPHIC EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - Graphic edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Copy Edit the document id: 3e184e45-34a2-4a46-b7a2-3a34b9a6d568 at URL https://docs.temporal.io/docs/server/production-deployment. STARTING COPY EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - Copy edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Publish the document id: 3e184e45-34a2-4a46-b7a2-3a34b9a6d568 at URL https://docs.temporal.io/docs/server/production-deployment. STARTING PUBLISH NOW!
[workflow-method-3e184e45-34a2-4a46-b7a2-3a34b9a6d568-a79f635d-bf73-4817-a310-35f823d2cf3b] INFO publishingdemo.PublicationWorkflowImpl - Publishing complete for document: https://docs.temporal.io/docs/server/production-deployment
[publishingdemo.App.main()] INFO io.temporal.worker.WorkerFactory - shutdown: WorkerFactory{identity=39369@mycomputer.lan}
[publishingdemo.App.main()] INFO io.temporal.internal.worker.Poller - shutdown: Poller{name=Workflow Poller taskQueue="PublishingDemo", namespace="default", identity=39369@mycomputer.lan}
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 5] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 3] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 2] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 4] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 1] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[publishingdemo.App.main()] INFO io.temporal.internal.worker.Poller - shutdown: Poller{name=Activity Poller taskQueue="PublishingDemo", namespace="default", identity=39369@mycomputer.lan}
[Activity Poller taskQueue="PublishingDemo", namespace="default": 4] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 2] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 5] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 3] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 1] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[publishingdemo.App.main()] INFO publishingdemo.App - The worker has been shutdown. That's all folks!
```
