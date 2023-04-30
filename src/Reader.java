import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Reader implements Iterator<String> {
    private int numFiles;
    private int numRecords = 100;
    private int currFile;
    private int currRecord;
    private char[] block;
    private static final int BYTES_PER_RECORD = 40;

    public Reader() {
        numFiles = new File("Project2Dataset").listFiles().length;
        currFile = 1;
        currRecord = 1;
        block = readBlock(1);
    }

    @Override
    public boolean hasNext() {
        if (currRecord == numRecords && currFile == numFiles) {
            return false;
        } else if (block == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        char[] record = new char[BYTES_PER_RECORD];
        System.arraycopy(block, BYTES_PER_RECORD * (currRecord - 1), record, 0, BYTES_PER_RECORD);

        currRecord++;
        if (currRecord > numRecords) {
            currRecord = 1;
            currFile++;
            block = readBlock(currFile);
        }

        return new String(record);
    }

    private char[] readBlock(int fileID) {
        while (fileID <= numFiles) {
            try {
                File file = new File("Project2Dataset/F" + fileID + ".txt");
                if (file.exists()) {
                    return new String(Files.readAllBytes(file.toPath())).toCharArray();
                } else {
                    System.out.println("File " + fileID + " could not be found");
                }
            } catch (IOException e) {
                System.out.println("File " + fileID + " could not be found");
                e.printStackTrace();
            }
            fileID++;
        }
        return null;
    }

    private void handleFileNotFound(int fileID) {
        System.out.println("File " + fileID + " could not be found");
        currFile = numFiles;
        currRecord = numRecords;
        block = null;
    }

    protected static void printRecords(int fileID, int[] recordIDs) {
        try {
            File file = new File("Project2Dataset/F" + fileID + ".txt");
            if (file.exists()) {
                String block = new String(Files.readAllBytes(file.toPath()));
                for (int rID : recordIDs) {
                    System.out.println(block.substring((rID - 1) * BYTES_PER_RECORD, rID * BYTES_PER_RECORD));
                }
            } else {
                System.out.println("File " + fileID + " could not be found");
            }
        } catch (IOException e) {
            System.out.println("File " + fileID + " could not be found");
            e.printStackTrace();
        }
    }

    protected static void printRecord(String s) {
        String[] fileAndRecords = s.split(";");
        Arrays.stream(fileAndRecords).forEach(far -> {
            int fileID = Integer.parseInt(far.substring(0, 2));
            int[] recordIDs = Arrays.stream(far.substring(3).replace(" ", "").split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            printRecords(fileID, recordIDs);
        });
        System.out.println("Number of files read: " + fileAndRecords.length);
    }
}
