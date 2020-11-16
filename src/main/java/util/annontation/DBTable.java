package util.annontation;

import java.lang.annotation.*;

/**
 *      标注表的名称
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented

public @interface DBTable {
    String tableName();
}
