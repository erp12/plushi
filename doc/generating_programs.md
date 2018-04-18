# Generating Programs

The Plushi HTTP server allows for users to get a list of all the instructions
supported by the interpreter.

Assuming we have a Plushi server running on port `8075`, we can issue a POST
request where the body is JSON with the `action` and `arity` keys specified.
Below is an example of such a request being made via the Python requests library.

```py
import json, requests
instr_set = requests.post("http://localhost:8075/", json=json.dumps({
  'action': 'instructions',
  'arity': arity
})).json()
```

The result of the instructions request above will be a list of JSON objects.
Each object describes the signature of a support Push instruction with the
following keys `name`, `input-type` `output-types` and optionall `docstring`.

The following is an example of one object that could appear in the list of
objects returned by the instructions command.

```json
{
  "name": "plushi:integer_add",
  "input-types": ["integer", "integer"],
  "output-types": ["integer"],
  "docstring": "Adds the top two integers and pushes the result."
}
```

The `name` field is an identifier for the instruction which can appear in
Plushi programs during execution. The `input-types` field denotes what data types,
and how many of them, the instruction requires for input. The `output-types`
field denotes how many output values the instruction produces and each of their
data types. The `docstring` values describes the behavior of the instruction for
reference, and is not intended to be used by AI/ML systems. Docstrings will
only be included in the response to the intructions request if JSON body of the
requests includes `docstrings: true`.

### Filtering the Instruction Set

Note that the above example specifies that the `integer_add` instruction
requires two integers as input and produces one integer as output. This
information can be used to filter the instruction set down to only the
instructions that deal with data types that are relevant to your problem.

For example, if you are attempting to produce a program that acts as a regression
model with real-valued numeric inputs, you will not require the use of instructions
that manipulate strings such as `string_split`. We could filter the instruction
set do just the instruction which manipulate floats using the following Python
code.

```py
float_instruction_names = []
for i in instructions:
  if ("float" in i["input-types"]) or ("float" in i["output-types"]):
    float_instruction_names.append(i["name"])
```

### Specifing Arity

We commonly want to produce programs that accept some input values that impact
program behavior. The programs we generate using Plushi must include
instructions which reference these input values. These instructions are called
input instructions. To ensure the input instructions are included in the
instruction set, we specify the `--arity` of the programs we will be generating
when calling pushi with `--instruction-set`. This value is generally the same
as the number of features in the dataset which is being used to produce
the program.


### Using Instruction Set to Create Programs

Once we have a list of instructions we would like to appear in our programs,
it is trivial to generate random programs. The structure of a Plushi program is
designed so that any linear sequence of instruction names and literal values
(integers, floats, booleans, or strings) is a valid, executable program.

```py
import random
program_size = 50
literals = [-1.0, 1.0, 2.0, 10.0]
all_atoms = literals + float_instruction_names
program = [random.choice(all_atoms) for _ in range(program_size)]
```

The listed produced by the code above stored in the `program` variable will be
a valid, executable program that can be run by the Plushi interpreter.
For more information, see the [running programs]() documentation
topic.

### Searching Over Programs

Given that any valid instruction name or literal can apear at any point in
the program and the program will remain valid and executable, it is trivial to
swap the parts of all of a program with new instructions and literals.
