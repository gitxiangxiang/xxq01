package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by  waiter on 18-7-6.
 *
 * @author waiter
 */
public class JDBCUtils {
    private final static Properties props;

    // 只在JdbcUtils类被加载时执行一次！
    static {
        // 给props进行初始化，即加载db-config.properties文件到props对象中
        try {
            InputStream in = JDBCUtils.class.getClassLoader()
                    .getResourceAsStream("db-config.properties");
            props = new Properties();
            props.load(in);

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        // 加载驱动类
        try {
            Class.forName(props.getProperty("driverClassName"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 私有构造
     */
    private JDBCUtils(){}

    /**
     * 获取连接!
     */

    public static Connection getConnection() throws SQLException {
        // 得到Connection
        return DriverManager.getConnection(props.getProperty("url"),
                props.getProperty("username"),
                props.getProperty("password"));
    }

    public static Properties getProps() {
        return props;
    }
}
