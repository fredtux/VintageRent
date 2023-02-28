package org.logger;

import org.database.DatabaseConnection;
import org.database.csv.CsvConnection;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CsvLogger extends Logger {
    private static final String LOGGER_FILE = "Log.csv";
    private static CsvLogger instance = null;
    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    public CsvLogger(String name, String resourceBundleName) throws RuntimeException {
        super(name, resourceBundleName);

        if(instance != null)
            throw new RuntimeException("CsvLogger is a singleton class. Use getInstance() instead.");

        instance = this;

        try{
            new CsvConnection();
        } catch (Exception e){

        }

    }

    public static CsvLogger getInstance(){
        if(instance == null)
            instance = new CsvLogger("CsvLogger", null);

        return instance;
    }

    public void log(String message) throws Exception{
        List<String[]> toWrite = new ArrayList<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        toWrite.add(new String[]{message, sdf.format(timestamp)});
        ((CsvConnection) CsvConnection.getInstance(DatabaseConnection.DatabaseType.CSV)).insertNoLog(LOGGER_FILE, null, toWrite);
    }


}
