
/* Enum for all the types of tokens */
enum Tok_Types {
    and, or, equal, less_than, not, plus, minus, times, divide, open_paren, close_paren, false_tok, true_tok, id, num,
    end_of_text, error
}

/* Class that will hold the information about tokens */

public class Token {
    Tok_Types type;
    String value;
    Position position;
    /* Constructor for new tokens */

    public Token(Tok_Types type, String value, int line_num, int char_num) {
        this.type = type;
        this.value = value;
        this.position = new Position(line_num, char_num);
    }

    public String toString() {
        StringBuilder myBuilder = new StringBuilder();
        myBuilder.append("Token Type: ");
        switch (this.type) {
        case and:
            myBuilder.append("And");
            break;
        case or:
            myBuilder.append("Or");
            break;
        case equal:
            myBuilder.append("=");
            break;
        case less_than:
            myBuilder.append("<");
            break;
        case not:
            myBuilder.append("not");
            break;
        case plus:
            myBuilder.append("+");
            break;
        case minus:
            myBuilder.append("-");
            break;
        case times:
            myBuilder.append("*");
            break;
        case divide:
            myBuilder.append("/");
            break;
        case open_paren:
            myBuilder.append("(");
            break;
        case close_paren:
            myBuilder.append(")");
            break;
        case false_tok:
            myBuilder.append("false");
            break;
        case true_tok:
            myBuilder.append("true");
            break;
        case id:
            myBuilder.append("ID");
            break;
        case num:
            myBuilder.append("NUM");
            break;
        case end_of_text:
            myBuilder.append("End of Text");
            break;
        case error:
            myBuilder.append("Unknown Character " + this.value + " at Line: " + this.position.getLineNum()
                    + " Character: " + this.position.getCharNum());
        }
        if (this.type != Tok_Types.end_of_text && this.type != Tok_Types.error) {
            if (this.value != null) {
                myBuilder.append(".  Value: " + this.value);
            }
            myBuilder.append(
                    ".  Line Number: " + this.position.getLineNum() + " Character Num: " + this.position.getCharNum());
        }
        return myBuilder.toString();
    }

    public static void main(String[] args) {
        Token testTok = new Token(Tok_Types.end_of_text, null, 0, 0);
        System.out.println(testTok);
    }
}
