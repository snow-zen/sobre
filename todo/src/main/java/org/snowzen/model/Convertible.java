package org.snowzen.model;

/**
 * 转换接口
 *
 * @author snow-zen
 */
public interface Convertible<T> {

    /**
     * 转换为指定类型
     *
     * @return 指定类型实例
     */
    T convert();

    /**
     * 以指定类型实例填充当前实例
     *
     * @param to 指定类型实例
     */
    void reverse(T to);
}
