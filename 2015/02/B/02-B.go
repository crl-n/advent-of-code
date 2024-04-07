package main

import (
	"aoc15/utils"
	"fmt"
	"sort"
	"strings"
)

func main() {
	input := utils.GetInput()

	getDimensions := func(line string) []int {
		vals := strings.Split(line, "x")
		return utils.StringsToInts(vals)
	}

	calcBow := func(dimensions []int) int {
		l := dimensions[0]
		w := dimensions[1]
		h := dimensions[2]
		return l*h*w
	}

	calcWrap := func(dimensions []int) int {
		sort.Ints(dimensions)
		return dimensions[0] + dimensions[0] + dimensions[1] + dimensions[1]
	}
	
	lines := strings.Split(strings.Trim(input, "\n"), "\n")

	total := 0

	for _, line := range lines {
		dimensions := getDimensions(line)
		total += calcWrap(dimensions) + calcBow(dimensions)
	}

	fmt.Println(total)
}
