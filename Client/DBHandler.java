/*
 * The singleton database handler.
 */
public class DBHandler extends SQLiteOpenHelper {

    /*
     * The SQL schema's file name (located in the /assets directory).
     */
    private static final String SCHEMA_FILE = "db.sql";

    /*
     * The file specifying the operations to be performed in case that the database needs to be
     * deleted. (located in the /assets directory).
     */
    private static final String DROP_SCHEMA_FILE = "sql_schema_drop.sql";

    /*
     * The database's name.
     */
    public static final String DATABASE_FILE = "ArDb.db";

    /*
     * The current database's version.
     */
    public static final int DATABASE_VERSION = 5;

    /*
     * The handler's singleton instance.
     */
    private static DBHandler instance = null;

    /*
     * Returns the handler's singleton instance.
     */
    public static DBHandler getInstance() {
        if (DBHandler.instance == null) {
            DBHandler.instance = new DBHandler(AudienceResponse.getAppContext());
        }
        return DBHandler.instance;
    }

    /*
     * Creates a new ArDbHelper instance.
     */
    private DBHandler(Context context) {
        super(context, DATABASE_FILE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Open the schema file
        AssetManager assetHandler = AudienceResponse.getAppContext().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetHandler.open(DBHandler.SCHEMA_FILE);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Read and store the schema file
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = inputReader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Split the SQL schema at semicolons to assure execSQL works properly.
        String[] tables = stringBuilder.toString().split(";");
        for (int i = 0; i < tables.length; i++) {
            db.execSQL(tables[i]);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // TODO: Add a deletion statement, here.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Open the deletion file
        AssetManager assetHandler = AudienceResponse.getAppContext().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetHandler.open(DBHandler.DROP_SCHEMA_FILE);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Read and store the deletion file contents
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = inputReader.readLine()) != null) {
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Split the SQL schema at semicolons to assure execSQL works properly.
        String[] tables = stringBuilder.toString().split(";");
        for (int i = 0; i < tables.length; i++) {
            db.execSQL(tables[i]);
        }
        onCreate(db);
    }
}