#!/usr/bin/env python3

import sys

with open(sys.argv[1]) as file:
    lines = [line.strip('\n') for line in file.readlines()]

cycle = 1
reg = 1
wait = False
value = 0
sum = 0

i = 0
while i < len(lines):

    if cycle in [20, 60, 100, 140, 180, 220]:
        print(f"Signal strength {cycle * reg} on cycle {cycle}")
        sum += cycle * reg

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

print(f"Sum of singal strengths {sum}")
