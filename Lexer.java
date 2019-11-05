
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Lexer {
    final static String fileName = "fileToRead.txt";
    private ArrayList<Token> tok_list = new ArrayList<Token>();
    private int current_tok = 0;

    public Lexer() {

    }

    public boolean next() {
        if (this.current_tok < this.tok_list.size() - 1) {
            current_tok++;
            return true;
        } else {
            return false;
        }
    }

    public Tok_Types kind() {
        return tok_list.get(current_tok).type;
    }

    public String value() {
        return tok_list.get(current_tok).value;
    }

    public Position position() {
        return tok_list.get(current_tok).position;
    }

    void readString(String stringToRead) throws Exception {
        if (this.tok_list.size() != 0) {
            throw new Exception("readString has already been called");
        }
        int currChar = 0;
        int lineNum = 1;
        int lineCharNum = 1;
        while (currChar < stringToRead.length()) {
            char myChar = stringToRead.charAt(currChar);
            if (Character.isDigit(myChar)) {
                StringBuilder myBuilder = new StringBuilder();
                int count = currChar;
                myBuilder.append(myChar);
                while (count != stringToRead.length() - 1 && Character.isDigit(stringToRead.charAt(count + 1))) {
                    myBuilder.append(stringToRead.charAt(count + 1));
                    count++;
                }
                Token myTok = new Token(Tok_Types.num, myBuilder.toString(), lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum += (count + 1) - currChar;
                currChar = count + 1;

            } else if (Character.isWhitespace(myChar)) {
                if (myChar == '\n') {
                    lineNum++;
                    lineCharNum = 1;
                } else {
                    lineCharNum++;
                }
                currChar++;
            } else if (isAlpha(myChar)) {
                if ((currChar + 2 < stringToRead.length()
                        && stringToRead.substring(currChar, currChar + 3).equals("and"))
                        && (currChar + 3 == stringToRead.length()
                                || Character.isWhitespace(stringToRead.charAt(currChar + 3)))) {
                    Token myToken = new Token(Tok_Types.and, null, lineNum, lineCharNum);
                    this.tok_list.add(myToken);
                    currChar += 3;
                    lineCharNum += 3;
                } else if ((currChar + 1 < stringToRead.length()
                        && stringToRead.substring(currChar, currChar + 2).equals("or"))
                        && (currChar + 2 == stringToRead.length()
                                || Character.isWhitespace(stringToRead.charAt(currChar + 2)))) {
                    Token myToken = new Token(Tok_Types.or, null, lineNum, lineCharNum);
                    this.tok_list.add(myToken);
                    currChar += 2;
                    lineCharNum += 2;
                } else if ((currChar + 2 < stringToRead.length()
                        && stringToRead.substring(currChar, currChar + 3).equals("not"))
                        && (currChar + 3 == stringToRead.length()
                                || Character.isWhitespace(stringToRead.charAt(currChar + 3)))) {
                    Token myToken = new Token(Tok_Types.not, null, lineNum, lineCharNum);
                    this.tok_list.add(myToken);
                    currChar += 3;
                    lineCharNum += 3;
                } else if ((currChar + 3 < stringToRead.length()
                        && stringToRead.substring(currChar, currChar + 4).equals("true"))
                        && (currChar + 4 == stringToRead.length()
                                || Character.isWhitespace(stringToRead.charAt(currChar + 4)))) {
                    Token myToken = new Token(Tok_Types.true_tok, null, lineNum, lineCharNum);
                    this.tok_list.add(myToken);
                    currChar += 4;
                    lineCharNum += 4;
                } else if ((currChar + 4 < stringToRead.length()
                        && stringToRead.substring(currChar, currChar + 5).equals("false"))
                        && (currChar + 5 == stringToRead.length()
                                || Character.isWhitespace(stringToRead.charAt(currChar + 5)))) {
                    Token myToken = new Token(Tok_Types.false_tok, null, lineNum, lineCharNum);
                    this.tok_list.add(myToken);
                    currChar += 5;
                    lineCharNum += 5;
                } else {
                    StringBuilder myBuilder = new StringBuilder();
                    int count = currChar;
                    myBuilder.append(myChar);
                    while (count != stringToRead.length() - 1
                            && (isAlpha(stringToRead.charAt(count + 1)) || (stringToRead.charAt(count + 1) == '_')
                                    || (Character.isDigit(stringToRead.charAt(count + 1))))) {
                        myBuilder.append(stringToRead.charAt(count + 1));
                        count++;
                    }
                    Token myTok = new Token(Tok_Types.id, myBuilder.toString(), lineNum, lineCharNum);
                    this.tok_list.add(myTok);
                    lineCharNum += (count + 1) - currChar;
                    currChar = count + 1;
                }
            } else if (myChar == '=') {
                Token myTok = new Token(Tok_Types.equal, null, lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum++;
                currChar++;
            } else if (myChar == '<') {
                Token myTok = new Token(Tok_Types.less_than, null, lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum++;
                currChar++;
            } else if (myChar == '+') {
                Token myTok = new Token(Tok_Types.plus, null, lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum++;
                currChar++;
            } else if (myChar == '-') {
                Token myTok = new Token(Tok_Types.minus, null, lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum++;
                currChar++;
            } else if (myChar == '*') {
                Token myTok = new Token(Tok_Types.times, null, lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum++;
                currChar++;
            } else if (myChar == '/') {
                if (currChar + 1 < stringToRead.length() && stringToRead.charAt(currChar + 1) == '/') {
                    while (currChar < stringToRead.length() && stringToRead.charAt(currChar) != '\n') {
                        currChar++;
                        lineCharNum++;
                    }
                } else {
                    Token myTok = new Token(Tok_Types.divide, null, lineNum, lineCharNum);
                    this.tok_list.add(myTok);
                    lineCharNum++;
                    currChar++;
                }

            } else if (myChar == '(') {
                Token myTok = new Token(Tok_Types.open_paren, null, lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum++;
                currChar++;
            } else if (myChar == ')') {
                Token myTok = new Token(Tok_Types.close_paren, null, lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum++;
                currChar++;
            } else {
                Token myTok = new Token(Tok_Types.error, "" + myChar, lineNum, lineCharNum);
                this.tok_list.add(myTok);
                lineCharNum++;
                currChar++;
            }
        }
        this.tok_list.add(new Token(Tok_Types.end_of_text, null, lineNum, lineCharNum));
    }

    private static boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Token myToken : this.tok_list) {
            builder.append(myToken);
            builder.append('\n');
            if (myToken.type == Tok_Types.error)
                break;
        }

        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        Lexer myLexer = new Lexer();
        String fileString = "";
        try {
            fileString = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (Exception e) {
            System.out.println("Something went wrong reading the file");
        }
        myLexer.readString(fileString);
        System.out.println(myLexer);
    }

}
