import subprocess
import json

# Get all supported instructions
instructions = json.loads(subprocess.run(["lein", "run", "-I", "-f" "json"],
                                         stdout=subprocess.PIPE).stdout)

# Filter to instructions of correct type
float_instructions = [i for i in instructions if ("float" in i["input-types"]) or ("float" in i["output-types"])]

# Get list of instruction names to use in prograom JSON
instruction_names = ([i['name'] for i in float_instructions])

print(instruction_names)
print()


# Create program from literal values and instruction names.
prog_dict = {
    "code": [1.5, 2.8, instruction_names[3]],
    "arity": 0,
    "output-types": ["float"]
}
prog_json = json.dumps(prog_dict)
print(prog_json)
print()

# Run program
prog_result_json = subprocess.run(["lein", "run", "-R", prog_json,
                                   "-f", "json",
                                   "-i", "[]"],
                                  stdout=subprocess.PIPE).stdout
prog_result = json.loads(prog_result_json)
print(prog_result)
print(prog_result[0])
print(type(prog_result[0]))
