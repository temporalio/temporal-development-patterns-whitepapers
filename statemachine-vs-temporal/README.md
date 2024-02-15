# Implementing the Principles of State Machines Using the Temporal Java SDK

The purpose of this project is compare two approaches to implementing a fictitious document publishing use case. One approach implements the use case as a State Machine written as Maven project programmed in Java.
The other approach implements the workflow using the [Temporal Java SDK](https://github.com/temporalio/sdk-java). The basic logic implemented by both versions is illustrated in the diagram below in Figure 1.

|![generic-workflow-01](https://github.com/temporalio/temporal-development-patterns-whitepapers/assets/1110569/462e7e65-dfcb-4527-b9cf-4b689339bf06)|
|----|
|**Figure 1:** A document intended for publication is submitted for processing. The document goes through two phases, Graphic Edit and Copy Edit, simultaneously. Then, when both phases complete the document is passed on for publication.|

The source code for the State Machine project is [here](./statemachine).

The source code for implementing the workflow in Temporal is [here](./temporal).

There are two key benefits that Temporal provides when compared to a State Machine. The first is that when using Temporal, developers don’t have to spend a lot of time programming the infrastructure for managing the workflow’s initialization and retry behavior; the plumbing to run a workflow is provided by the Temporal framework “out of the box.”

On the other hand, in terms of a State Machine, a developer needs to implement the infrastructure that supports the State machine in addition to programming the controller, events, transitions and commands that make up the State Machine. If the State Machine is message-driven, then the developer needs to implement the message queue(s) and the messages that the queue will support too. This can be significant work, particularly for distributed applications that operate at web scale. With Temporal, the work is easier. The infrastructure to support a workflow is built in.

The second benefit is that Temporal is durable by design.

Workflow failure is commonplace, even with the simplest of business processes. Some failures, such as timeout, can be remedied by a retry. Other failures are so disastrous that the workflow needs to be abandoned and the system needs to be reverted to its last known good state. Addressing failure in a State Machine is difficult; in fact, it can be just as much, if not more work as creating the State Machine’s happy path.

On the other hand, Temporal anticipates the need for retries and reversions. With Temporal, implementing retries is a matter of a configuration setting that Temporal will execute automatically. Implementing system reversions, known in Temporal parlance as compensations is a straightforward undertaking. Compensation code that is programmed in one place: within the workflow file.

In a State Machine, even under the best case when a State Machine is well structured, reversion behavior would need to be programmed into each state. Implementing reversion behavior in a loosely structured State Machine can be a foray into working with spaghetti code that is hard to program, not to mention test.

In short, under Temporal, durability is built in. Under a State Machine durability must be programmed in.
