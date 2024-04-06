package utils

import "strconv"

func StringsToInts(strs []string) []int {
	var ints []int

	for _, s := range strs {
		v, err := strconv.Atoi(s)

		if (err != nil) {
			panic(err)
		}

		ints = append(ints, v)
	}

	return ints
}
