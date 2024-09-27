const text = await Deno.readTextFile("input")
let lines: string[] = text.split("\n")
lines.pop()

let counted: boolean[][] = []
for (let i = 0; i < lines.length; i++) {
	counted.push([])
	for (let j = 0; j < lines[0].length; j++) {
		counted[i].push(false)
	}
}

let count = 0

// From left
for (let i = 1; i < lines.length - 1; i++) {
	let highest = lines[i][0]
	for (let j = 1; j < lines[i].length - 1; j++) {
		if (lines[i][j] > highest) {
			if (counted[i][j] == false) {
				counted[i][j] = true
				count++
			}
			highest = lines[i][j]
		}
	}
}

// From right
for (let i = 1; i < lines.length - 1; i++) {
	let highest = lines[i][lines[i].length - 1]
	for (let j = lines[i].length - 2; j > 0; j--) {
		if (lines[i][j] > highest) {
			if (counted[i][j] == false) {
				counted[i][j] = true
				count++
			}
			highest = lines[i][j]
		}
	}
}

// From top
for (let j = 1; j < lines[0].length - 1; j++) {
	let highest = lines[0][j]
	for (let i = 1; i < lines.length - 1; i++) {
		if (lines[i][j] > highest) {
			if (counted[i][j] == false) {
				counted[i][j] = true
				count++
			}
			highest = lines[i][j]
		}
	}
}

// From bottom
for (let j = 1; j < lines[0].length - 1; j++) {
	let highest = lines[lines.length - 1][j]
	for (let i = lines.length - 2; i > 0; i--) {
		if (lines[i][j] > highest) {
			if (counted[i][j] == false) {
				counted[i][j] = true
				count++
			}
			highest = lines[i][j]
		}
	}
}

count += lines.length + lines.length + lines[0].length + lines[0].length - 4

console.log("Visible trees: ", count)
