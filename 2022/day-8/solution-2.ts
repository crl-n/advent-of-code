const text = await Deno.readTextFile("input")
let lines: string[] = text.split("\n")
lines.pop()

let height = lines.length
let width = lines[0].length

function calcScenicScore(iStart: number, jStart: number) {
	let treeHeight = lines[iStart][jStart]
	let score = 1
	let count = 0

 	for (let i = iStart + 1; i < height; i++) {
		if (lines[i][jStart] < treeHeight) {
			count++
		} else {
			count++
			break
		}
	}

	score = score * count
	count = 0

 	for (let i = iStart - 1; i >= 0; i--) {
		if (lines[i][jStart] < treeHeight) {
			count++
		} else {
			count++
			break
		}
	}

	score = score * count
	count = 0

 	for (let j = jStart + 1; j < width; j++) {
		if (lines[iStart][j] < treeHeight) {
			count++
		} else {
			count++
			break
		}
	}

	score = score * count
	count = 0

 	for (let j = jStart - 1; j >= 0; j--) {
		if (lines[iStart][j] < treeHeight) {
			count++
		} else {
			count++
			break
		}
	}

	score = score * count
	return score
}

let highestScenicScore = 0

for (let i = 0; i < height; i++) {
	for (let j = 0; j < width; j++) {
		let scenicScore = calcScenicScore(i, j)
		if (scenicScore > highestScenicScore) {
			highestScenicScore = scenicScore
		}
	}
}

console.log("Highest scenic score: ", highestScenicScore)
