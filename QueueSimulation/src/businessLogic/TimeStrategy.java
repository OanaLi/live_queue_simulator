package businessLogic;

import model.Server;
import model.Task;

import java.util.List;

public class TimeStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        int minWaintingPeriod = 1000000;
        Server chosenServer = new Server();

        for (Server s : servers) {
            if (s.getWaitingPeriod().intValue() < minWaintingPeriod) {
                minWaintingPeriod = s.getWaitingPeriod().intValue();
                chosenServer = s;
            }
        }
        chosenServer.addTask(t);
        t.setWaitingTime(chosenServer.getWaitingPeriod().intValue());

    }
}
