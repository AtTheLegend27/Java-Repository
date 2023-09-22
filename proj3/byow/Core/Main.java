package byow.Core;


import java.io.FileNotFoundException;
import java.io.IOException;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over`
 *  in either keyboard or input string mode.
 */
public class Main {
    //saves the string seed number into this variable
    public static void main(String[] args) {
        //this is to instantiate the map
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            //builds map through string input
            Engine engine = new Engine();
            engine.interactWithInputString(args[1]);
            System.out.println(engine);
        } else {
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        }

    }
}
