# Getting Started


Plushi is a standalone, language agnostic interpreter for the Push language
capable of running Push against a datset.

Push is a programming language designed for AI systems to write software in.
It has virtually no syntax and supports all common data types and can express
a variety of control structures and data structures. To read more about the
push language, see the [Push Redux](https://erp12.github.io/push-redux/).

In Plushi, a program is a linear sequence of instruction names and literals. An
instruction is a built in function or operation supported by the Push
interpreter. A literal is roughly the Push equivalent of a primitive data type
in most other languages. Literal values include integers, floats, strings,
and booleans.

The two main functions of Plushi are 1) providing a machine learning system
with the instruction names that can appear in programs so that random programs
can be generated and 2) executing a Push program across all records in a dataset.

## Installation

Download the standalone jar from https://github.com/erp12/plushi/releases.

To build your own standalone jar, clone the repository and run the following
[leiningen](https://leiningen.org/) commands.

```
lein deps
lein uberjar
```

## The Plushi Server

In order to achieve an interface that can be used from as many environments as
possible Plushi is implemented as an HTTP server. The Plushi server can be run
locally, or on dedicated hardware. The user, or users, interact with the Plushi
server through POST requests. The body of each POST request is assumed to be
JSON which the Plushi server parses, and interprets in various ways described in
later sections. The response Plushi gives to each POST request is also
serialized in JSON format.


### Starting the Plushi Server

To start the plushi HTTP server, simply run the jar with the `--start` flag.

```sh
java -jar plushi-0.1.0-standalone.jar --start
# or
java -jar plushi-0.1.0-standalone.jar -s
```

The default port is `8075`. To specify the port number, supply the `--port` flag
followed by a valid port number.

```sh
java -jar plushi-0.1.0-standalone.jar --start --port 8076
# or
java -jar plushi-0.1.0-standalone.jar -s -p 8076
```

### Plushi Usage

Once the Plushi server is started, it will be available to respond to requests.
Thre are two main kinds of requests that are used when using Plushi for
program synthesis.

1. The `instructions` requeset is used to return the set of supported
instructions that can appear in Plushi programs. For more information, see
the [generating programs]() documentation topic.

2. The `run` request is used to run a given Plushi program on each record in a
dataset. For more information, see the [running programs]() documentation
topic.
