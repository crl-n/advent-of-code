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
		//
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
function recurse(dir: Directory) {
	dir.dirs.forEach(recurse)
	dir.files.forEach((file) => {
		dir.size += file.size
	})
	dir.dirs.forEach((childDir) => {
		dir.size += childDir.size
	})
}
recurse(root)

let totalSpace = 70000000
let neededSpace = 30000000
let usedSpace = root.size
let availableSpace = totalSpace - usedSpace
let needToDel = neededSpace - availableSpace
let smallest = usedSpace
function findDirToDel(dir: Directory) {
	if (dir.size >= needToDel && dir.size < smallest) {
		smallest = dir.size
	}
	dir.dirs.forEach(findDirToDel)
}
findDirToDel(root)
console.log("Smallest dir size that is enough: ", smallest)