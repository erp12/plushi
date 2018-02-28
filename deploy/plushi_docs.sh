
# Should be run from the plushi root directory

lein codox
cp -r target/base+system+user+dev/doc target/base+system+user+dev/docs
rm -rf docs
mv target/base+system+user+dev/docs docs
lein run --docs docs/plushi_instruction_set.html
