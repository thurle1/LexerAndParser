import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Parser {
    final static String fileName = "fileToRead.txt";
    static ArrayList<Tok_Types> expected = new ArrayList<Tok_Types>();
    static Position errPos = null;
    static Tok_Types saw = null;
    static String AST = "";

    private static boolean parseToks(Lexer myLexer) {
        try {
            AST = expression(myLexer);
            if (myLexer.kind() != Tok_Types.end_of_text) {
                errPos = myLexer.position();
                expected.add(Tok_Types.end_of_text);
                saw = myLexer.kind();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static String expression(Lexer myLexer) throws Exception {
        return booleanExpression(myLexer);
    }

    private static String booleanExpression(Lexer myLexer) throws Exception {
        StringBuilder val = new StringBuilder(booleanTerm(myLexer));
        while (myLexer.kind() == Tok_Types.or) {
            myLexer.next();
            val.insert(0, "(OP_OR");
            val.append(booleanTerm(myLexer) + ")");
        }
        return val.toString();
    }

    private static String booleanTerm(Lexer myLexer) throws Exception {
        StringBuilder val = new StringBuilder(booleanFactor(myLexer));
        while (myLexer.kind() == Tok_Types.and) {
            myLexer.next();
            val.insert(0, "(OP_AND");
            val.append(booleanFactor(myLexer) + ")");
        }
        return val.toString();
    }

    private static String booleanFactor(Lexer myLexer) throws Exception {
        boolean hasNot = false;
        StringBuilder val = new StringBuilder();
        if (myLexer.kind() == Tok_Types.not) {
            val.append("(OP_NOT");
            myLexer.next();
            hasNot = true;
        }
        String val2 = arithmaticExpression(myLexer);
        if (myLexer.kind() == Tok_Types.equal) {
            myLexer.next();
            val.append("(OP_EQ" + val2);
            val.append(arithmaticExpression(myLexer) + ")");
        } else if (myLexer.kind() == Tok_Types.less_than) {
            myLexer.next();
            val.append("(OP_LT" + val2);
            val.append(arithmaticExpression(myLexer) + ")");
        } else {
            val.append(val2);
        }
        if (hasNot) {
            val.append(")");
        }
        return val.toString();
    }

    private static String arithmaticExpression(Lexer myLexer) throws Exception {
        StringBuilder val = new StringBuilder();
        val.append(term(myLexer));
        while (myLexer.kind() == Tok_Types.plus || myLexer.kind() == Tok_Types.minus) {
            if (myLexer.kind() == Tok_Types.plus) {
                val.insert(0, "(OP_PLUS");
                myLexer.next();
                val.append(term(myLexer));
                val.append(")");
            } else {
                val.insert(0, "(OP_MINUS");
                myLexer.next();
                val.append(term(myLexer));
                val.append(")");
            }
        }
        return val.toString();
    }

    private static String term(Lexer myLexer) throws Exception {
        StringBuilder val = new StringBuilder();
        val.append(factor(myLexer));
        while (myLexer.kind() == Tok_Types.times || myLexer.kind() == Tok_Types.divide) {
            if (myLexer.kind() == Tok_Types.times) {
                val.insert(0, "(OP_MULT");
                myLexer.next();
                val.append(factor(myLexer));
                val.append(")");
            } else {
                val.insert(0, "(OP_DIV");
                myLexer.next();
                val.append(factor(myLexer));
                val.append(")");
            }
        }
        return val.toString();
    }

    private static String factor(Lexer myLexer) throws Exception {
        StringBuilder val = new StringBuilder();
        if (myLexer.kind() == Tok_Types.true_tok || myLexer.kind() == Tok_Types.false_tok
                || myLexer.kind() == Tok_Types.num) {
            val.append(literal(myLexer));
        } else if (myLexer.kind() == Tok_Types.id) {
            val.append("(IDENTIFIER " + myLexer.value() + ")");
            myLexer.next();
        } else if (myLexer.kind() == Tok_Types.open_paren) {
            myLexer.next();
            val.append(expression(myLexer));
            if (myLexer.kind() == Tok_Types.close_paren) {
                myLexer.next();
            } else {
                expected.add(Tok_Types.close_paren);
                errPos = myLexer.position();
                saw = myLexer.kind();

                throw new Exception();
            }
        } else {
            expected.add(Tok_Types.true_tok);
            expected.add(Tok_Types.false_tok);
            expected.add(Tok_Types.num);
            expected.add(Tok_Types.id);
            expected.add(Tok_Types.open_paren);
            errPos = myLexer.position();
            saw = myLexer.kind();
            throw new Exception();
        }
        return val.toString();
    }

    private static String literal(Lexer myLexer) {
        StringBuilder val = new StringBuilder();
        if (myLexer.kind() == Tok_Types.false_tok) {
            val.append("(BOOL_LITERAL ");
            val.append("False)");
        } else if (myLexer.kind() == Tok_Types.true_tok) {
            val.append("(BOOL_LITERAL ");
            val.append("True)");
        } else {
            val.append("(INT_LITERAL ");
            val.append(myLexer.value() + ")");
        }

        myLexer.next();

        return val.toString();

    }

    public static void main(String[] args) {
        Lexer myLexer = new Lexer();
        String fileString = "";
        try {
            fileString = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (Exception e) {
            System.out.println("Something went wrong reading the file");
        }

        try {
            myLexer.readString(fileString);
        } catch (Exception e) {
            System.out.println("Something went wrong lexing");
            e.printStackTrace();
        }
        String resultString = "";
        if (parseToks(myLexer)) {
            resultString = "Successful Parse\n";
            resultString += "AST: " + AST;
        } else {
            StringBuilder myBuilder = new StringBuilder("Unsuccessful Parse, Expected one of these: \n");
            for (Tok_Types tok : expected) {
                switch (tok) {
                case true_tok:
                    myBuilder.append("True\n");
                    break;
                case false_tok:
                    myBuilder.append("False\n");
                    break;
                case num:
                    myBuilder.append("Number\n");
                    break;
                case id:
                    myBuilder.append("Identifier\n");
                    break;
                case open_paren:
                    myBuilder.append("(\n");
                    break;
                case close_paren:
                    myBuilder.append(")\n");
                    break;
                case end_of_text:
                    myBuilder.append("End Of Text\n");
                    break;
                default:
                    myBuilder.append("Something went wrong");
                }
            }
            myBuilder.append("at ");
            myBuilder.append(errPos);
            myBuilder.append("\nBut saw: ");
            myBuilder.append(saw);
            resultString = myBuilder.toString();
        }
        System.out.println(resultString);
    }
}
