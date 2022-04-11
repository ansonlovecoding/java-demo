package com.javademo.thread;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

//CAS类
public class CasCounter {
    private static final Unsafe unsafe;
    private static final long valueOffset;
    private volatile int value;

    static {
        try {
            // 获取 Unsafe 内部的私有的实例化单例对象
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 无视权限
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            valueOffset = unsafe.objectFieldOffset
                    (CasCounter.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    public CasCounter(int initialValue) {
        value = initialValue;
    }

    public CasCounter() {
    }

    public final int get() {
        return value;
    }

    public final void set(int newValue) {
        value = newValue;
    }

    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }
}
