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

You'll see output similar to the output shown below. Also, be advised, the workflow includes a Temporal Saga. The Saga
provides the ability to compensate for a failed workflow. In the example below, the workflow fails because the  `Publish`
activity is not allowed to execute on a Sunday. The workflow compensates for the failure by executing a compensation that resets
the `canPublishOnSunday` flag in [PublishingActivitiesImpl](https://github.com/reselbob/publishing-statemachine/blob/a0d4dfcf1a38ba3b31686540440a6b8c4f21b24b/temporal/src/main/java/publishingdemo/PublishingActivitiesImpl.java#L36)
to `true`.

The workflow then restarts the workflow. This time the `Publish` activity executes successfully. As shown in the screenshot of the Temporal Web UI below.

![Screenshot 2023-12-19 at 5 51 07 PM](https://github.com/reselbob/publishing-statemachine/assets/1110569/d7f7d026-3068-4787-b100-baee12dedd71)


```text                                                                                                                                                                                $ mvn exec:java -Dexec.mainClass="publishingdemo.App"
Enter the TASK QUEUE name: 

You did not enter a value for TASK QUEUE to we'll use the default value: PublishingDemo
[main] INFO io.temporal.serviceclient.WorkflowServiceStubsImpl - Created WorkflowServiceStubs for channel: ManagedChannelOrphanWrapper{delegate=ManagedChannelImpl{logId=1, target=127.0.0.1:7233}}
[main] INFO io.temporal.internal.worker.Poller - start: Poller{name=Workflow Poller taskQueue="PublishingDemo", namespace="default", identity=86243@bobs-mac-mini.lan}
[main] INFO io.temporal.internal.worker.Poller - start: Poller{name=Activity Poller taskQueue="PublishingDemo", namespace="default", identity=86243@bobs-mac-mini.lan}
[main] INFO publishingdemo.App - The worker has started and is listening on task queue: PublishingDemo.
Enter 'exit' to exit or any other key to add a new Document URL: 

Enter Document URL: 
https://www.thesaurus.com/browse/milieu
Enter 'exit' to exit or any other key to add a new Document URL: 
[workflow-method-4311d1cd-c52a-4652-bdd0-dec9b85e4ef7-5ae2aaf4-e5ae-4ce7-8a6d-dc45159b03c5] INFO publishingdemo.PublicationWorkflowImpl - Starting Workflow for Publishing
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Graphic Edit the document id: 4311d1cd-c52a-4652-bdd0-dec9b85e4ef7 at URL https://www.thesaurus.com/browse/milieu. STARTING GRAPHIC EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Copy Edit the document id: 4311d1cd-c52a-4652-bdd0-dec9b85e4ef7 at URL https://www.thesaurus.com/browse/milieu. STARTING COPY EDIT NOW!
[workflow-method-4311d1cd-c52a-4652-bdd0-dec9b85e4ef7-5ae2aaf4-e5ae-4ce7-8a6d-dc45159b03c5] INFO publishingdemo.PublicationWorkflowImpl - Copy edit complete
[workflow-method-4311d1cd-c52a-4652-bdd0-dec9b85e4ef7-5ae2aaf4-e5ae-4ce7-8a6d-dc45159b03c5] INFO publishingdemo.PublicationWorkflowImpl - Graphic edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] WARN io.temporal.internal.activity.ActivityTaskExecutors$ActivityTaskExecutor - Activity failure. ActivityId=459f6d59-3a2d-3bac-b108-d8c62cbb6008, activityType=Publish, attempt=1
java.lang.RuntimeException: Cannot publish on Sunday
	at publishingdemo.PublishingActivitiesImpl.publish(PublishingActivitiesImpl.java:26)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at io.temporal.internal.activity.RootActivityInboundCallsInterceptor$POJOActivityInboundCallsInterceptor.executeActivity(RootActivityInboundCallsInterceptor.java:64)
	at io.temporal.internal.activity.RootActivityInboundCallsInterceptor.execute(RootActivityInboundCallsInterceptor.java:43)
	at io.temporal.internal.activity.ActivityTaskExecutors$BaseActivityTaskExecutor.execute(ActivityTaskExecutors.java:107)
	at io.temporal.internal.activity.ActivityTaskHandlerImpl.handle(ActivityTaskHandlerImpl.java:124)
	at io.temporal.internal.worker.ActivityWorker$TaskHandlerImpl.handleActivity(ActivityWorker.java:278)
	at io.temporal.internal.worker.ActivityWorker$TaskHandlerImpl.handle(ActivityWorker.java:243)
	at io.temporal.internal.worker.ActivityWorker$TaskHandlerImpl.handle(ActivityWorker.java:216)
	at io.temporal.internal.worker.PollTaskExecutor.lambda$process$0(PollTaskExecutor.java:105)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:840)
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - Compensating publish for document id: 4311d1cd-c52a-4652-bdd0-dec9b85e4ef7 at URL https://www.thesaurus.com/browse/milieu
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - Compensating graphicEdit for document id: 4311d1cd-c52a-4652-bdd0-dec9b85e4ef7 at URL https://www.thesaurus.com/browse/milieu
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - Compensating copyEdit for document id: 4311d1cd-c52a-4652-bdd0-dec9b85e4ef7 at URL https://www.thesaurus.com/browse/milieu
[workflow-method-4311d1cd-c52a-4652-bdd0-dec9b85e4ef7-5ae2aaf4-e5ae-4ce7-8a6d-dc45159b03c5] WARN io.temporal.internal.sync.WorkflowExecutionHandler - Workflow execution failure WorkflowId='4311d1cd-c52a-4652-bdd0-dec9b85e4ef7', RunId=5ae2aaf4-e5ae-4ce7-8a6d-dc45159b03c5, WorkflowType='PublicationWorkflow'
io.temporal.failure.ActivityFailure: Activity with activityType='Publish' failed: 'Activity task failed'. scheduledEventId=14, startedEventId=15, activityId=459f6d59-3a2d-3bac-b108-d8c62cbb6008, identity='86243@bobs-mac-mini.lan', retryState=RETRY_STATE_MAXIMUM_ATTEMPTS_REACHED
	at java.base/java.lang.Thread.getStackTrace(Thread.java:1619)
	at io.temporal.internal.sync.CompletablePromiseImpl.throwFailure(CompletablePromiseImpl.java:137)
	at io.temporal.internal.sync.CompletablePromiseImpl.getImpl(CompletablePromiseImpl.java:96)
	at io.temporal.internal.sync.CompletablePromiseImpl.get(CompletablePromiseImpl.java:75)
	at publishingdemo.PublicationWorkflowImpl.startWorkflow(PublicationWorkflowImpl.java:79)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at io.temporal.internal.sync.POJOWorkflowImplementationFactory$POJOWorkflowImplementation$RootWorkflowInboundCallsInterceptor.execute(POJOWorkflowImplementationFactory.java:339)
	at io.temporal.internal.sync.POJOWorkflowImplementationFactory$POJOWorkflowImplementation.execute(POJOWorkflowImplementationFactory.java:314)
	at io.temporal.internal.sync.WorkflowExecutionHandler.runWorkflowMethod(WorkflowExecutionHandler.java:70)
	at io.temporal.internal.sync.SyncWorkflow.lambda$start$0(SyncWorkflow.java:135)
	at io.temporal.internal.sync.CancellationScopeImpl.run(CancellationScopeImpl.java:102)
	at io.temporal.internal.sync.WorkflowThreadImpl$RunnableWrapper.run(WorkflowThreadImpl.java:107)
	at io.temporal.worker.ActiveThreadReportingExecutor.lambda$submit$0(ActiveThreadReportingExecutor.java:53)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:539)
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:840)
Caused by: io.temporal.failure.ApplicationFailure: message='Cannot publish on Sunday', type='java.lang.RuntimeException', nonRetryable=false
	at publishingdemo.PublishingActivitiesImpl.publish(PublishingActivitiesImpl.java:26)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method:0)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at io.temporal.internal.activity.RootActivityInboundCallsInterceptor$POJOActivityInboundCallsInterceptor.executeActivity(RootActivityInboundCallsInterceptor.java:64)
	at io.temporal.internal.activity.RootActivityInboundCallsInterceptor.execute(RootActivityInboundCallsInterceptor.java:43)
	at io.temporal.internal.activity.ActivityTaskExecutors$BaseActivityTaskExecutor.execute(ActivityTaskExecutors.java:107)
	at io.temporal.internal.activity.ActivityTaskHandlerImpl.handle(ActivityTaskHandlerImpl.java:124)
	at io.temporal.internal.worker.ActivityWorker$TaskHandlerImpl.handleActivity(ActivityWorker.java:278)
	at io.temporal.internal.worker.ActivityWorker$TaskHandlerImpl.handle(ActivityWorker.java:243)
	at io.temporal.internal.worker.ActivityWorker$TaskHandlerImpl.handle(ActivityWorker.java:216)
	at io.temporal.internal.worker.PollTaskExecutor.lambda$process$0(PollTaskExecutor.java:105)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	at java.base/java.lang.Thread.run(Thread.java:840)
[workflow-method-4311d1cd-c52a-4652-bdd0-dec9b85e4ef7-0aba51cc-820f-425b-be83-98a4596fc9b5] INFO publishingdemo.PublicationWorkflowImpl - Starting Workflow for Publishing
[Activity Executor taskQueue="PublishingDemo", namespace="default": 1] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Graphic Edit the document id: 4311d1cd-c52a-4652-bdd0-dec9b85e4ef7 at URL https://www.thesaurus.com/browse/milieu. STARTING GRAPHIC EDIT NOW!
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Copy Edit the document id: 4311d1cd-c52a-4652-bdd0-dec9b85e4ef7 at URL https://www.thesaurus.com/browse/milieu. STARTING COPY EDIT NOW!
[workflow-method-4311d1cd-c52a-4652-bdd0-dec9b85e4ef7-0aba51cc-820f-425b-be83-98a4596fc9b5] INFO publishingdemo.PublicationWorkflowImpl - Copy edit complete
[workflow-method-4311d1cd-c52a-4652-bdd0-dec9b85e4ef7-0aba51cc-820f-425b-be83-98a4596fc9b5] INFO publishingdemo.PublicationWorkflowImpl - Graphic edit complete
[Activity Executor taskQueue="PublishingDemo", namespace="default": 2] INFO publishingdemo.PublishingActivitiesImpl - I am Amazing AI. I have the smarts to Publish the document id: 4311d1cd-c52a-4652-bdd0-dec9b85e4ef7 at URL https://www.thesaurus.com/browse/milieu. STARTING PUBLISH NOW!
[workflow-method-4311d1cd-c52a-4652-bdd0-dec9b85e4ef7-0aba51cc-820f-425b-be83-98a4596fc9b5] INFO publishingdemo.PublicationWorkflowImpl - Publishing complete

exit
[main] INFO io.temporal.worker.WorkerFactory - shutdown: WorkerFactory{identity=86243@bobs-mac-mini.lan}
[main] INFO io.temporal.internal.worker.Poller - shutdown: Poller{name=Workflow Poller taskQueue="PublishingDemo", namespace="default", identity=86243@bobs-mac-mini.lan}
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 2] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 3] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 4] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 1] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="PublishingDemo", namespace="default": 5] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[main] INFO io.temporal.internal.worker.Poller - shutdown: Poller{name=Activity Poller taskQueue="PublishingDemo", namespace="default", identity=86243@bobs-mac-mini.lan}
[Activity Poller taskQueue="PublishingDemo", namespace="default": 2] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 4] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 3] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 5] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="PublishingDemo", namespace="default": 1] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[main] INFO publishingdemo.App - The worker has been shutdown. That's all folks!

Process finished with exit code 0
```
