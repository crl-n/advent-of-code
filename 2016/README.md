# Advent of Code 2016
Solved with Kotlin 2.0.20, each solution is its own Kotlin script.

## Bootstrap Script
`bootstrap.kts` creates a directory and `.kts` file for solving the exercise.

It also gets the input from www.adventofcode.com and writes it to a file if not already present.
For this to work AOCSESSION env variable has to be set, session token can be retrieved manually
by inspecting authenticated requests made to the Advent of Code website in a browser. 

To run `bootstrap.kts`:
```shell
kotlinc -script ./bootstrap.kts 1 a 
```

## Run
```shell
kotlinc -script ./01/01-A.kts 
```
