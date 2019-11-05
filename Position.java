public class Position {
    private int line_num;
    private int char_num;

    public Position(int line_num, int char_num) {
        this.line_num = line_num;
        this.char_num = char_num;
    }

    public int getLineNum() {
        return this.line_num;
    }

    public int getCharNum() {
        return this.char_num;
    }

    public String toString() {
        StringBuilder myBuilder = new StringBuilder();
        myBuilder.append("Line: ");
        myBuilder.append(this.line_num);
        myBuilder.append(" Character: ");
        myBuilder.append(this.char_num);
        return myBuilder.toString();
    }

}
