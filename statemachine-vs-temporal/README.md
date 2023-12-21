# Comparing a document publishing use case using a Statement Machine vs a Temporal workflow.

The purose of this project is compare two approaches to implementing a document publishing use case. One approach implements the use case as a State Machine written as Maven project programmed in Java.
The other approach uses a Tremporal Workflow programmed using the [Temporal Java SDK API](https://www.javadoc.io/doc/io.temporal/temporal-sdk/latest/index.html).

The differences in approach are illustrated in the diagram below:

![project-comparisons-01](https://github.com/reselbob/publishing-statemachine/assets/1110569/f6ed68aa-2fa3-489d-982c-1f29a19c67f6)

The State Machine code is [here](./statemachine).
The Temporal Workflow code is [here](./temporal).

