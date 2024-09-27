package main

import (
	"bufio"
	"fmt"
	"os"
)

func count(line string, lookup *[53]uint8) {
	var index int
	for _, c := range line {
		if c >= 'a' && c <= 'z' {
			index = int(c) - 'a' + 1
		}
		if c >= 'A' && c <= 'Z' {
			index = int(c) - 'A' + 27
		}
		lookup[index]++
	}
}

func find_shared(first string, second string, third string) int {
	var lookup_first [53]uint8
	var lookup_second [53]uint8
	var lookup_third [53]uint8

	count(first, &lookup_first)
	count(second, &lookup_second)
	count(third, &lookup_third)

	for i, n := range lookup_first {
		if n != 0 && lookup_second[i] != 0 && lookup_third[i] != 0{
			return i
		}
	}
	return 0
}

func main() {
	var shared int
	var sum int
	scanner := bufio.NewScanner(os.Stdin)

	for scanner.Scan() {
		first := scanner.Text()
		scanner.Scan()
		second := scanner.Text()
		scanner.Scan()
		third := scanner.Text()
		shared = find_shared(first, second, third)
		sum += shared
	}
	fmt.Println("Sum: ", sum)
}
