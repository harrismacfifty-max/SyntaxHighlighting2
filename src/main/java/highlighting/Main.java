package highlighting;

import highlighting.antlr.PrettyPrinter;
import java.util.List;
import java.util.Scanner;

public class Main {

  public static void main(String... args) {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Leerzeichen pro Einrückstufe eingeben (z.B. 2, 4 oder 8): ");
    int indentWidth = readIndentWidth(scanner);

    List<Example> examples =
        List.of(
            new Example(
                "Einfache Klasse mit Feld und Methode",
                "public class Demo{private String name;public String getName(){return name;}}"),
            new Example(
                "Methode mit if/else und while",
                "public class Control{public String run(){if(null)return \"empty\";else"
                    + " while(null){return \"loop\";}}}"),
            new Example(
                "Verschachtelte Blöcke",
                "public class Nested{public String test(){{{return \"deep\";}}}}"));

    for (Example example : examples) {
      printExample(example, indentWidth);
    }
  }

  private static int readIndentWidth(Scanner scanner) {
    if (!scanner.hasNextInt()) {
      return 2;
    }

    int indentWidth = scanner.nextInt();

    if (indentWidth < 0) {
      return 2;
    }

    return indentWidth;
  }

  private static void printExample(Example example, int indentWidth) {
    System.out.println();
    System.out.println("=".repeat(80));
    System.out.println(example.title());
    System.out.println("=".repeat(80));

    System.out.println();
    System.out.println("Input:");
    System.out.println(example.sourceCode());

    System.out.println();
    System.out.println("Pretty-Printer-Ausgabe:");
    System.out.println(PrettyPrinter.prettyPrint(example.sourceCode(), indentWidth));
  }

  private record Example(String title, String sourceCode) {}
}
