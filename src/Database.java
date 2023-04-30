import java.util.Iterator;

public class Database implements Iterable<String> {
    private HashIndex hashIndex;
    private Index arrayIndex;

    public void createIndexes() {
        hashIndex = new HashIndex();
        arrayIndex = new Index();

        this.forEach(r -> {
            int randomV = Integer.parseInt(r.substring(33, 37));
            String fileAndRecordID = r.substring(1, 3) + ":" + r.substring(7, 10);

            hashIndex.add(randomV, fileAndRecordID);
            arrayIndex.add(randomV, fileAndRecordID);
        });

        System.out.println("The hash-based and array-based indexes are built successfully.\nProgram is ready and waiting for user command");
    }

    protected void search(int searchType, int randomV1, int randomV2) {
        if (hashIndex == null) {
            searchType += 3;
        }

        long start = System.currentTimeMillis();

        Runnable searchRunnable = () -> {
        };

        switch (searchType) {
            case 0:
                searchRunnable = () -> equalitySearch(randomV1);
                break;
            case 1:
                searchRunnable = () -> rangeSearch(randomV1, randomV2);
                break;
            case 2:
            case 5:
                searchRunnable = () -> inequalityTableScan(randomV1);
                break;
            case 3:
                searchRunnable = () -> equalityTableScan(randomV1);
                break;
            case 4:
                searchRunnable = () -> rangeTableScan(randomV1, randomV2);
                break;
        }

        searchRunnable.run();

        long totalTime = System.currentTimeMillis() - start;
        System.out.println("Search time: " + totalTime + "ms");
    }

    public void equalitySearch(int randomV) {
        hashIndex.read(randomV);
        System.out.println("Used hash-based indexing");
    }

    public void rangeSearch(int greaterThan, int lessThan) {
        arrayIndex.readRange(greaterThan, lessThan);
        System.out.println("Used array-based indexing");
    }

    public void tableScan(int searchType, int randomV1, int randomV2) {
        int filesRead = 0;
        for (String r : this) {
            int value = Integer.parseInt(r.substring(33, 37));

            switch (searchType) {
                case 0:
                    if (value == randomV1) {
                        System.out.println(r);
                    }
                    break;
                case 1:
                    if (value > randomV1 && value < randomV2) {
                        System.out.println(r);
                    }
                    break;
                case 2:
                    if (value != randomV1) {
                        System.out.println(r);
                    }
                    break;
            }
            filesRead++;
        }
        System.out.println("Full table scan completed (no index available)");
        System.out.println(filesRead + " files read");
    }

    public void equalityTableScan(int randomV) {
        tableScan(0, randomV, 0);
    }

    public void rangeTableScan(int greaterThan, int lessThan) {
        tableScan(1, greaterThan, lessThan);
    }

    public void inequalityTableScan(int randomV) {
        tableScan(2, randomV, 0);
    }

    @Override
    public Iterator<String> iterator() {
        return new Reader();
    }
}