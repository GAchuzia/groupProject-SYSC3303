import java.io.FileNotFoundException;

/**
 * The main file of the program.
 *
 * @author Matteo Golin, 101220709
 * @version 0.0.0
 */
public class Main {
    public static void main(String[] args) {
        FloorSubsystem fsys;
        try {
            fsys = new FloorSubsystem("./testdata.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Could not open input file.");
            return;
        }
        fsys.run();
    }
}
