package main

import (
	"aoc15/utils"
	"fmt"
)

type coord struct {
	x int
	y int
}

func main() {
	input := utils.GetInput()

	houses := map[string]bool{}

	santa := coord{x: 0, y: 0}
	robo := coord{x: 0, y: 0}
	current := &santa
	
	for i := 0; i < len(input); i++ {
		c := input[i]

		if c == '^' {
			current.y++
		} else if c == '>' {
			current.x++
		} else if c == '<' {
			current.x--
		} else if c == 'v' {
			current.y--
		} else {
			continue
		}

		key := fmt.Sprintf("%dx%d", current.x, current.y)
		houses[key] = true
	
		if (current == &santa) {
			current = &robo
		} else {
			current = &santa
		}
	}

	fmt.Println(len(houses))
}
