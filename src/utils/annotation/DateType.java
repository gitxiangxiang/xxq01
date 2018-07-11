package utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by  waiter on 18-7-9  下午3:08.
 *
 * @author waiter
 */

/**标注在实体类的日期型数据上
 * 实现格式转换
 * 由于Date有sql的和utils
 * 为了转化它们
 * 在实体类的日期属性上标注此注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DateType {


}
