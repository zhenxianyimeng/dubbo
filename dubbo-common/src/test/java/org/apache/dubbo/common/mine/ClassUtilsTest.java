package org.apache.dubbo.common.mine;

import org.apache.dubbo.common.utils.ClassUtils;

import java.util.Date;

/**
 * Description:
 *
 * @author: zhenxianyimeng
 * @date: 2019-07-08
 * @time: 16:01
 */
public class ClassUtilsTest {
    public static void main(String[] args) {
        System.out.println(ClassUtils.isPrimitive(Date.class));
    }
}
