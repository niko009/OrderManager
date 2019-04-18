package ordermanager.niko.com.database;

public class OrderDbSchema {


    public static final class OrderTable {
        public static final String NAME = "orders";


    public static class Cols {
        public static final String UUID = "uuid";
        public static final String NAME = "name";
        public static final String START_TIME = "start_time";
        public static final String DESCRIPTION = "description";
        public static final String COST = "cost";
    }
}
}
