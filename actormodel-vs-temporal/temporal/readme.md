# Adapting the Actor Model to a Temporal Workflow

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
[shoppingcartdemo.App.main()] INFO io.temporal.serviceclient.WorkflowServiceStubsImpl - Created WorkflowServiceStubs for channel: ManagedChannelOrphanWrapper{delegate=ManagedChannelImpl{logId=1, target=127.0.0.1:7233}}
[shoppingcartdemo.App.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Workflow Poller taskQueue="ShoppingCartDemo", namespace="default", identity=23192@bobs-mac-mini.lan}
[shoppingcartdemo.App.main()] INFO io.temporal.internal.worker.Poller - start: Poller{name=Activity Poller taskQueue="ShoppingCartDemo", namespace="default", identity=23192@bobs-mac-mini.lan}
[shoppingcartdemo.App.main()] INFO shoppingcartdemo.App - The worker has started and is listening on task queue: ShoppingCartDemo.
[workflow-method-11f54be4-ed56-4c5d-b7bf-76342bc5fcbb-6158f809-8ef8-4550-a522-9d581cd6d6e9] INFO shoppingcartdemo.temporal.ShoppingCartWorkflowImpl - Shopping cart started
[signal addItems] INFO shoppingcartdemo.temporal.ShoppingCartWorkflowImpl - Adding items to the shopping cart
[signal checkout] INFO shoppingcartdemo.temporal.ShoppingCartWorkflowImpl - Checking out the shopping cart
[Activity Executor taskQueue="ShoppingCartDemo", namespace="default": 1] INFO shoppingcartdemo.temporal.ShoppingCartActivitiesImpl - I am Amazing Shopping Cart AI and Robotics. I have the smarts to pay to the Shopping Cart with data: [shoppingcartdemo.model.PurchaseItem@adb6e0f, shoppingcartdemo.model.PurchaseItem@5d2578dd, shoppingcartdemo.model.PurchaseItem@55854c78, shoppingcartdemo.model.PurchaseItem@14b27883, shoppingcartdemo.model.PurchaseItem@4548d70c] shoppingcartdemo.model.CheckoutInfo@33ddbce8
[Activity Executor taskQueue="ShoppingCartDemo", namespace="default": 1] INFO shoppingcartdemo.temporal.ShoppingCartActivitiesImpl - I am Amazing Shopping Cart AI and Robotics. I have the smarts to getItemsFromInventory to the Shopping Cart with data: [shoppingcartdemo.model.PurchaseItem@38aa6b0c, shoppingcartdemo.model.PurchaseItem@400115b2, shoppingcartdemo.model.PurchaseItem@197c37d0, shoppingcartdemo.model.PurchaseItem@5a4eafb9, shoppingcartdemo.model.PurchaseItem@31575df7]
[Activity Executor taskQueue="ShoppingCartDemo", namespace="default": 1] INFO shoppingcartdemo.temporal.ShoppingCartActivitiesImpl - I am Amazing Shopping Cart AI and Robotics. I have the smarts to ship to the Shopping Cart with data: purchaseItems: [shoppingcartdemo.model.PurchaseItem@2e954c44, shoppingcartdemo.model.PurchaseItem@25261484, shoppingcartdemo.model.PurchaseItem@47e006f8, shoppingcartdemo.model.PurchaseItem@ee512ef, shoppingcartdemo.model.PurchaseItem@a96981b] |  checkoutInfo: shoppingcartdemo.model.CheckoutInfo@1f012ff9
[workflow-method-11f54be4-ed56-4c5d-b7bf-76342bc5fcbb-6158f809-8ef8-4550-a522-9d581cd6d6e9] INFO shoppingcartdemo.temporal.ShoppingCartWorkflowImpl - Shopping cart completed
[shoppingcartdemo.App.main()] INFO io.temporal.worker.WorkerFactory - shutdown: WorkerFactory{identity=23192@bobs-mac-mini.lan}
[shoppingcartdemo.App.main()] INFO io.temporal.internal.worker.Poller - shutdown: Poller{name=Workflow Poller taskQueue="ShoppingCartDemo", namespace="default", identity=23192@bobs-mac-mini.lan}
[Workflow Poller taskQueue="ShoppingCartDemo", namespace="default": 2] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="ShoppingCartDemo", namespace="default": 1] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="ShoppingCartDemo", namespace="default": 5] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="ShoppingCartDemo", namespace="default": 4] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[Workflow Poller taskQueue="ShoppingCartDemo", namespace="default": 3] INFO io.temporal.internal.worker.Poller - poll loop is terminated: WorkflowPollTask
[shoppingcartdemo.App.main()] INFO io.temporal.internal.worker.Poller - shutdown: Poller{name=Activity Poller taskQueue="ShoppingCartDemo", namespace="default", identity=23192@bobs-mac-mini.lan}
[Activity Poller taskQueue="ShoppingCartDemo", namespace="default": 1] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="ShoppingCartDemo", namespace="default": 2] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="ShoppingCartDemo", namespace="default": 4] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="ShoppingCartDemo", namespace="default": 5] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[Activity Poller taskQueue="ShoppingCartDemo", namespace="default": 3] INFO io.temporal.internal.worker.Poller - poll loop is terminated: ActivityPollTask
[shoppingcartdemo.App.main()] INFO shoppingcartdemo.App - The worker has been shutdown. That's all folks!
```
