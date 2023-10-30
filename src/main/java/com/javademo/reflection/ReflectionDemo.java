package com.javademo.reflection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionDemo {

    //声明一个可继承的自定义注解
    @Inherited //表示此注解可以被子类继承
    @Retention(RetentionPolicy.RUNTIME) //表示此注解可以被通过反射读取
    public @interface FatherInherited{
        String value() default "";
    }

    @Description("这是测试父类！")
    @FatherInherited("可继承注解！")
    public class TestFather{
        public Float price;

        @Value("HUAWEI")
        private String brandName;

        protected Float sumPrice;

        public TestFather(){

        }

        public void testPublic(){

        }

        private void testPrivate(){

        }
    }

    @Description("这是测试类！")
    public class TestReflection extends TestFather{

        public Float store;

        @Value("11")
        private Number number;

        protected String name;

        public TestReflection(){
            super();
        }

        public void testPublic(){

        }

        private void testPrivate(String name, Number number){
            this.name = name;
            this.number = number;
            System.out.println("输出name:"+name+",number:"+number.toString());
        }
    }

    //获取类的class信息
    private void classInfo() throws NoSuchFieldException {
        TestReflection testReflection = new TestReflection();
        //获取类的class对象
        Class testReflectionClass = testReflection.getClass();
        //class对象常见的方法:
        //获取类对象所属类的名称
        String className = testReflectionClass.getName();
        System.out.println("类对象所属类的名称:"+className);

        //获取类对象的变量对象
        String fieldClassName = testReflectionClass.getDeclaredField("number").getType().getName();
        System.out.println("类对象的成员变量number所属类的名称:"+fieldClassName);

        //获取类对象以及其父类的public成员变量数组
        String fieldArrayString = Arrays.toString(testReflectionClass.getFields());
        System.out.println("获取类对象以及其父类的public成员变量数组:"+fieldArrayString);

        //获取类对象以及其父类的所有成员变量数组
        String declaredFieldArrayString = Arrays.toString(testReflectionClass.getDeclaredFields());
        System.out.println("获取类对象以及其父类的所有成员变量数组:"+declaredFieldArrayString);

        //类对象以及其父类的所有public方法
        String methodArrayString = Arrays.toString(testReflectionClass.getMethods());
        System.out.println("类对象以及其父类的所有public方法:"+methodArrayString);

        //类对象以及其父类的所有方法
        String declaredMethodArrayString = Arrays.toString(testReflectionClass.getDeclaredMethods());
        System.out.println("类对象以及其父类的所有方法:"+declaredMethodArrayString);

        //类对象的所有注解(包含父类可继承注解),注意不包括成员变量的注解
        String annotationArrayString = Arrays.toString(testReflectionClass.getAnnotations());
        System.out.println("类对象的所有注解(包含继承):"+annotationArrayString);

        //类对象的所有注解(不包含继承),注意不包括成员变量的注解
        String declaredAnnotationArrayString = Arrays.toString(testReflectionClass.getDeclaredAnnotations());
        System.out.println("类对象的所有注解(不包含继承):"+declaredAnnotationArrayString);
    }

    //利用反射对类对象的成员变量进行操作
    private void fieldAction() throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        //获取类的class对象
        Class testReflectionClass = Class.forName("com.javademo.reflection.ReflectionDemo$TestReflection");
        //获取Field对象
        Field field = testReflectionClass.getDeclaredField("name");
        //给Field对象赋值
        TestReflection testReflection = new TestReflection();
        field.setAccessible(true); //当成员变量为私有变量时需设置权限为true,public变量不需要
        field.set(testReflection, "李小龙");
        //输出fild的赋值结果
        String name = field.get(testReflection).toString();
        System.out.println("Field赋值后的结果：" + name);
    }

    //利用反射调用类对象的方法
    private void methodAction() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //获取类的class对象
        Class testReflectionClass = Class.forName("com.javademo.reflection.ReflectionDemo$TestReflection");
        //获取method对象
        Method method = testReflectionClass.getDeclaredMethod("testPrivate", String.class, Number.class);
        //私有方法需设置权限
        method.setAccessible(true);
        //通过invoke方法调用方法传参
        TestReflection testReflection = new TestReflection();
        method.invoke(testReflection, "李小龙", 3);
    }

    public static void main(String[] args) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        ReflectionDemo reflectionDemo = new ReflectionDemo();
        reflectionDemo.classInfo();
        reflectionDemo.fieldAction();
        reflectionDemo.methodAction();

        //输出内容
//        类对象所属类的名称:com.javademo.reflection.ReflectionDemo$TestReflection
//        类对象的成员变量number所属类的名称:java.lang.Number
//        获取类对象以及其父类的public成员变量数组:[public java.lang.Float com.javademo.reflection.ReflectionDemo$TestReflection.store, public java.lang.Float com.javademo.reflection.ReflectionDemo$TestFather.price]
//        获取类对象以及其父类的所有成员变量数组:[public java.lang.Float com.javademo.reflection.ReflectionDemo$TestReflection.store, private java.lang.Number com.javademo.reflection.ReflectionDemo$TestReflection.number, protected java.lang.String com.javademo.reflection.ReflectionDemo$TestReflection.name, final com.javademo.reflection.ReflectionDemo com.javademo.reflection.ReflectionDemo$TestReflection.this$0]
//        类对象以及其父类的所有public方法:[public void com.javademo.reflection.ReflectionDemo$TestReflection.testPublic(), public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException, public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException, public final void java.lang.Object.wait() throws java.lang.InterruptedException, public boolean java.lang.Object.equals(java.lang.Object), public java.lang.String java.lang.Object.toString(), public native int java.lang.Object.hashCode(), public final native java.lang.Class java.lang.Object.getClass(), public final native void java.lang.Object.notify(), public final native void java.lang.Object.notifyAll()]
//        类对象以及其父类的所有方法:[private void com.javademo.reflection.ReflectionDemo$TestReflection.testPrivate(java.lang.String,java.lang.Number), public void com.javademo.reflection.ReflectionDemo$TestReflection.testPublic()]
//        类对象的所有注解(包含继承):[@com.javademo.reflection.ReflectionDemo$FatherInherited(value=可继承注解！), @org.springframework.context.annotation.Description(value=这是测试类！)]
//        类对象的所有注解(不包含继承):[@org.springframework.context.annotation.Description(value=这是测试类！)]
//        Field赋值后的结果：李小龙
//        输出name:李小龙,number:3
    }
}
