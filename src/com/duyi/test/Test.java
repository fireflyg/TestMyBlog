package com.duyi.test;

import com.duyi.test.dao.StudentDao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
//        //    需要代理的对象
//        HelloService helloService = new HelloServiceImpl();
//
//        //    方法反射处理回调
//        InvocationHandler handler = new DynamicProxy<>(helloService);
//
//        //通过 类加载器，接口类对象，反射回调，创建代理对象
//        HelloService proxyInstance = (HelloService)Proxy.newProxyInstance(
//                handler.getClass().getClassLoader(),
//                helloService.getClass().getInterfaces(),
//                handler);
//        proxyInstance.sayHello("ok");
//        StudentDao studentDao = StudentDao.class.newInstance();
//        InvocationHandler handler = new DynamicProxy<>(studentDao);
//        StudentDao studentDaoProxy = (StudentDao) Proxy.newProxyInstance(
//                handler.getClass().getClassLoader(),
//                studentDao.getClass().getInterfaces(),
//                handler);
//        studentDaoProxy.insertStudent();
    }
}

interface HelloService {
    void sayHello(String something);
}

class HelloServiceImpl implements HelloService {
    public void sayHello(String something) {
        System.out.println("hello, " + something);
    }
}

class DynamicProxy<T> implements InvocationHandler {

    //这里是泛型，通配型很强
    private T proxyObj;

    public DynamicProxy(T proxyObj) {
        this.proxyObj = proxyObj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("----------------------impl myself----------------------");
        return method.invoke(proxyObj, args);
    }
}


