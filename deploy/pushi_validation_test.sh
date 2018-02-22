
# Should be run from the pushi root directory

prog=`cat resources/simple_program.json | tr -d '[:space:]'`
data=`cat resources/data.json | tr -d '[:space:]'`

echo $prog
echo $data

lein run -R $prog -D $data -f json
