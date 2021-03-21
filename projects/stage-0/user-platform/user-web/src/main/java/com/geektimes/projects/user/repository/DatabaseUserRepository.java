package com.geektimes.projects.user.repository;

import com.geektimes.function.ThrowableFunction;
import com.geektimes.projects.user.domain.User;
import com.geektimes.projects.user.sql.DBConnectionManager;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;

public class DatabaseUserRepository implements UserRepository{

    private  static Logger logger=Logger.getLogger(DatabaseUserRepository.class.getName());

    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER=e -> logger.log(Level.SEVERE,e.getMessage());

    public static final String INSERT_USER_DML_SQL="INSERT INTO users(name,password,email,phoneNumber) VALUES (?,?,?,?)";

    private final DBConnectionManager dbConnectionManager;

    public DatabaseUserRepository(DBConnectionManager dbConnectionManager){
        this.dbConnectionManager=dbConnectionManager;
    }

    private Connection getConnection(){
        return dbConnectionManager.getConnection();
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement preparedStatement=this.getConnection().prepareStatement(INSERT_USER_DML_SQL);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4,user.getPhoneNumber());
            int affected = preparedStatement.executeUpdate();
            return  affected==1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteById(Long userId) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    @Override
    public User getByNameAndPassword(String username, String password) {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users WHERE name=? and password=?",resultSet -> {
            return  new User();
        },COMMON_EXCEPTION_HANDLER,username,password);
    }

    @Override
    public Collection<User> getAll() {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users",resultSet -> {
            BeanInfo userBeanInfo= Introspector.getBeanInfo(User.class,Object.class);
            List<User> users=new ArrayList<>();
            while(resultSet.next()){
                User user=new User();
                for (PropertyDescriptor propertyDescriptor:userBeanInfo.getPropertyDescriptors()){
                    String fieldName=propertyDescriptor.getName();
                    Class fieldType=propertyDescriptor.getPropertyType();
                    String methodName=resultSetMethodMappings.get(fieldType);
                    String columnLabel=mapColumnLabel(fieldName);
                    Method resultSetMethod=ResultSet.class.getMethod(methodName,String.class);
                    Object resultValue=resultSetMethod.invoke(resultSet,columnLabel);

                    Method setterMethodFromUser=propertyDescriptor.getWriteMethod();
                    setterMethodFromUser.invoke(user,resultValue);
                }
            }
            return users;
        },e->{});
    }

    protected <T> T executeQuery(String sql, ThrowableFunction<ResultSet,T> function,Consumer<Throwable> exceptionHandler,Object... args){
        Connection connection=getConnection();
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                Object arg=args[i];
                Class argType=arg.getClass();
                Class wrapperType=wrapperToPrimitive(argType);
                if(wrapperType==null){
                    wrapperType=argType;
                }

                String methodName=preparedStatementMethodMappings.get(argType);
                Method method=PreparedStatement.class.getMethod(methodName,wrapperType);
                method.invoke(preparedStatement,i+1,args);
            }
            ResultSet resultSet=preparedStatement.executeQuery();
            return function.apply(resultSet);
        }catch (Throwable e){
            exceptionHandler.accept(e);
        }
        return null;
    }

    private static String mapColumnLabel(String fieldName){
        return  fieldName;
    }

    static Map<Class,String> resultSetMethodMappings=new HashMap<>();
    static Map<Class,String> preparedStatementMethodMappings=new HashMap<>();
    static {
        resultSetMethodMappings.put(Long.class, "getLong");
        resultSetMethodMappings.put(String.class, "getString");

        preparedStatementMethodMappings.put(Long.class, "setLong"); // long
        preparedStatementMethodMappings.put(String.class, "setString"); //

    }
}
