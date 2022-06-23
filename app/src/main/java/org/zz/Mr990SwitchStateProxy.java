package org.zz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;


@IntDef({Mr990SwitchStateProxy.OPEN,
        Mr990SwitchStateProxy.CLOSE})
@Retention(RetentionPolicy.SOURCE) //表示注解所存活的时间,在运行时,而不会存在. class 文件.

public @interface Mr990SwitchStateProxy { //接口，定义新的注解类型

    /**
     * 1 开
     */
    int OPEN = 1;
    /**
     * 2：关
     */
    int CLOSE = 0;

}
