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

let head: Thing = {
	y: height + minY - 1,
	x: -minX
}
let tail: Thing = {
	y: height + minY - 1,
	x: -minX
}

function areAdjacentOrOnSameSquare(tail: Thing, head: Thing) {
	if (Math.sqrt(
		Math.pow(tail.x - head.x, 2) + Math.pow(tail.y - head.y, 2)
	) < 2)
		return true
	return false
}

lines.forEach((line) => {
	let [direction, stepsString] = line.split(" ")
	let steps = parseInt(stepsString)

	switch (direction) {
		case "R":
			for (let offset = 1; offset <= steps; offset++) {
				head.x++
				if (areAdjacentOrOnSameSquare(tail, head))
					continue
				if (tail.y != head.y)
					tail.y = head.y
				if (tail.x == head.x - 2) {
					tail.x++	
				}
				grid[tail.y][tail.x] = true
			}
			break
		case "L":
			for (let offset = 1; offset <= steps; offset++) {
				head.x--
				if (areAdjacentOrOnSameSquare(tail, head))
					continue
				if (tail.y != head.y)
					tail.y = head.y
				tail.x--
				grid[tail.y][tail.x] = true
			}
			break
		case "D":
			for (let offset = 1; offset <= steps; offset++) {
				head.y++
				if (areAdjacentOrOnSameSquare(tail, head))
					continue
				if (tail.x != head.x)
					tail.x = head.x
				tail.y++
				grid[tail.y][tail.x] = true
			}
			break
		case "U":
			for (let offset = 1; offset <= steps; offset++) {
				head.y--
				if (areAdjacentOrOnSameSquare(tail, head))
					continue
				if (tail.x != head.x)
					tail.x = head.x
				tail.y--
				grid[tail.y][tail.x] = true
			}
			break
	}
})

let visited = 0
grid.forEach((row) => {
	visited += row.filter(Boolean).length
})

console.log("Tail visited:", visited)
