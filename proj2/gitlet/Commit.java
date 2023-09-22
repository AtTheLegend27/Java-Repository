package gitlet;


// TODO: any imports you need here

import org.dom4j.Branch;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.serialize;
import static gitlet.Utils.sha1;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    public String message;
    public HashMap<String, byte[]> toCommit;
    public Date timestamp;
    private Branch branch;
    public String parentCommitHash;

    /* TODO: fill in the rest of this class. */

    /**
     * Constructor for commit class
     * options: can do multiple constructors or just one based on how you want to organize later
     *          If I do two constructors, do one for initial commits and one for general commits
     *          I would reccomend doing one constructor to be clean and efficient
     * @param message : commit message just like when doing git commit -m "blah blah"
     * @param date : timestamp signature of when commit was made
     * @param fileRefs : list of file path references? Depends on how you use File class
     */
    public Commit(String message, Date date, File[] fileRefs, String parentHash) {
        this.message = message;
        this.timestamp = date;
        this.parentCommitHash = parentHash;
        //this.branch = branch;
        toCommit = generateCommitHashMap(fileRefs);
    }

    public HashMap<String, byte[]> generateCommitHashMap(File[] fileRefs) {
        // return a hash
        HashMap<String, byte[]> fileDict = new HashMap<String, byte[]>();
        for (File f :fileRefs){
            fileDict.put(sha1(serialize(f.getName())), Utils.serialize(f));
        }
        return fileDict;
    }
}
