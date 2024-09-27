const filename = Deno.args[0]
const text = await Deno.readTextFile(filename)
let lines: string[] = text.split("\n")
lines.pop()

let x = 0
let y = 0
let maxX = 0
let minX = 0
let maxY = 0
let minY = 0

lines.forEach((line) => {
	let [direction, count] = line.split(" ")
	
	switch (direction) {
		case "U":
			y += parseInt(count)
			break
		case "D":
			y -= parseInt(count)
			break
		case "L":
			x -= parseInt(count)
			break
		case "R":
			x += parseInt(count)
			break
	}
	if (x < minX) {
		minX = x
	} else if (x > maxX) {
		maxX = x
	}
	if (y < minY) {
		minY = y
	} else if (y > maxY) {
		maxY = y
	}
})


let width = maxX - minX + 1
let height = maxY - minY + 1

let grid: boolean[][] = new Array(height)
for (let i = 0; i < height; i++) {
	grid[i] = new Array(width).fill(false)
}

grid[height + minY - 1][-minX] = true

interface Thing {
	y: number,
	x: number
}

let segments: Thing[] = new Array(10).fill({})
for (let i = 0; i < segments.length; i++) {
	segments[i] = {
		y: height + minY - 1,
		x: -minX
	} as Thing
}

function areAdjacentOrOnSameSquare(tail: Thing, head: Thing) {
	if (Math.sqrt(
		Math.pow(tail.x - head.x, 2) + Math.pow(tail.y - head.y, 2)
	) < 2)
		return true
	return false
}

function moveSegments(segments: Thing[]) {
	for (let i = 1; i < segments.length; i++) {
		if (areAdjacentOrOnSameSquare(segments[i], segments[i - 1]))
			continue
		let deltaY = segments[i - 1].y - segments[i].y
		let deltaX = segments[i - 1].x - segments[i].x
		if (deltaY > 0) {
			segments[i].y++
		} else if (deltaY < 0) {
			segments[i].y--
		}
		if (deltaX > 0) {
			segments[i].x++
		} else if (deltaX < 0) {
			segments[i].x--
		}
	}
	grid[segments[9].y][segments[9].x] = true
}

lines.forEach((line) => {
	let [direction, stepsString] = line.split(" ")
	let steps = parseInt(stepsString)

	switch (direction) {
		case "R":
			for (let offset = 1; offset <= steps; offset++) {
				segments[0].x++
				moveSegments(segments)
			}
			break
		case "L":
			for (let offset = 1; offset <= steps; offset++) {
				segments[0].x--
				moveSegments(segments)
			}
			break
		case "D":
			for (let offset = 1; offset <= steps; offset++) {
				segments[0].y++
				moveSegments(segments)
			}
			break
		case "U":
			for (let offset = 1; offset <= steps; offset++) {
				segments[0].y--
				moveSegments(segments)
			}
			break
	}
})

let visited = 0
grid.forEach((row) => {
	visited += row.filter(Boolean).length
})

console.log("Visited:", visited)
