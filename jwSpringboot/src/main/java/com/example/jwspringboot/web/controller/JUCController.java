package com.example.jwspringboot.web.controller;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/juc")
public class JUCController {

    private int number1=0;
    private int number2=0;


    //asynchronous 异步
    @RequestMapping("/getAsynInfo")
    public String getAsynInfo() throws ExecutionException, InterruptedException {
        CompletableFuture<Response> future = new CompletableFuture<>();
        Request request = new Request();
        request.future = future;
        request.map = new HashMap();
        queue.add(request);
        return future.get().toString();
    }
    @RequestMapping("/getSyncInfo")
    public String getSyncInfo() throws ExecutionException, InterruptedException {
        return  lateMethod("Sync",number1++).toString();
    }



    private Response lateMethod(String name,int number) throws InterruptedException {
        Thread.sleep(100);

        Response result = new Response(name+number, number);
        return result;

    }


    LinkedBlockingQueue<Request> queue = new LinkedBlockingQueue();

    class Request {
        Map map;
        CompletableFuture<Response> future;
    }

    public class Response {
        public Response(String name, int age) {
            this.name = name;
            this.age = age;
        }

        String name;
        int age;

        @Override
        public String toString() {
            return "Response{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @PostConstruct
    public void doBusiness() {

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int size = queue.size();
                if (queue.size() == 0) {
                    return;
                }
                List<Request> requestList = new ArrayList();
                List<Map> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Request request = queue.poll();
                    list.add(request.map);
                    requestList.add(request);
                }
                int requestSize = requestList.size();
                for (int i = 0; i < requestSize; i++) {

                    try {
                        requestList.get(i).future.complete(lateMethod("asyc",number2++));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 100, 20, TimeUnit.MILLISECONDS);
    }


//    @Scheduled(fixedRate=20*1000)
    public void runBusiness(){

        int size = queue.size();
        if (queue.size() == 0) {
            return;
        }
        List<Request> requestList = new ArrayList();
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Request request = queue.poll();
            list.add(request.map);
            requestList.add(request);
        }
        int requestSize = requestList.size();
        for (int i = 0; i < requestSize; i++) {

            try {
                requestList.get(i).future.complete(lateMethod("asyc",number2++));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
