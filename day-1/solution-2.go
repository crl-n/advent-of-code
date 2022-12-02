package main

import (
	"fmt"
	"bufio"
	"log"
	"os"
	"strconv"
	"sort"
)

func main() {
	file, err := os.Open("input")
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	sums := make([]int, 0)
	sum := 0
	calories := 0
	line := ""
	for scanner.Scan() {
		line = scanner.Text()
		if line == "" {
			sums = append(sums, sum)
			sum = 0
			continue
		}
		calories, err = strconv.Atoi(line)
		sum += calories
	}

	sort.Ints(sums)
	fmt.Println("Sums: ", sums[len(sums) - 3] + sums[len(sums) - 2] + sums[len(sums) - 1])
	
	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}
}
