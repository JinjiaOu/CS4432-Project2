public class Index {

    private String[] array;

    protected Index() {
        array = new String[5000];
    }

    protected void add(int randomV, String fileAndRecordID) {
        randomV -= 1;
        String fileIdSubstring = fileAndRecordID.substring(0, 3);

        if (array[randomV] == null) {
            array[randomV] = fileAndRecordID;
        } else if (array[randomV].contains(fileIdSubstring)) {
            int index = array[randomV].indexOf(fileIdSubstring);
            array[randomV] = new StringBuilder(array[randomV])
                    .replace(index, index + 3, array[randomV].substring(0, index + 3) + fileAndRecordID.substring(3) + ", ")
                    .append(array[randomV].substring(index + 3))
                    .toString();
        } else {
            array[randomV] = array[randomV] + ";" + fileAndRecordID;
        }
    }

    public void readRange(int greaterThan, int lessThan) {
        if (greaterThan < 0 || lessThan < 0 || greaterThan > 5000) {
            System.out.println("Invalid range, no records found.");
            return;
        }

        lessThan = Math.min(lessThan, 5000);
        StringBuilder fileRecord = new StringBuilder();

        for (int i = greaterThan; i < lessThan - 1; i++) {
            if (array[i] != null) {
                String[] files = array[i].split(";");
                for (String s : files) {
                    String fileIdSubstring = s.substring(0, 3);
                    int fileIndex = fileRecord.indexOf(fileIdSubstring);
                    if (fileIndex >= 0) {
                        fileRecord.replace(fileIndex, fileIndex + 3, fileRecord.substring(0, fileIndex + 3) + s.substring(3) + ",");
                    } else {
                        if (fileRecord.length() > 0) {
                            fileRecord.append(";");
                        }
                        fileRecord.append(s);
                    }
                }
            }
        }

        if (fileRecord.length() < 1) {
            System.out.println("No records found");
        } else {
            Reader.printRecord(fileRecord.toString());
        }
    }
}