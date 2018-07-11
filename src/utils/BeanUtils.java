package utils;

import utils.annotation.DateType;
import utils.annotation.Column;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by  waiter on 18-7-9  上午8:29.
 *
 * @author waiter
 */
public class BeanUtils {

    private BeanUtils(){}
    /**
     * 把从数据库查询到的结果集中的一行数据注入到一个实体类对象中
     * @param cls 实体类的class字节码文件，如User.class
     * @param set 结果集，会取当前游标所在下一行数据进行注入
     * @return 返回实体类对应对象，出错或查询不到结果时为null
     * @throws SQLException 若set为空或set的游标已在末尾则抛出
     */
    public static Object rsToBean(Class cls , ResultSet set) throws SQLException {
        if(set==null||!set.next()){
            return null;
        }
        Field[] declaredFields = cls.getDeclaredFields();
        Object obj= null;
        try {
            obj = cls.newInstance();
            for(Field f:declaredFields)
            {
                String name = f.getName();
                //访问类中的私有属性
                f.setAccessible(true);

                DateType annotation = f.getAnnotation(DateType.class);
                Column annotation1 = f.getAnnotation(Column.class);
                if(annotation1!=null){
                    name=annotation1.name();
                }
                if (annotation != null) {

                    f.set(obj,new Date(set.getDate(name).getTime()));


                }else {

                    //调用类中指定属性的set方法赋值
                    f.set(obj, set.getObject(name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    /**
     * 把从数据库查询到的结果集中的一行数据注入到一个实体类对象链表中
     * @param cls 实体类的class字节码文件，如User.class
     * @param set 结果集
     * @return 返回实体类对应对象链表，出错或查询不到结果时为null
     * @throws SQLException 若set为空或set的游标已在末尾则抛出
     */
    public static LinkedList rsToBeanList(Class cls , ResultSet set) throws SQLException {
        LinkedList list = new LinkedList();
        Object o;
        do {
             o = rsToBean(cls, set);
             list.add(o);
        }while (o!=null);
        list.remove(null);
        return list;
    }



}
