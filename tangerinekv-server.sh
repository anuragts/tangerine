#!/bin/bash

# Navigate to the src directory
cd src

# Compile all the .java files in the kv and utils packages
javac kv/*.java utils/*.java

# Run the KVServer program
java kv.KVServer

# Navigate back to the original directory
cd ..
