package nro.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import nro.jdbc.DBService;

/**
 *
 *
 *
 */
public class Log {

    public static void log(String text) {
        System.out.println("\u001B[33m" + "LOG : " + text + "\u001B[0m");
    }

    public static void errorSaveSQL(String text) {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForDebug();) {
            ps = con.prepareStatement("insert history_debug"
                    + "(debug, time) "
                    + "values (?,?)");
            ps.setString(1, text);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
        }
        System.out.println("\u001B[33m" + "SQL LOG : " + text + "\u001B[0m");
    }

    public static void success(String text) {
        System.out.println("\u001B[36m" + text + "\u001B[0m");
    }

    public static void warning(String text) {
        System.err.println("warning : " + text);
    }

    public static void error(String text) {
        System.err.println("error : " + text);
    }

    public static void error(Class clazz, Exception ex, String logs) {
        System.err.println("error claz : " + clazz.getName() + ":" + logs);
        ex.printStackTrace();
    }

    public static void error(Class clazz, Exception ex) {
        System.err.println(clazz.getName() + ": " + ex.getMessage());
        ex.printStackTrace();
    }

}
