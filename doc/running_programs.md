# Running Programs

Once we have [generated a program](), we can make a `run` request to the Plushi
server to map the execution of the program down a given dataset.

Below is an example of request made through python. All of the required and
optional values are described in detail in the sections below.

```py
import json, requests
X = [[0], [1], [2], [3], [4], [5]]
request_body = {
  'action': 'run',
  'code': ["plushi:input_0", 2, "plushi:integer_add"],
  'arity': 1,
  'output-types': ['integer'],
  'dataset': X
}
outputs = requests.post("http://localhost:8075/",
                        json=json.dumps(request_body)).json()
print(oututs)
# [[0], [2], [4], [6], [8], [10]]
```

The above example runs a program equivalent to `f(x) = 2x` on a univariate
dataset `X`. The result of the `run` request is a json list containing the
output of running the program on each record in `X`.


## The `code`

The value for the `"code"` key should be a flat list containing valid plushi atoms.
A valid plushi atom can be any of the following:

- an integer
- a float
- a boolean
- a string

If a string atom is equal to a name of any instruction (ie `"plushi:float_mult"`)
Plushi will assume that the atom at that position should be the instruction.
Otherwise the string (or any other atom) will be assumed to be a literal.

For more information about generating programes, see [the seperate documentation
topic]().

## The `arity`

An integer denoting how many inputs the program specified by `"code"` will expect.
This is also the number of features in the dataset.


## The `output-types`

A list of plushi types to return after program execution. If more than one value
of a certain type should be output, that type name should appear in the list
multiple times.

For example a program which returns two integer and a string should set the
`"output-types"` value to `["integer", "integer", "string"]`.

## The `dataset`

Plushi is designed to be used inside of inductive machine learning frameworks.
Thus, it is expected that each program will need to be run on an entire dataset
of inputs for evaluation.

There are two formats for the serialized data that are supported by Plushi.

### Format 1

The value of the `"dataset"` key can be a list of JSON objects.
Each JSON object is one record in the dataset with the keys being feature names
and values being feature values.

```json
[
  {"name" : "Alice", "age": 31},
  {"name" : "Bob", "age": 45},
  {"name" : "Cathy", "age": 24}
]
```

### Format 2

The value of the `"dataset"` key can be a list of lists. Each list is one
record in the dataset where the position of each value determines which
feature it is refering to. Feature names are not included in this format. It is
expected that all records are the same length.

```json
[
  ["Alice", 31],
  ["Bob", 45],
  ["Cathy", 24]
]
```

> The dataset should not contain the training label you are trying to predict.
> Currently Plushi only requires you specify the arity of the program, not the
> names of which feature to use, and thus Plushi may  use your label as an input
> by mistake.
