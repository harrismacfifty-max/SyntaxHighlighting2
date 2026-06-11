package highlighting.antlr;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrettyPrinterTest {

  @Test
  void givenValidMiniJavaProgram_whenParse_thenCompilationUnitIsCreated() {
    // given
    String sourceCode = "public class Demo { public void run() { return; } }";

    // when
    MiniJavaParser.CompilationUnitContext tree = PrettyPrinter.parse(sourceCode);

    // then
    assertNotNull(tree);
    assertEquals(1, tree.typeDecl().size());
  }
}
