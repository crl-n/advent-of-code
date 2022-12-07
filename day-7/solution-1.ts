type SimpleFile = {
	name: string,
	size: number
}

type Directory = {
	name: string,
	size: number,
	parentDir: Directory | undefined
	dirs: Map<string, Directory>,
	files: SimpleFile[]
}

const text = await Deno.readTextFile("input")
let lines: string[] = text.split("\n")

let root: Directory = {
	name: "/",
	size: 0,
	parentDir: undefined,
	dirs: new Map<string, Directory>(),
	files: []
}

let currentDir: Directory

// Populate file tree
lines.forEach((line) => {
	if (line === "") {
		//console.log("EOF")
	} else if (line == "$ cd /") {
		currentDir = root
	}
	else if (line.startsWith("$ cd ")) {
		let targetDirName: string = line.substring(5)
		if (targetDirName === ".." && typeof currentDir.parentDir != "undefined") {
			currentDir = currentDir.parentDir
		} else {
			if (!currentDir.dirs.has(targetDirName)) {
				currentDir.dirs.set(targetDirName, <Directory>{
					name: targetDirName,
					size: 0,
					parentDir: currentDir,
					dirs: new Map<string, Directory>(),
					files: []
				})
			}
			let targetDir: Directory | undefined = currentDir.dirs.get(targetDirName)
			if (typeof targetDir != "undefined") {
				currentDir = targetDir
			}
		}
	} else if (line === "$ ls") {
		//console.log(line)
	} else if (line.startsWith("dir")) {
		let parts = line.split(" ")
		currentDir.dirs.set(parts[1], <Directory>{
			name: parts[1],
			size: 0,
			parentDir: currentDir,
			dirs: new Map<string, Directory>(),
			files: []
		})
	} else {
		let parts = line.split(" ")
		currentDir.files.push(<SimpleFile>{
			name: parts[1],
			size: parseInt(parts[0])
		})
	}
})

// Calculate answer
let sum = 0
function recurse(dir: Directory) {
	dir.dirs.forEach(recurse)
	dir.files.forEach((file) => {
		dir.size += file.size
	})
	dir.dirs.forEach((childDir) => {
		dir.size += childDir.size
	})
	if (dir.size <= 100000) {
		sum += dir.size
	}
}

recurse(root)
console.log("Sum:", sum)

let depth = 0
function display(dir: Directory) {
	let pad = "  ".repeat(depth)
	console.log(`${pad}- ${dir.name} (dir)`)
	depth++
	pad = "  ".repeat(depth)
	dir.dirs.forEach(display)
	dir.files.forEach((file) => {
		console.log(`${pad}- ${file.name} (file, size=${file.size})`)
	})
	depth--
}