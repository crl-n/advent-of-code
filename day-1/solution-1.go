package main

import (
	"fmt"
	"bufio"
	"log"
	"os"
	"strconv"
)

func main() {
	file, err := os.Open("input")
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	max := 0
	sum := 0
	calories := 0
	line := ""
	for scanner.Scan() {
		line = scanner.Text()
		if line == "" {
			sum = 0
			continue
		}
		calories, err = strconv.Atoi(line)
		sum += calories
		if sum > max {
			max = sum
		}
	}

	fmt.Println("Max: ", max)
	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}
}
