package main

import (
	"fmt"
	"bufio"
	"os"
)

func allCharsUnique(slice string) bool {
	for i, _ := range slice {
		for j := i + 1; j < len(slice); j++ {
			if slice[i] == slice[j] {
				return false
			}
		}
	}
	return true
}

func findMessage(line string) int {
	for index := 0; index < len(line); index++ {
		if allCharsUnique(line[index:index + 14]) {
			return index
		}
	}
	return -1
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	line := scanner.Text()

	messageMarker := findMessage(line)
	fmt.Println(messageMarker + 14)
}
