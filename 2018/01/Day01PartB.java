void main(String argv[]) {
    List<String> lines = null;
    try {
        String filePath = "./01/01.input";
        lines = Files.readAllLines(Paths.get(filePath));
    } catch (IOException e) {
        e.printStackTrace();
    }

    var freq = 0;
    var seen = new HashSet<Integer>();

    var i = 0;
    while (true) {
        var line = lines.get( i % lines.size());
        freq += Integer.parseInt(line);
        if (seen.contains(freq)) break;
        seen.add(freq);
        i++;
    }
    println("Solution: " + freq);
}
