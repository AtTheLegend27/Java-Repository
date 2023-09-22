package gitlet;
import java.io.IOException;
import java.nio.file.Files;
import org.dom4j.Branch;
import java.io.File;
import static gitlet.Utils.*;
import java.io.Serializable;
// TODO: any imports you need here
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */

public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File staging = new File(GITLET_DIR, "staging");
    public static final File commits = new File(staging, "commits");
    //change file name

    /* TODO: fill in the rest of this class. */
    public void init() {
        //cwd
        //
        if (GITLET_DIR.exists()) {
            // instead of throw new Illegal Argument Exception we use system.print to system.exit
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        } else {
            //make an exception for mkdir in case for boolean errors
            GITLET_DIR.mkdir();
            staging.mkdir();
            commits.mkdir();
            //File branches = new File(commits, "branches");
            //branches.mkdir();
            //Looked into Date class, Calendar class, TimeZone class for line below
            //Also looked at stackoverflow how to setTimeZone to UTC
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            Date d = new Date(0);
//            System.out.println(d);
            Commit initalCommit = new Commit("initial commit", d, new File[0], null);
            String commitHash = sha1(serialize(initalCommit));
            File headCommit = new File(commits, "HEAD");
            Utils.writeContents(headCommit, commitHash);
            File newCommit = new File(commits, commitHash);
            Utils.writeObject(newCommit, initalCommit);
        }
    }
    //serialize all files in "Files"
    public void add(String fileName ) {
        File addCheck = new File(fileName);
        String headCommitHash = readContentsAsString(new File(commits, "HEAD"));
        if (!addCheck.exists()) {
            System.out.println("File does not exist");
            System.exit(0);
        } else {
            File fileFileName = new File(fileName);
            String fileTester = sha1(serialize(fileName));
            Commit test = Utils.readObject(new File(commits, headCommitHash), Commit.class);
            HashMap<String, byte[]> testingFile = test.toCommit;
            if (testingFile.containsKey(fileTester)){
                if (readContents(fileFileName) == testingFile.get(fileTester)) {
                    return;
                }
            }
            File stagingHelper = new File(staging, fileName);
            if (stagingHelper.exists()) {
                stagingHelper.delete();
                File newStagingHelper = new File(fileName);
                try {
                    Files.copy(newStagingHelper.toPath(), stagingHelper.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                File newStagingHelper = new File(fileName);
                try {
                    Files.copy(newStagingHelper.toPath(), stagingHelper.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void commit(String message) {
        // looks into the staging file area
        File headCommit = new File(commits, "HEAD");
        String headCommitHash = Utils.readContentsAsString(headCommit);

        if (! this.staging.exists()) {
            System.out.println("No changes added to the commit");
            System.exit(0);
        } else if (message.equals("")){
            System.out.println("Please enter a commit message");
            System.exit(0);
        } else { // we have stuff in staging & we have a message
            // apply a date
            Date currentDate = new Date();
            // place a message onto the stages file
            File [] toCommit = this.staging.listFiles();
            Commit currentCommit = new Commit(message, currentDate, toCommit, headCommitHash);
            String commitHash = sha1(serialize(currentCommit));
//            System.out.println(headCommitHash + " " + commitHash);
            File newCommit = new File(commits, commitHash);
            Utils.writeObject(newCommit, currentCommit);
            Utils.writeContents(new File(commits, "HEAD"), commitHash);
            //after commit clear stage directory
            staging.delete();
            staging.mkdir();
        }
    }
    public void log() throws Exception{
        //spit out commit hash, commit date, commit message
        String head = readContentsAsString(new File(commits, "HEAD"));
        Commit test = Utils.readObject(new File(commits, head), Commit.class);
        while(test != null) {
            //for all commits in commits directory, print hash, date, message
            //Looked on Ed --> post #660abc
            String date = new SimpleDateFormat("EEE MMM d hh:mm:ss yyyy ZZZZZ").format(test.timestamp);
            System.out.println("===");
            System.out.println("commit " + sha1(serialize(test)));
            System.out.println("Date: " + date);
            System.out.print(test.message);
            System.out.println("");
            System.out.println();
            //scan trough commits directory
            if (test.parentCommitHash != null) {
                test = Utils.readObject(new File(commits, test.parentCommitHash), Commit.class);
            } else {
                test = null;
            }
        }
        return;
    }
    public void restoreIdLog(String commitId, String fileName){
        //index to correct commit to find commitId
        File restoreFile = new File(commits, commitId);
        if (!restoreFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit restoreTo = Utils.readObject(restoreFile, Commit.class);
        HashMap<String, byte[]> toScan = restoreTo.toCommit;
        String toGet = sha1(serialize(fileName));
        byte[] read = toScan.get(toGet);
        if (read == null){
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else{
            File toRead = new File(GITLET_DIR, "toRead");
            Utils.writeContents(toRead, read);
            //restore to then
            File fileFileName = new File(fileName);
            fileFileName.delete();
            fileFileName.mkdir();
            fileFileName = Utils.readObject(toRead, File.class);
            fileFileName.renameTo(new File(fileName));
        }

    }
    public void restore(String fileName){
        String head = readContentsAsString(new File(commits, "HEAD"));
        restoreIdLog(head, fileName);
    }
}


