package businessLogic;

import model.Server;
import model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        int minQueue=100000;
        Server chosenServer = new Server();
        for(Server s: servers){
            if(s.getSizeServer()<minQueue){
                minQueue=s.getSizeServer();
                chosenServer=s;
            }
        }
        chosenServer.addTask(t);
        t.setWaitingTime(chosenServer.getWaitingPeriod().intValue());
    }
}
