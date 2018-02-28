# Examples of Plushi Usage

## From Bash

For this example, we will assume that our dataset and push program have been serialized in JSON format and are stored in a file on disk.

Say the `program.json` file has the following contents:

```JSON
{
  "code": ["plushi:input_1", "plushi:integer_sub", 2, "plushi:integer_add", "plushi:input_0", "plushi:integer_mult"],
  "arity": 2,
  "output-types": ["integer"]
}
```

Say the `data.json` file has the following contents:

```JSON
[
  {"in1": 0, "in2": 0},
  {"in1": 0, "in2": 1},
  {"in1": 1, "in2": 0},
  {"in1": 1, "in2": 1},
  {"in1": 1, "in2": 2},
  {"in1": 2, "in2": 1},
  {"in1": 2, "in2": 2}
]
```

The goal of this example is to show how we get the output of the program in `program.json` on each data case in `data.json`.

### 1. Preprocess Data

When we read the JSON files we must remove newlines or there will be parsing errors. Here this is done with `tr`.

```sh
prog=`cat simple_program.json | tr -d '[:space:]'`
data=`cat data.json | tr -d '[:space:]'`
```

### 2. Run plushi

We specify the run (`-R`), dataset (`-D`), and format (`-f`) flags in our call to plushi.

```sh
java -jar plushi.jar -R $prog -D $data -f json
```

A JSON blob should be printed to stdout containing an array of outputs for each data case. In this case each array will have a length of one because the program only outputs one value.

The output should look like the following:

```JSON
[[0],[0],[2],[3],[4],[6],[8]]
```

### Overview

```sh
prog=`cat simple_program.json | tr -d '[:space:]'`
data=`cat data.json | tr -d '[:space:]'`

java -jar plushi.jar -R $prog -D $data -f json
```


## From Python

Write me!


## From Clojure

Write me!
