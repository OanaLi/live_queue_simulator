package model;

public class Task {

    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int waitingTime;

    public Task(int id, int timpSosire, int timpServire){
        this.id=id;
        this.arrivalTime =timpSosire;
        this.serviceTime =timpServire;
        waitingTime=0;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime){ this.serviceTime=serviceTime;}

    public String toString(){
        return "("+id+", "+ arrivalTime + ", "+ serviceTime +")";
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
}
