#!/usr/bin/env python3

import sys

with open(sys.argv[1]) as file:
    lines = [line.strip('\n') for line in file.readlines()]

cycle = 1
reg = 1
wait = False
value = 0
sum = 0
canvas = ""

i = 0
while i < len(lines):

    if cycle % 40 == 0 and reg in [38, 39, 40]:
        canvas += '#'
    elif cycle % 40 in [reg + 1, reg + 2, reg]:
        canvas += '#'
    else:
        canvas += '.'
    if cycle % 40 == 0:
        canvas += '\n'

    if wait == True:
        wait = False
        reg += value
    elif lines[i].startswith("addx"):
        wait = True
        value = int(lines[i].split(' ')[1])
        i += 1
    elif lines[i].startswith("noop"):
        i += 1
    cycle += 1

print(canvas)
