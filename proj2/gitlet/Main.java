package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */

public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    static boolean checkOperands(int size, String ... arguments) {
        if (arguments.length != size){
            System.out.println("Incorrect operands");
            System.exit(0);
            return false;
        } else {
            return true;
        }
    }
    public static void main(String[] args) throws Exception {
        // TODO: what if args is empty?
        if (args.length == 0){
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        Repository repo = new Repository();
        String firstArg = args[0];
        switch(firstArg) {
            case "init": {
                // TODO: handle the `init` command
                if (checkOperands(1, args)) {
                    repo.init();
                }
                break;
            }
            case "add": {
                // TODO: handle the `add [filename]` command
                if (checkOperands(2, args)) {
                    repo.add(args[1]);
                }
                break;
            }
            case "commit": {
                if (checkOperands(2, args)) {
                    repo.commit(args[1]);
                }
                break;
            }
            case "log": {
                if (checkOperands(1, args)) {
                    repo.log();
                }
                break;
            }
            case "restore": {
                if (args.length == 4) {
                    repo.restoreIdLog(args[1], args[3]);
                    break;
                } else if (args.length == 3) {
                    repo.restore(args[2]);
                    break;
                } else {
                    System.out.println("Incorrect operands");
                    System.exit(0);
                }
            }
            default:
                //system.out.println
                System.out.println("No command with that name exists.");
                System.exit(0);
        }

    }

}

