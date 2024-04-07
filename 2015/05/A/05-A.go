package main

import (
	"aoc15/utils"
	"fmt"
	"strings"
)

var vowels = "aeiou"

func countVowels(s string) int {
	count := 0
	for i := 0; i < len(vowels); i++ {
		count += strings.Count(s, string(vowels[i]))
	}
	return count
}

func hasDoubles(s string) bool {
	for i := 0; i < len(s) - 1; i++ {
		if s[i] == s[i + 1] {
			return true
		}
	}
	return false
}

func hasForbiddenSubstrings(s string) bool {
	forbidden := []string{"ab", "cd", "pq", "xy"}

	for i := 0; i < len(forbidden); i++ {
		if strings.Contains(s, forbidden[i]) {
			return true
		}
	}
	return false
}

func isNice(s string) bool {
	if countVowels(s) < 3 {
		return false
	}
	if !hasDoubles(s) {
		return false
	}
	if hasForbiddenSubstrings(s) {
		return false
	}
	return true	
}

func main() {
	input := utils.GetInput()

	lines := strings.Split(strings.Trim(input, "\n"), "\n")

	niceCount := 0

	for i := 0; i < len(lines); i++ {
		if isNice(lines[i]) {
			niceCount++
		}
	}

	fmt.Println(niceCount)
}
