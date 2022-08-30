package com.example.jwspringboot;

import com.example.jwspringboot.web.controller.JUCController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class JwspringbootApplicationTests {

    @Autowired
    private JUCController controller;

    @Test
    void contextLoads() {
    }


    @Test
    public void test1() throws InterruptedException {


        long time1=  System.currentTimeMillis();
        List<String> list=new ArrayList<>();
        CountDownLatch latch=new CountDownLatch(10000);
        for (int i = 0; i <10000 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        list.add(controller.getAsynInfo());
                        latch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        latch.await();

        long time2=  System.currentTimeMillis();

        System.out.println(time2-time1);
        System.out.println(list.get(list.size()-1));
    }
    @Test
    public void test2() throws InterruptedException {


        long time1=  System.currentTimeMillis();
        CountDownLatch latch=new CountDownLatch(100);
        List<String> list=new ArrayList<>();
        for (int i = 0; i <10000 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        list.add(controller.getSyncInfo());
                        latch.countDown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        latch.await();

        long time2=  System.currentTimeMillis();

        System.out.println(time2-time1);
        System.out.println(list.get(list.size()-1));
    }







}
