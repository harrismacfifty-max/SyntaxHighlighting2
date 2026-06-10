package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaColours;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class AntlrTokenCollector extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(text));
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();

    List<HighlightRegion> regions = new ArrayList<>();
    List<Token> tokens = tokenStream.getTokens();

    for (int i = 0; i < tokens.size(); i++) {
      Token token = tokens.get(i);

      if (token.getType() == Token.EOF) {
        continue;
      }

      if (token.getType() == MiniJavaLexer.AT) {
        addRegion(regions, token, MiniJavaColours.ANNOTATION_COLOUR);
        addFollowingAnnotationIdentifier(regions, tokens, i);
        continue;
      }

      Color colour = colourFor(token.getType());

      if (colour != null) {
        addRegion(regions, token, colour);
      }
    }

    return regions;
  }

  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> normalized) {
    return normalized;
  }

  private void addFollowingAnnotationIdentifier(
      List<HighlightRegion> regions, List<Token> tokens, int atIndex) {
    int nextIndex = atIndex + 1;

    if (nextIndex >= tokens.size()) {
      return;
    }

    Token nextToken = tokens.get(nextIndex);

    if (nextToken.getType() == MiniJavaLexer.IDENTIFIER) {
      addRegion(regions, nextToken, MiniJavaColours.ANNOTATION_COLOUR);
    }
  }

  private void addRegion(List<HighlightRegion> regions, Token token, Color colour) {
    int start = token.getStartIndex();
    int end = token.getStopIndex() + 1;

    regions.add(new HighlightRegion(start, end, colour));
  }

  private Color colourFor(int tokenType) {
    return switch (tokenType) {
      case MiniJavaLexer.STRING_LITERAL -> MiniJavaColours.STRING_LITERAL_COLOUR;
      case MiniJavaLexer.CHAR_LITERAL -> MiniJavaColours.CHAR_LITERAL_COLOUR;

      case MiniJavaLexer.LINE_COMMENT -> MiniJavaColours.LINE_COMMENT_COLOUR;
      case MiniJavaLexer.BLOCK_COMMENT -> MiniJavaColours.BLOCK_COMMENT_COLOUR;
      case MiniJavaLexer.JAVADOC_COMMENT -> MiniJavaColours.JAVADOC_COMMENT_COLOUR;

      case MiniJavaLexer.PACKAGE,
          MiniJavaLexer.IMPORT,
          MiniJavaLexer.CLASS,
          MiniJavaLexer.PUBLIC,
          MiniJavaLexer.PRIVATE,
          MiniJavaLexer.FINAL,
          MiniJavaLexer.RETURN,
          MiniJavaLexer.NULL,
          MiniJavaLexer.NEW,
          MiniJavaLexer.IF,
          MiniJavaLexer.ELSE,
          MiniJavaLexer.WHILE,
          MiniJavaLexer.EXTENDS,
          MiniJavaLexer.IMPLEMENTS ->
          MiniJavaColours.KEYWORD_COLOUR;

      default -> null;
    };
  }
}
