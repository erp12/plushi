# Instructions

## Built-in Instructions

Plushi aims to provide an expressive, general purpose set of instructions that
mirror the built-in capabilities of most modern modern languages. The built
in instruction set covers the typical opperations that can be performed on the
following data types.

- Integers
- Float
- Booleans
- Strings
- Characters
- Vectors (Coming soon)

Plushi also uses instructions to implement control structures such as if-else
and loops. These instructions are implemented by manipulating the exectution
(`exec`) stack which holds the instructions and literals which have yet to be
executed.

A webpage detailing all supported instructions can be found
[here](plushi_instruction_set.html).

## Defining New Instructions

It is not uncommon to require specialized instrutions for particular applications
of the Push language.

Plushi does not currently support any form of runtime user defined instructions
(UDI), although it is trivial to add your own instructions in the Plushi source
code and build your own version of the interpreter. If you feel your instructions
are likely to be of any interest to other users, please consider
[contributing them]() to the plushi project on GitHub.

The define your own instruction, simply call `plushi.instruction/register`
with the necessary components. The componensts are listsed and defined below.


- **Name:** A unique string with respect to the other instructions in the instruction set.
- **Function:** Clojure function which implements the behavior of your instruction.
- **Input-types:** A vector of stack types to be used when retreiving the arguments
to the function.
- **Output-types:** A vector of stack types to be used when routing the output of
the function back onto the stacks.
- **Code-blocks:** An integer denoting the number of code blocks that follow the
instruction. This is commonly used when implementing control structures. More
information about this is given in a later section.
- **Docstring (optional):** A string explaining what the instruction does. Should be
included for instructions that are going to be contributed to the official Plushi
codebase.

Below are some examples of registering new instrutions.

```clojure
(require '[plushi.instruction :as i])

(i/register "float_sin"
            #(Math/sin %)
            [:float] [:float] 0
            "Pushes the sin of the top float.")

(i/register "string_head"
            #(subs %1 0 %2)
            [:string :integer] [:string] 0
            "Pushes a string of the first N characters from the top string. N is the top integer.")

(i/register "exec_if"
            (fn [b then else]
              (if b then else))
            [:boolean :exec :exec] [:exec] 2
            "If the top boolean is true, execute the top element of the exec
            stack and skip the second. Otherwise, skip the top element of the
            exec stack and execute the second.")
```

After creating a namespace with your instructions, add the namespace to the list
of namespaces `use`-ed at the bottom of `instruction.clj` and rebuild the
Plushi standalone jar with `lein uberjar`.


## Code Blocks

As mentioned above, some instructions are followed by some number of "code-blocks".
The code blocks make up the body of loops, if-else, and other control structures.
To see how this works, lets consider the following Plushi program.

```json
["plushi:input_0", "plushi:exec_if", 5, "plushi:close", 3, 1, "plushi:close"]
```

The `exec_if` instruction implements the standard `if-else` constrol structure
where there is a body of code to be run under the `if` clause and a seperate
body of the code to be run under the `else` clause. In the case of `exec_if`, a
different body of code should be run depending on if the top boolean is true or
false.

The definition of `exec_if` specifies that the instruction opens 2 "code-blocks",
one for the body of the `if` and one for the body of the `else`. However, since
Plushi programs are always linear, it is still unlear which instructions in the
program are part of each of these two code blocks.

To denote when a code block is closed, a dummy "close" instruction is used.
These instructions can be seen in the example program above. Any instruction or
literal that appears between an instruction which opens a code block and a
close instruction is considered to be in the code block.

If a close instruction appears in a program when no code blocks are open, it is
ignored. If an instruction opens a code block and there are no close instructions
to denote the end of the code block, the remainder of the program is considered
to be in code block.
