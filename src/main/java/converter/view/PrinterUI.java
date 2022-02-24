package converter.view;

/**
 * simple UI class to handle output of converting, i.e writing to stdout as of now.
 */
public class PrinterUI {

    private final String output;

    public PrinterUI(String output) {
        this.output = output;
    }

    public void print() {
        System.out.println(output);
    }
}
