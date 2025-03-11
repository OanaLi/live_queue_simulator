package businessLogic;
import model.Server;
import model.Task;

import java.util.*;

public class Scheduler {
    private List<Server> servers;
    private Strategy strategy;

    public Scheduler(int NoServers, SelectionPolicy selectionPolicy){

        servers=new LinkedList<>();
        //pornire servere
        for(int i=0; i<NoServers; i++){
            servers.add(new Server()); //initializare
            Thread serverThread = new Thread(servers.get(i));
            serverThread.start();
        }

        if(selectionPolicy==SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        } else {
            strategy = new ShortestQueueStrategy();
        }

    }

    public String printareQueues(){
        int k = 0;
        String text = "";
        for (Server s : servers) {
            Task[] tasks = new Task[s.getSizeServer()];
            tasks = s.getTasks();

            k++;
            text=text+" Queue " + k + ": ";
            if(s.getSizeServer()==0) {
                text=text+"closed\n";
            }
            else {
                for (int i = 0; i < s.getSizeServer(); i++)
                    text=text+tasks[i] + " ";
                text=text+"\n";
            }
        }

        return text;
    }

    public void dispatchTask(Task t) {
        strategy.addTask(servers, t);
    }

    public int clientsInQueues(){
        int nr=0;
        for (Server s : servers)
            nr += s.getSizeServer();

        return nr;

    }

}
