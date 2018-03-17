# plushi

[![CircleCI](https://circleci.com/gh/erp12/plushi.svg?style=svg)](https://circleci.com/gh/erp12/plushi)


Plushi is an embadable language agnostic push interpreter capable of running
push programs via JSON interface.

Push is a programming language designed for AI systems to write software in.
It has virtually no syntax and supports all common data types and can express
a variety of control structures and data structures.

To read more about the push language, see the [Push Redux](https://erp12.github.io/push-redux/).


## Installation

Download from https://github.com/erp12/plushi/releases.

For easy integration with Clojure projects, a public clojars release coming soon!


## Usage

The Plushi interpreter is wrapped in an HTTP server written using [ring](https://github.com/ring-clojure/ring).
Any environment capable of making POST requests can use Plushi. JVM languages
can also attempt to use plushi via inerop, although this is not the primary
design goal.

### The Standalone jar

See the releases page for downloads of the standalone jar file.

To build your own standalone jar, clone the repository and run the following
[leiningen](https://leiningen.org/) commands.

```
lein deps
lein uberjar
```

### Starting the Plushi Server

To start the plushi HTTP server, simply run the jar with the `--start` flag.

```sh
java -jar plushi-0.1.0-standalone.jar --start
# or
$ java -jar plushi-0.1.0-standalone.jar -s
```

The default port is `8075`. To specify the port number, supply the `--port` flag
followed by a valid port number.

```sh
java -jar plushi-0.1.0-standalone.jar --start --port 8076
# or
$ java -jar plushi-0.1.0-standalone.jar -s -p 8076
```

## Request Actions

All of the actions required to synthesis plushi programs are available via POST
requests to the plushi HTTP server. Other applications can send JSON blobs that
describe what action the plushi server should take.

### Getting the Supported Instructions

The Plushi interpreter supports a set of instructions which can appear in
programs along side literals (ints, floats, strings, booleans). These instructions
and literals in a linear sequence is a "plush program" which can be executed by
the plushi interpreter.

To request the Plushi instruction set, the JSON blob in the request body must
have 2 keys: `"action"` and `"arity"`. The value of the `"action"` key must
be `"instructions"` and the value of the `"arity"` key should be an integer
denoting how many inputs the programs you would like to generate will accept.

Below is an example of this in python.

```py
import json, requests
instr_set = requests.post("http://localhost:8075/", json=json.dumps({
  'action': 'instructions',
  'arity': arity
})).json()
```

### Getting the Supported Types

Depending on what kinds of programs you intend on synthesizing, it maybe be
helful to know what kinds of data types the Plushi interpreter is capable of
manipulating. The `"types"` action returns this information on requst.

Below is an example of this in python.

```py
import json, requests
plushi_types = requests.post("http://localhost:8075/",
                             json=json.dumps({'action': 'types'})).json()
```

### Running a Plushi Program

If the value associated with the `"action"` key is `"run"` then Plushi will
expect to be running a given plush program.

Below is an example of request made through python. All of the required and
optional values are described in detail in the paragraphs below.

```py
import json, requests
X = json.load("data.json")
request_body = {
  'action': 'run',
  'code': [1, 2, "plushi:integer_add"],
  'arity': 1,
  'output-types': ['integer'],
  'dataset': X
}
outputs = requests.post("http://localhost:8075/",
                        json=json.dumps(request_body)).json()
```

#### `"code"`

The value for the `"code"` key should be a flat list containing valid plushi atoms.
A valid plushi atom can be any of the following:

- an integer
- a float
- a boolean
- a string

If a string atom is equal to a name of any instruction (ie `"plushi:float_mult"`)
Plushi will assume that the atom at that position should be the instruction.
Otherwise the string (or any other atom) will be assumed to be a literal.

Unlike other implemenations of the the Push language, Plushi programs are always
linear. Certain instructions (ie. `exec_if`) assume that the subsequent atom is
a nested Push expression (or "code block"). Plushi has a dedicated instruction
called `close` which denotes the closing of a code block. Using these instructions
a linear Plushi program can be translated into a traditional, executable Push
program. If no code blocks are open, the `close` instructions are a noop. 

#### `"arity"`

An integer denoting how many inputs the program specified by `"code"` will expect. This is also the number of features in the dataset.

#### `"output-types"`

A list of plushi types to return after program execution. If more than one value
of a certain type should be output, that type name should appear in the list
multiple times.

For example a program which returns two integer and a string should set the
`"output-types"` value to `["integer", "integer", "string"]`.

### `"dataset"`

Plushi is designed to be used inside of inductive machine learning frameworks.
Thus, it is expected that each program will need to be run on an entire dataset
of inputs for evaluation.

The value of the `"dataset"` key is expected to be a list of JSON objects.
Each JSON object is one record in the dataset with the keys being feature names
and values being feature values.

```JSON
[
  {"name" : "Alice", "age": 31},
  {"name" : "Bob", "age": 45},
  {"name" : "Cathy", "age": 24},
]

```

> The dataset should not contain the training label you are trying to predict.
> Currently Plushi only requires you specify the arity of the program, not the
> names of which feature to use, and thus Plushi may  use your label as an input
> by mistake.

## Examples

Below is a list of other projects which use Plushi:

- [plushi-annealing](https://github.com/erp12/plushi-annealing) - A simple simulated annealing algorithm for synthesizing Plushi programs.


## Contributing

All contributions and ideas are strongly encouraged!

To see the list of proposed new features and changes, check the issues on the
project's issues. Feel free to open new issues with any ideas or suggestions.

If you would like to contribute to either the source code or the documentation,
please follow the "GitHub Flow" which is simply summarized
[here](https://guides.github.com/introduction/flow/).

TLDR:
1. Fork the official repository.
2. Create a branch for your contribution.
3. Make commits (Including tests).
4. Open a pull request and await review and merge.


## License

Copyright © 2018 Edward Pantridge

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
