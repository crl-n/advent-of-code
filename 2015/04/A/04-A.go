package main

import (
	"aoc15/utils"
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"strconv"
	"strings"
)

func main() {
	input := strings.Trim(utils.GetInput(), "\n")

	n := 1
	var s string
	var hash [16]byte

	for {
		s = input + strconv.Itoa(n)
		hash = md5.Sum([]byte(s))
		hex := hex.EncodeToString(hash[:])
		if strings.HasPrefix(hex, "00000") {
			break
		}
		n++
	}

	fmt.Println(n)
}
