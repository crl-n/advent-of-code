void main(String argv[]) {
    List<String> lines = null;
    try {
        String filePath = "./01/01.input";
        lines = Files.readAllLines(Paths.get(filePath));
    } catch (IOException e) {
        e.printStackTrace();
    }

    var freq = 0;

    for (String line : lines) {
        freq += Integer.parseInt(line);
    }
    println(freq);
}
