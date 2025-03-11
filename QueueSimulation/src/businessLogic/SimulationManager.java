package businessLogic;

import gui.SimulationFrame;
import model.Server;
import model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static businessLogic.SelectionPolicy.*;

public class SimulationManager implements Runnable{

    private Scheduler scheduler;
    private List<Task> list;
    private SelectionPolicy selectionPolicy;
    private final int nrClients, nrQueues, simulationTime;
    private int minArrivalTime, maxArrivalTime;
    private int minServiceTime, maxServiceTime;
    private float averageWaitingTime, averageServiceTime;
    private int peakHour;
    private SimulationFrame sf;
    private FileWriter fileWriter;
    private boolean reset=false;


    public SimulationManager(SimulationFrame sf, SelectionPolicy selectionPolicy, Integer nrClients, Integer nrQueues, Integer simulationTime,
                             Integer minArrivalTime, Integer maxArrivalTime, Integer minServiceTime,
                             Integer maxServiceTime){

        averageWaitingTime=0f;
        averageServiceTime=0f;
        peakHour=0;

        this.sf=sf;
        this.nrClients=nrClients;
        this.nrQueues=nrQueues;
        this.simulationTime=simulationTime;
        this.minArrivalTime=minArrivalTime;
        this.maxArrivalTime=maxArrivalTime;
        this.minServiceTime=minServiceTime;
        this.maxServiceTime=maxServiceTime;
        this.selectionPolicy= selectionPolicy;

        list=new LinkedList<>();
        generateRandomTasks(); //generare clienti in list<task>
        scheduler=new Scheduler(nrQueues, selectionPolicy); //poorneste threadurile de la queue
    }

    public void generateRandomTasks(){
        Random rand = new Random();

        for(int i=0; i<nrClients; i++) {
            int arrivalTime = rand.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = rand.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            list.add(new Task(i+1, arrivalTime, serviceTime));
        }
        sortTasksByArrivalTime();
    }

    public void sortTasksByArrivalTime() {
        list.sort(new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Integer.compare(t1.getArrivalTime(), t2.getArrivalTime());
            }
        });
    }

    public String printareWaitingClients(){
        String text="";
        for(Task t: list)
            text=text+t + " ";

        return text+"\n";
    }

    public String printFinalData(){
        String text="";
        text=text + String.format("\nAverage waiting time: %.2f\n", averageWaitingTime);
        text=text + String.format("Average service time: %.2f\n", averageServiceTime);
        text=text+"Peak hour: " + peakHour+"\n";

        return text;
    }


    @Override
    public void run() {
        int timp = 0;
        int clientsInQueues=0;

        try {
            File file = new File("log.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(timp<=simulationTime && reset==false){

            Iterator<Task> iterator = list.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getArrivalTime() == timp) {
                    scheduler.dispatchTask(task); //se ia taskul generat si se adauga intr-o coada
                    averageWaitingTime += task.getWaitingTime();
                    averageServiceTime += task.getServiceTime();
                    iterator.remove();
                }
            }

            //afisare in consola
            System.out.print("\nTime "+timp+"\n");
            System.out.print("Waiting clients: ");
            System.out.print(printareWaitingClients());
            System.out.print(scheduler.printareQueues());

            //afisare in GUI
            sf.updateLiveGUI(timp, printareWaitingClients(), scheduler.printareQueues());

            //afisare in fisier
            try{
                fileWriter.write("\nTime "+timp+"\n");
                fileWriter.write(printareWaitingClients());
                fileWriter.write(scheduler.printareQueues());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(clientsInQueues < scheduler.clientsInQueues()) {
                clientsInQueues = scheduler.clientsInQueues();
                peakHour = timp;
            }

            if(list.isEmpty() && scheduler.clientsInQueues()==0)
                break;

            timp++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        if(reset==false) {
            averageWaitingTime = averageWaitingTime / nrClients;
            averageServiceTime = averageServiceTime / nrClients;

            //afisare in consola
            System.out.println(printFinalData());

            //afisare in frame
            sf.updateResultsGUI(String.format("%.2f", averageWaitingTime),
                    String.format("%.2f", averageServiceTime), peakHour);
            sf.showFinished();

            //afisare rezultate in fisier
            try {
                fileWriter.write(printFinalData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //inchidere fisier
        try{
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }
}
