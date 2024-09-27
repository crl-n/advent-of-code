package main

import (
	"fmt"
	"bufio"
	"os"
	"strings"
	"strconv"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	var stackLines []string
	var moveLines []string
	var stacks [9][]string

	for scanner.Scan() {
		stackLines = append(stackLines, scanner.Text())
		if stackLines[len(stackLines) - 1] == "" {
			break
		}
	}

	for scanner.Scan() {
		moveLines = append(moveLines, scanner.Text())
	}

	for i := len(stackLines) - 3; i >= 0; i-- {
		line := stackLines[i]
		stackId := 0
		for j := 1; j < len(line) - 1; j += 4 {
			c := line[j]
			if c >= 'A' && c <= 'Z' {
				stacks[stackId] = append(stacks[stackId], string(c))
			}
			stackId++
		}
	}

	for _, line := range moveLines {
		tokens := strings.Split(line, " ")
		amount, _ := strconv.Atoi(tokens[1])
		srcId, _:= strconv.Atoi(tokens[3])
		dstId, _ := strconv.Atoi(tokens[5])
		srcId--
		dstId--
		for i := 0; i < amount; i++ {
			stacks[dstId] = append(stacks[dstId], stacks[srcId][len(stacks[srcId]) -1])
			stacks[srcId] = stacks[srcId][:len(stacks[srcId]) - 1]
		}
	}

	for _, stack := range stacks {
		fmt.Printf("%s", stack[len(stack) - 1])
	}
}
