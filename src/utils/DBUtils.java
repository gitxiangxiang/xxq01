package utils;

import utils.annotation.Column;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by  waiter on 18-7-6  14.
 *
 * @author waiter
 */
public class DBUtils {
    private static final Logger loger = Logger.getLogger(JDBCPool.class.getName());

    /**
     * 私有构造，禁止实例化
     */
    private DBUtils(){}
    /**
     * 执行sql，
     *
     * @param sql
     * @param params 可选的可变参数，可以省略，若有则按顺序加入到sql中的？占位符
     * @return 成功返回true，失败返回false
     */
    public static boolean executeSQL(String sql, String... params) {
        boolean flag = false;
        Connection connection = JDBCPool.getConnection();
        PreparedStatement statement = null;
        try {

            statement = connection.prepareStatement(sql);
            for (int i = 0, n = params.length; i < n; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.execute();
            flag = true;
        } catch (Exception e) {
            loger.log(Level.ALL,"sql出错");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                DBUtils.close(connection,statement,null);
            }
        }
        return flag;
    }

    /**
     * 执行查询sql
     * @param sql
     * @param params 可选的可变参数，可以省略，若有则按顺序加入到sql中的？占位符
     * @return 返回结果集
     */
    public static ResultSet executeQuerySQL(String sql, String... params){
        Connection connection =  JDBCPool.getConnection();
        PreparedStatement statement = null;
        ResultSet set=null;

        try {
            statement = connection.prepareStatement(sql);
            for (int i = 0, n = params.length; i < n; i++) {
                statement.setString(i + 1, params[i]);
            }
            set=statement.executeQuery();

        } catch (Exception e) {
            loger.log(Level.ALL,"sql出错");
            e.printStackTrace();
        } finally {
                DBUtils.close(connection,null,null);
        }
        return set;
    }

    /**
     * 通过实体类的字节码文件获取对应的数据库表中的数据
     * @param cls 字节码文件，例如User.class
     * @return 对应的实体类的 LinkList
     */
    public static LinkedList getList(Class cls)
    {
        LinkedList list=null;
        ResultSet rs=null;
        //要求:数据表中的字段必须与类中的属性一一对应
        //获取类名(类名必须与数据表一致)
        String sql="select * from "+cls.getSimpleName();
        try{
            rs=executeQuerySQL(sql);
            list = BeanUtils.rsToBeanList(cls, rs);
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally{
            DBUtils.close(null, null,rs);
        }
        return list;
    }




    /**
     * 根据数据表中的主键获取到指定数据表对应的一条记录(对应一个对象)
     * @param cl 实体类字节码文件
     * @param id id主键值
     * @return  返回实体类对象
     */

    public static Object getObjectById(Class cl,int id)
    {
        ResultSet rs=null;
        Object obj=null;
        //获取类中的所有的属性
        Field[] fi=cl.getDeclaredFields();
        String name = fi[0].getName();
        Column annotation = fi[0].getAnnotation(Column.class);
        if(annotation!=null){
            name=annotation.name();
        }
        //fi[0].getName()获取数据表中第一列字段
        String sql="select * from "+cl.getSimpleName()+" where "+name+" = ?";
        try{
            rs=executeQuerySQL(sql,String.valueOf(id));
            obj = BeanUtils.rsToBean(cl,rs);
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally{
            DBUtils.close(null, null,rs);
        }
        return obj;
    }

    /**
     * 根据数据表中的一个字段和对应的值查询
     * @param cl 实体类字节码文件
     * @param name 字段名
     * @param value 字段值
     * @return 返回符合条件的实体类对象链表
     */
    public static LinkedList getListBySome(Class cl,String name,String value)
    {


        LinkedList list=null;
        ResultSet rs=null;
        //获取类中的所有的属性
        Field[] fi=cl.getDeclaredFields();

        String sql="select * from "+cl.getSimpleName()+" where "+name+" = ?";
        try{
            rs=executeQuerySQL(sql,value);
            list=BeanUtils.rsToBeanList(cl,rs);
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally{
            DBUtils.close(null, null,rs);
        }
        return list;
    }


    /**
     * 插入一行数据
     * @param obj 对应的实体类对象 该实体类主键id应为自增，并且定义为第一个属性
     * @return true或者false，对应成功或失败
     */
    public static boolean insert(Object obj)
    {
        boolean flag=false;
        Connection conn=JDBCPool.getConnection();
        PreparedStatement pstmt=null;
        Class clz=obj.getClass();
        //获取类中的所有的属性
        Field[] fi=clz.getDeclaredFields();
        StringBuilder sb=new StringBuilder();
        sb.append("insert into ");
        sb.append(clz.getSimpleName());
        sb.append("(");
        //主键为第一列，是自增的，有默认值，不需要添加
        for(int i=1;i<fi.length;i++)
        {
            //获取列名
            String name = fi[i].getName();
            Column annotation = fi[0].getAnnotation(Column.class);
            if(annotation!=null){
                name=annotation.name();
            }
            sb.append(name);
            //最后一列不用加逗号
            if(i!=fi.length-1) {
                sb.append(",");
            }
        }
        sb.append(") values(");
        for(int i=1;i<fi.length;i++)
        {
            sb.append("?");
            //最后一列不用加逗号
            if(i!=fi.length-1) {
                sb.append(",");
            }
        }
        sb.append(")");
        String sql=sb.toString();
        System.out.println(sql);
        try {
            pstmt=conn.prepareStatement(sql);
            for(int i=1;i<fi.length;i++)
            {
                fi[i].setAccessible(true);
                //第一列为主键，不用添加(对应数组下标是0)
                pstmt.setObject(i,fi[i].get(obj));
            }
            int row=pstmt.executeUpdate();
            if(row>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.close(conn, pstmt,null);
        }
        return flag;
    }
    /**
     * 万能的更新方法
     *  @param obj 对应的实体类对象
     */
    public static boolean update(Object obj){
        boolean flag=false;
        Connection conn=JDBCPool.getConnection();
        PreparedStatement pstmt=null;
        Class clz=obj.getClass();
        //获取类中的所有的属性
        Field[] fi=clz.getDeclaredFields();
        StringBuilder sb=new StringBuilder();
        sb.append("update ");
        sb.append(clz.getSimpleName());
        sb.append(" set ");
        for(int i=1;i<fi.length;i++)
        {
            //获取列名
            String name = fi[i].getName();
            Column annotation = fi[0].getAnnotation(Column.class);
            if(annotation!=null){
                name=annotation.name();
            }
            sb.append(name);
            sb.append(" =? ");
            //最后一列不用加逗号
            if(i!=fi.length-1) {
                sb.append(",");
            }
        }
        sb.append(" where ");
        sb.append(fi[0].getName());
        sb.append(" =? ");
        try{
            pstmt=conn.prepareStatement(sb.toString());
            for(int i=1;i<fi.length;i++)
            {
                fi[i].setAccessible(true);
                pstmt.setObject(i,fi[i].get(obj));
            }
            fi[0].setAccessible(true);
            //主键是第一列
            pstmt.setObject(fi.length, fi[0].get(obj));
            int row=pstmt.executeUpdate();
            if(row>0){
                flag=true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.close(conn, pstmt,null);
        }
        return flag;
    }

    /**
     * 万能的删除方法，通过id删除数据
     * @param clz 实体类字节码文件
     * @param id id
     * @return
     */
    public static boolean  delete(Class clz,int id)
    {
        boolean flag=false;
        Connection conn=JDBCPool.getConnection();
        PreparedStatement pstmt=null;
        //获取类中的所有的属性
        Field[] fi=clz.getDeclaredFields();
        String sql="delete from "+clz.getSimpleName()+ " where "+fi[0].getName()+"=?";
        System.out.println(sql);
        try{
            pstmt=conn.prepareStatement(sql);
            pstmt.setObject(1, id);
            int row=pstmt.executeUpdate();
            if(row>0){
                flag=true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.close(conn, pstmt,null);
        }
        return flag;
    }

    private static void close(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet){
        try {

            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }catch (Exception e){
            loger.warning("关闭连接异常");
            e.printStackTrace();
        }


        if(connection!=null) {
            JDBCPool.close(connection);
        }
    }

    /**
     * 统计总行数
     * @param cls 实体类字节码文件
     * @return 总数
     */
    public static int getObjectCount(Class cls) {
        PreparedStatement preparedStatement = null;
        Connection connection = JDBCPool.getConnection();
        ResultSet rs=null;
        String sql = "select count(*) from " + cls.getSimpleName();
        try {
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            rs.next();
            int i =rs.getInt(1);
            return i;
        }catch (Exception e){
            loger.info(e.getMessage());
            e.printStackTrace();
            return 0;
        }finally {
            close(connection,preparedStatement,rs);

        }
    }

    /**
     * 分页查询功能
     * @param pageBean 工具类中的PageBean对象
     * @param cls 实体类字节码文件
     * @return PageBean对象
     */
    public static PageBean getPage(PageBean pageBean,Class cls){
        pageBean.setTotalCount(getObjectCount(cls));
        pageBean.getPageData().clear();
        ResultSet rs=null;
        String sql = "select * from " + cls.getSimpleName()+" LIMIT "+(pageBean.getCurrentPage()-1)*pageBean.getPageCount()+","+pageBean.getPageCount();
        try {
            rs=executeQuerySQL(sql);
            pageBean.getPageData().addAll(BeanUtils.rsToBeanList(cls,rs));
        }catch (Exception e){
            loger.info(e.getMessage());
            e.printStackTrace();
        }finally {
            close(null,null,rs);

        }

        return pageBean;
    }

    /**
     * 分页查询功能，通过某一字段的值筛选分页
     * @param pageBean 工具类中的PageBean对象
     * @param cls 实体类字节码文件
     * @param name
     * @param value
     * @return
     */
    public static PageBean getPageBySome(PageBean pageBean,Class cls,String name,String value){
        Map<String,String> map = new TreeMap<>();
        map.put(name, value);
        pageBean.setTotalCount(getObjectCount(cls,map));
        pageBean.getPageData().clear();
        ResultSet set=null;
        String sql="select * from "+cls.getSimpleName()+" where "+name+" = ?"+" LIMIT "+(pageBean.getCurrentPage()-1)*pageBean.getPageCount()+","+pageBean.getPageCount();;
        try {
            set=executeQuerySQL(sql,value);
            pageBean.getPageData().addAll(BeanUtils.rsToBeanList(cls,set));
        }catch (Exception e){
            loger.info(e.getMessage());
            e.printStackTrace();
        }finally {
            close(null,null,set);

        }

        return pageBean;
    }

    /**
     * 分页查询功能加强版，实现多条件筛选
     * @param pageBean
     * @param cls
     * @param map map参数中key对应字段名，value对应字段值
     * @return
     */
    public static PageBean getPageBySome(PageBean pageBean,Class cls,Map<String,String> map){
        pageBean.setTotalCount(getObjectCount(cls,map));
        pageBean.getPageData().clear();
        ResultSet set=null;
        String sql="select * from "+cls.getSimpleName()+" where ";

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sql+=entry.getKey()+" = "+entry.getValue()+" AND ";
        }
        String substring = sql.substring(0, sql.length() - 4);
        substring+=" LIMIT "+(pageBean.getCurrentPage()-1)*pageBean.getPageCount()+","+pageBean.getPageCount();

        try {
            set=executeQuerySQL(substring);
            pageBean.getPageData().addAll(BeanUtils.rsToBeanList(cls,set));
        }catch (Exception e){
            loger.info(e.getMessage());
            e.printStackTrace();
        }finally {
            close(null,null,set);

        }

        return pageBean;
    }

    /**
     * getList加强版，可实现多个查询条件
     * @param cl
     * @param map map参数中key对应字段名，value对应字段值
     * @return
     */
    public static LinkedList getListBySome(Class cl,Map<String,String> map)
    {
        LinkedList list=null;
        ResultSet rs=null;


        String sql="select * from "+cl.getSimpleName()+" where ";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sql+=entry.getKey()+" = "+entry.getValue()+" AND ";
        }
        String substring = sql.substring(0, sql.length() - 4);
        try{
            rs=executeQuerySQL(substring);
            list=BeanUtils.rsToBeanList(cl,rs);
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally{
            DBUtils.close(null, null,rs);
        }
        return list;
    }
    public static int getObjectCount(Class cls,Map<String,String> map) {
        PreparedStatement preparedStatement = null;
        Connection connection = JDBCPool.getConnection();
        ResultSet rs=null;
        String sql = "select count(*) from " + cls.getSimpleName()+" WHERE ";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sql+=entry.getKey()+" = "+entry.getValue()+" AND ";
        }
        String substring = sql.substring(0, sql.length() - 4);
        try {
            preparedStatement = connection.prepareStatement(substring);
            rs = preparedStatement.executeQuery();
            rs.next();
            int i =rs.getInt(1);
            return i;
        }catch (Exception e){
            loger.info(e.getMessage());
            e.printStackTrace();
            return 0;
        }finally {
            close(connection,preparedStatement,rs);

        }
    }
}























