package highlighting.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public final class PrettyPrinter {

  private PrettyPrinter() {}

  public static MiniJavaParser.CompilationUnitContext parse(String sourceCode) {
    MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(sourceCode));
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    MiniJavaParser parser = new MiniJavaParser(tokenStream);

    return parser.compilationUnit();
  }

  public static String prettyPrint(String sourceCode, int indentWidth) {
    PrettyPrinterVisitor visitor = new PrettyPrinterVisitor(indentWidth);
    visitor.visit(parse(sourceCode));
    return visitor.result();
  }
}
