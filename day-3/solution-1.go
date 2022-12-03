package main

import (
	"bufio"
	"fmt"
	"os"
)

func count(half string, lookup *[53]uint8) {
	var index int
	for _, c := range half {
		if c >= 'a' && c <= 'z' {
			index = int(c) - 'a' + 1
		}
		if c >= 'A' && c <= 'Z' {
			index = int(c) - 'A' + 27
		}
		lookup[index]++
	}
}

func main() {
	var first string
	var second string
	var sum int
	scanner := bufio.NewScanner(os.Stdin)

	for scanner.Scan() {
		line := scanner.Text()

		first = line[:len(line) / 2]
		second = line[len(line) / 2:]

		var lookup_first [53]uint8
		var lookup_second [53]uint8
		count(first, &lookup_first)
		count(second, &lookup_second)
		for i, n := range lookup_first {
			if n != 0 && lookup_second[i] != 0 {
				sum += i
			}
		}
	}
	fmt.Println(sum)
}
