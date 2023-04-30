import java.util.HashMap;

public class HashIndex {
    private HashMap<Integer, String> hm;

    protected HashIndex() {
        hm = new HashMap<>();
    }

    protected void add(int randomV, String fileAndRecordID) {
        String record = hm.get(randomV);
        if (record != null) {
            String fileId = fileAndRecordID.substring(0, 3);
            int index = record.indexOf(fileId);

            if (index >= 0) {
                record = record.substring(0, index + 3) + fileAndRecordID.substring(3) + "," + record.substring(index + 3);
            } else {
                record += ";" + fileAndRecordID;
            }

            hm.put(randomV, record);
        } else {
            hm.put(randomV, fileAndRecordID);
        }
    }

    public void read(int randomV) {
        String record = hm.get(randomV);
        if (record == null) {
            System.out.println("No records found for randomV of " + randomV + "\nNo I/Os performed");
        } else {
            Reader.printRecord(record);
        }
    }
}