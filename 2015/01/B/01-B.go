package main

import (
	"fmt"
	"aoc15/utils"
)

func main() {
	input := utils.GetInput()

	result := 0

	i := 0
	for i < len(input) {

		c := input[i]

		if c == '(' {
			result++
		}

		if c == ')' {
			result--
		}

		if  result == -1 {
			break
		}

		i++
	}

	fmt.Println(i + 1)
}
