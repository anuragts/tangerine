#!/bin/bash
# Navigate to the src directory
cd src

# Compile all the .java files in the kv and utils packages
javac kv/*.java utils/*.java

# Navigate back to the original directory
cd ..
