package main

import (
	"aoc15/utils"
	"bytes"
	"crypto/md5"
	"fmt"
	"strings"
)

func main() {
	input := strings.Trim(utils.GetInput(), "\n")

	n := 1
	var s string
	var hash [16]byte

	for {
		s = fmt.Sprintf("%s%d", input, n)
		hash = md5.Sum([]byte(s))
		if bytes.HasPrefix(hash[:], []byte{0, 0, 0, 0, 0}) {
			break
		}
		n++
	}

	fmt.Println(s)
	fmt.Println(hash)
	fmt.Println(string(hash[:]))
	fmt.Println(n)
}
