# Introduction to pushi

Pushi is a standalone, language agnostic interpreter for the Push language
capable of running push programs via a JSON/EDN interface.

Push is a programming language designed for AI systems to write software in.
It has virtually no syntax and supports all common data types and can express
a variety of control structures and data structures.

To read more about the push language, see the [Push Redux](https://erp12.github.io/push-redux/).


## Push Programs in Pushi

In pushi, a Push program is made up of three fields.

1. A serialized blog of push code in either JSON or EDN. This is described in
   detail in the next section.
2. The arity (number of inputs) to the push program.
3. A list of data types the program should return.

It is reasonable to think of a push program in pushi as a single function where
the blob of push code is the body and the arity and return types make up the
signature. This information give pushi everything it needs to run the program
given any valid set of inputs.


## Generating Serialized Push Code

The main use case which pushi is designed to support is to be integrated with
various artificial intelligence, machine learning, or search frameworks for the
purpose of automatic programming. In order to acheive this, pushi must supply
these frameworks with everything needed to generate valid Push programs which
pushi is capable of executing.

As described in the [Push-Redux](https://erp12.github.io/push-redux/), Push
programs are nested lists of "literals" and "instructions."

A literal is roughly the Push equivalent of a primitive data type in most other
languages. Literal values include integers, floats, strings, and booleans.

An instruction is a built in function or operation supported by the Push
interpreter. Pushi has a set of supported instructions which is documented
[in its own topic]() however one can also retreive these instructions, including
their signatures and docstrings, in code via the Pushi standalone.

> Pushi does not currently support any form of runtime user defined instructions
> (UDI), although it is trivial to add your own instructions in the Pushi source
> code and build your own version of the standalone. If you feel your instructions
> are likely to be of any interest to other users, please consider
> [contributing them]() to the pushi project on GitHub.

Below is a snippet of python 3 code that shows how one might retreive the set of
supported instructions in their own project.

```py
import subprocess
import json
instructions = json.loads(subprocess.run(["java", "-jar" "pushi-0.1.0-standalone.jar",
                                          "--instruction-set",
                                          "--arity", 2,
                                          "--format" "json"],
                                         stdout=subprocess.PIPE).stdout)
```

The `-I` flag denotes that the standalone should return the set of supported instructions. The result will be a list of dictionaries. Each dictionary will define a single
instruction. For example, below is the dictoinary for the `integer_add` instruction:

```py
{
  'name': 'pushi:integer_add',
  'input-types': ['integer', 'integer'],
  'output-types': ['integer'],
  'docstring': 'Adds the top two integer stack values, and pushes the result.'
}
```

The `name` field is an identifier for the instruction. The `input-types` field
denotes what data types, and how many of them, the instruction requires for input.
In the `integer-add` example, we can see that the instruction requires 2 integer
inputs. The `output-types` field denotes how many output values
the instruction produces and each of their data types. The `docstring` values
describes the behavior of the instruction for reference, and is not intended to
be used by AI/ML systems.


### Instructions For Your Problem

It is rare that all instructions will be required for a given problem. For
example, if you are attempting to produce a program that acts as a regression
model with real-valued numeric inputs, you will not require the use of instructions
that manipulate strings such as `string_split`. Given the instruction specs
in the instruction set returned by pushi, it is trival to filter out
instructions which are not needed. Below is a python example.

```py
float_instructions = []
for i in instructions:
  if ("float" in i["input-types"]) or ("float" in i["output-types"]):
    float_instructions.append(i)
```

We commonly want to produce programs that accept some input values that impact
program behavior. The Push programs we generate using pushi must include
instructions which reference these input values. These instructions are called
input instructions. To ensure the input instructions are included in the
instruction set, we specify the `--arity` of the programs we will be generating
when calling pushi with `--instruction-set`.


### Using Instruction Set to Create Programs

```py
program_dict = {
    "code": [1.5, 2.8, float_instructions[3]["name"]],
    "arity": 2,
    "output-types": ["float"]
}
prog_result_json = subprocess.run(["lein", "run",
                                   "--run", json.dumps(program_dict),
                                   "--format", "json",
                                   "--inputs", "[0.123, 4.56]"],
                                  stdout=subprocess.PIPE).stdout
prog_result = json.loads(prog_result_json)
```
