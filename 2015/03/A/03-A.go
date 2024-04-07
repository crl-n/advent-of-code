package main

import (
	"aoc15/utils"
	"fmt"
)

func main() {
	input := utils.GetInput()

	houses := map[string]bool{}

	x := 0
	y := 0
	
	for i := 0; i < len(input); i++ {
		c := input[i]

		if c == '^' {
			y++
		} else if c == '>' {
			x++
		} else if c == '<' {
			x--
		} else if c == 'v' {
			y--
		} else {
			continue
		}

		key := fmt.Sprintf("%dx%d", x, y)
		houses[key] = true
	}

	fmt.Println(len(houses))
}
