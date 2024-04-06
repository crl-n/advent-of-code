package utils

import "os"

func GetInput() string {
	data, err := os.ReadFile("input")
	if err != nil {
		panic(err)
	}

	return string(data)
}
