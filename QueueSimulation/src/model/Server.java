package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server(){
        tasks=new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger();
    }

    public void addTask(Task newTask){
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    public AtomicInteger getWaitingPeriod(){
        return waitingPeriod;
    }

    public int getSizeServer(){
        return tasks.size();
    }


    public Task[] getTasks(){
        Task[] listaTaskuri=new Task[tasks.size()];
        int i=0;
        for(Task t: tasks) {
            listaTaskuri[i]=t;
            i++;
        }
        return listaTaskuri;
    }

    @Override
    public void run() {
        while(true) {
            Task servedClient = tasks.peek();
            if (servedClient != null) {
                servedClient.setServiceTime(servedClient.getServiceTime() - 1);
                waitingPeriod.decrementAndGet();
                if (servedClient.getServiceTime() == 0) {
                    tasks.poll();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Eroare la thread");
            }

        }
    }

}