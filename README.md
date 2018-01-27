# pushi

Pushi is a language agnostic push interpreter capable of running push programs
via JSON/EDN interface.

Push is a programming language designed for AI systems to write software in.
It has virtually no syntax and supports all common data types and can express
a variety of control structures and data structures.

To read more about the push language, see the [Push Redux](https://erp12.github.io/push-redux/).


## Installation

Download from http://example.com/FIXME.

For easy integration with Clojure projects, a public clojars release coming soon!


## Usage

### Standalone

FIXME: explanation

    $ java -jar pushi-0.1.0-standalone.jar [args]

### Clojure

Pushi can also be used as a Clojure library for easy integration with Clojure projects. For more information see with documentation on this exact topic.

### Python

A python interface using Py4j might be in this project's future, but it is not
currently being worked on. In the meantime, you can run pushi from Python
using the standalone jar and system calls.

If you would like to help integrate pushi and python (or any other language)
consider contributing!


## Options

### --format, -f

Denotes the file format to use for communication. Supported formats are given in
the below table

| format | link                              |
| ------ | --------------------------------- |
| json   | https://www.json.org/             |
| edn    | https://github.com/edn-format/edn |


### --instruction-set, -I

Returns a serialized object desciribing the instructions which are supported by the interpreter. The serialized list will be encoded using the format specified by `--format`.

The returned serialized object includes instruction names, the types of values they accept as input, and the types of values they produce as output. Users can create program by assembling serialzied programs out of the instructions which are relavent to their system.

Requires the `--arity` argument to be specified.

### --supported-types, -T

Returns a serialized list of push types which are supported by the interpreter. The serialized list will be encoded using the format specified by `--format`.

### --run, -R

Must be followed by a serialized program. Returns the result of running the program. Result will be returned in the format specified by `--format`.

Requires the `--inputs` argument also be specified.

### --arity, -a

Must be followed by a non-negative integer denoting the number of input values that is required to run the program.

### --inputs, -i

Must be followed by a serialized list of values to use as inputs to the push program.


## Examples

More in depth examples can be found on the documenation page.


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
