package com.bsw.mydemo;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Blog {
    @Test
    public void create() {
        Subject subject = new SubjectImpl();
        InvocationHandler subjectProxy = new SubjectProxy(subject);
        Subject proxyInstance = (Subject) Proxy.newProxyInstance(subjectProxy.getClass().getClassLoader(), subject.getClass().getInterfaces(), subjectProxy);
        proxyInstance.hello("world");
    }
}

interface Subject {
    void hello(String param);
}

class SubjectImpl implements Subject {
    @Override
    public void hello(String param) {
        System.out.println("hello  " + param);
    }
}

class SubjectProxy implements InvocationHandler {
    private Subject subject;

    public SubjectProxy(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("--------------begin-------------");
        Object invoke = method.invoke(subject, args);
        System.out.println("--------------end-------------");
        return invoke;
    }
}