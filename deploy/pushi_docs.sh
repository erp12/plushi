
# Should be run from the pushi root directory

lein codox
cp -r target/base+system+user+dev/doc target/base+system+user+dev/docs
rm -rf docs
mv target/base+system+user+dev/docs docs
lein run --docs docs/pushi_instruction_set.html
