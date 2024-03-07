# Adapting the Actor Model to a Temporal Workflow

![temporal-shop-backorder-01](https://github.com/temporalio/temporal-development-patterns-whitepapers/assets/1110569/d59dad5a-c0b1-49cf-b3db-cb6c1692d80c)


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

## (5) Start the Workers

In that same terminal window run:

```bash
mvn exec:java -Dexec.mainClass="shoppingcartdemo.WorkerRunner"
```

You get output similar to the following:

```text
reselbob@mycomputer temporal % mvn exec:java -Dexec.mainClass="shoppingcartdemo.WorkerRunner"
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------------< publishingdemo:app >-------------------------
[INFO] Building app 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.1.0:java (default-cli) @ app ---
[shoppingcartdemo.WorkerRunner.main()] INFO io.temporal.serviceclient.WorkflowServiceStubsImpl - Created WorkflowServiceStubs for channel: ManagedChannelOrphanWrapper{delegate=ManagedChannelImpl{logId=1, target=127.0.0.1:7233}}
[shoppingcartdemo.WorkerRunner.main()] INFO shoppingcartdemo.WorkerRunner - The Shopping Cart Worker has started and is listening on task queue: ShoppingCartDemo.
[shoppingcartdemo.WorkerRunner.main()] INFO shoppingcartdemo.WorkerRunner - The Backorder Worker has started and is listening on task queue: BackorderDemo.
[shoppingcartdemo.WorkerRunner.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Workflow Poller taskQueue="ShoppingCartDemo", namespace="default", identity=43927@mycomputer.lan}
[shoppingcartdemo.WorkerRunner.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Activity Poller taskQueue="ShoppingCartDemo", namespace="default", identity=43927@mycomputer.lan}
[shoppingcartdemo.WorkerRunner.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Workflow Poller taskQueue="BackorderDemo", namespace="default", identity=43927@mycomputer.lan}
[shoppingcartdemo.WorkerRunner.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Activity Poller taskQueue="BackorderDemo", namespace="default", identity=43927@mycomputer.lan}
```

## (6) Start the Application

In a separate terminal window run:

```bash
mvn exec:java -Dexec.mainClass="shoppingcartdemo.App"
```

You get output similar to the following:

```text
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------------< publishingdemo:app >-------------------------
[INFO] Building app 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- exec:3.1.0:java (default-cli) @ app ---
[shoppingcartdemo.App.main()] INFO shoppingcartdemo.App - Starting the Shopping Cart Demo bound to Task Queue: ShoppingCartDemo
[shoppingcartdemo.App.main()] INFO io.temporal.serviceclient.WorkflowServiceStubsImpl - Created WorkflowServiceStubs for channel: ManagedChannelOrphanWrapper{delegate=ManagedChannelImpl{logId=1, target=127.0.0.1:7233}}

```

## (7) View the results in the Temporal Web Console

In your browser, go to: `http://localhost:8233`

![Screenshot 2024-02-07 at 11 05 15 AM](https://github.com/temporalio/temporal-development-patterns-whitepapers/assets/1110569/feb73692-507a-435a-900c-651c14a37b5c)


