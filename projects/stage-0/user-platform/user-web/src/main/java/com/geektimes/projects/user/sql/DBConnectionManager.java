package com.geektimes.projects.user.sql;

import com.geektimes.projects.user.context.ComponentContext;
import com.geektimes.projects.user.domain.User;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnectionManager {

    private final Logger logger = Logger.getLogger(DBConnectionManager.class.getName());

    private ComponentContext componentContext;

//    private Connection connection;
//
//    public Connection getConnection() {
//        return connection;
//    }
//
//    public void setConnection(Connection connection) {
//        this.connection = connection;
//    }
//
//    public  void releaseConnection(){
//        if(this.connection != null){
//            try{
//                this.connection.close();
//            }catch (SQLException ex){
//                throw new RuntimeException(ex.getCause());
//            }
//        }
//    }

//    public Connection getConnection() {
//        Context context = null;
//        Connection connection = null;
//        try {
//            context = new InitialContext();
//            Context envCtx = (Context) context.lookup("java:comp/env");
//            DataSource dataSource = (DataSource) envCtx.lookup("jdbc/UserPlatformDB");
//            connection = dataSource.getConnection();
//        } catch (NamingException | SQLException e) {
//            logger.log(Level.SEVERE,e.getMessage(),e);
//        }
//        if(connection!=null){
//            logger.log(Level.FINER,"获取JDNI数据库连接成功");
//        }
//        return connection;
//    }

    public Connection getConnection() {
//        DataSource dataSource = componentContext.getComponent("jdbc/UserPlatformDB");
        DataSource dataSource=ComponentContext.getInstance().getComponent("jdbc/UserPlatformDB");
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        if (connection != null) {
            logger.log(Level.FINER, "获取JNDI数据库连接成功");
        }
        return connection;

    }

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1,INCREMENT BY 1)," +
            "name VARCHAR(6) NOT NULL," +
            "password VARCHAR(64) NOT NULL," +
            "email VARCHAR(64) NOT NULL," +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) values" +
            "('A','******','a@gmail.com','1') , " +
            "('B','******','b@gmail.com','2') , " +
            "('C','******','c@gmail.com','3') , " +
            "('D','******','d@gmail.com','4') , " +
            "('E','******','e@gmail.com','5')";

    public static void main(String[] args) throws Exception {
        String databaseURL = "jdbc:derby:E:/db/user-platform;create=true";
        String driverName = "org.apache.derby.jdbc.EmbeddedDriver";

        Class.forName(driverName);
//        Driver driver = DriverManager.getDriver(databaseURL);
//        driver.connect(databaseURL,new Properties());

        Connection connection = DriverManager.getConnection(databaseURL);
        Statement statement = connection.createStatement();

//        System.out.println(statement.execute(DROP_USERS_TABLE_DDL_SQL));
//
//        System.out.println(statement.execute(CREATE_USERS_TABLE_DDL_SQL));
//
//        System.out.println(statement.execute(INSERT_USER_DML_SQL));

        ResultSet resultSet = statement.executeQuery("SELECT id,name,password,email,phoneNumber FROM users");

        //读取数据
        while (resultSet.next()) {
//            Long id= resultSet.getLong("id");
//            String name=resultSet.getString("name");
//            System.out.println(id + name);

            User user = new User();
            //按指定类型读取
//            user.setId(resultSet.getLong("id"));
//            user.setName(resultSet.getString("name"));
//            user.setEmail(resultSet.getString("email"));
//            user.setPhoneNumber(resultSet.getString("phoneNumber"));
//            user.setPassword(resultSet.getString("password"));
//            System.out.println(user);

            //类映射 ORM
            BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
            for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                String fieldName = propertyDescriptor.getName();
                Class fieldType = propertyDescriptor.getPropertyType();
                //获取方法名称
                String methodName = typeMethodMappings.get(fieldType);
                //ORM映射列
                String columnLabel = mapColumnLabel(fieldName);
                Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                Object returnValue = resultSetMethod.invoke(resultSet, columnLabel);
                Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                setterMethodFromUser.invoke(user, returnValue);
            }
            System.out.println(user);
        }

        //元信息
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        System.out.println("当前表的名称：" + resultSetMetaData.getTableName(1));
        System.out.println("当前表的列个数：" + resultSetMetaData.getColumnCount());
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            System.out.println("列名称：" + resultSetMetaData.getColumnLabel(i) + ",类型：" + resultSetMetaData.getColumnTypeName(i));
        }

        //生成sql
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            stringBuilder.append(" ").append(resultSetMetaData.getColumnLabel(i)).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(" FROM ").append(resultSetMetaData.getTableName(1));
        System.out.println(stringBuilder.toString());
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    static Map<Class, String> typeMethodMappings = new HashMap<Class, String>();

    static {
        typeMethodMappings.put(Long.class, "getLong");
        typeMethodMappings.put(String.class, "getString");
    }

}
