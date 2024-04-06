package main

import (
	"fmt"
	"aoc15/utils"
)

func main() {
	input := utils.GetInput()

	result := 0

	for i := 0; i < len(input); i++ {

		c := input[i]

		if c == '(' {
			result++
		}

		if c == ')' {
			result--
		}
	}

	fmt.Println(result)
}
