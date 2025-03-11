package gui;

import businessLogic.SelectionPolicy;
import businessLogic.SimulationManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SimulationFrame extends JFrame {
    private JPanel panel1;
    private JTextArea textAreaQueues;
    private JTextArea textAreaWaitingClients;
    private JTextField queuesNr;
    private JTextField clientsNr;
    private JTextField simulationTime;
    private JTextField minArrivalTime;
    private JTextField maxArrivalTime;
    private JTextField minServiceTime;
    private JTextField maxServiceTime;
    private JButton resetButton;
    private JButton validateButton;
    private JButton startButton;
    private JLabel averageWaitingTime;
    private JLabel averageServiceTime;
    private JLabel peakHour;
    private JLabel time;
    private JComboBox strategie;
    private boolean valid=false;
    private SelectionPolicy selectionPolicy;
    private SimulationManager manager;

    public SimulationFrame() {
        setContentPane(panel1);
        setTitle("Queue Menagement");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        //textAreaWaitingClients.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        textAreaQueues.setEditable(false);
        textAreaWaitingClients.setEditable(false);
        textAreaQueues.setFocusable(false);
        textAreaWaitingClients.setFocusable(false);

        startButton.setEnabled(false);

        if(strategie.getSelectedItem()=="shortest queue")
            selectionPolicy=SelectionPolicy.SHORTEST_QUEUE;
        else
            selectionPolicy=SelectionPolicy.SHORTEST_TIME;

        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (clientsNr.getText().equals("") || queuesNr.getText().equals("") || simulationTime.getText().equals("")
                        || minArrivalTime.getText().equals("") || maxArrivalTime.getText().equals("")
                        || minServiceTime.getText().equals("") || maxServiceTime.getText().equals("")) {
                    valid = false;
                    JOptionPane.showMessageDialog(panel1, "Fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    valid = true;
                    JOptionPane.showMessageDialog(panel1, "Data is valid!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    validateButton.setEnabled(false);
                    startButton.setEnabled(true);
                }

            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (valid == true) {
                    startSimulationManager();
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanEverything();
                validateButton.setEnabled(true);
                startButton.setEnabled(false);
                manager.setReset(true);
            }
        });

    }

    public void showFinished(){
        JOptionPane.showMessageDialog(panel1, "Simulation finished!", "Finalized", JOptionPane.INFORMATION_MESSAGE);
    }

    public void startSimulationManager(){
        manager = new SimulationManager(this, selectionPolicy, jtfToInteger(clientsNr), jtfToInteger(queuesNr), jtfToInteger(simulationTime),
                jtfToInteger(minArrivalTime), jtfToInteger(maxArrivalTime),
                jtfToInteger(minServiceTime), jtfToInteger(maxServiceTime));
        //startButton.setEnabled(false);
        Thread a=new Thread(manager);
        a.start();
    }

    public Integer jtfToInteger(JTextField jtf){
        if(Objects.equals(jtf.getText(), ""))
            return 0;
        else
            return Integer.parseInt(jtf.getText());
    }

    public void cleanEverything(){
        clientsNr.setText("");
        queuesNr.setText("");
        simulationTime.setText("");
        minArrivalTime.setText("");
        maxArrivalTime.setText("");
        minServiceTime.setText("");
        maxServiceTime.setText("");
        textAreaQueues.setText("");
        textAreaWaitingClients.setText("");
        averageWaitingTime.setText("");
        averageServiceTime.setText("");
        peakHour.setText("");
        time.setText("Time");
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void updateLiveGUI(int t, String waitingClients, String queues){
        time.setText("Time " + String.valueOf(t));
        textAreaWaitingClients.setText(waitingClients);
        textAreaQueues.setText(queues);
    }

    public void updateResultsGUI(String avgWaitingTime, String avgServiceTime, int h){
        averageWaitingTime.setText(avgWaitingTime);
        averageServiceTime.setText(avgServiceTime);
        peakHour.setText(String.valueOf(h));
    }

    public void setStartButton(boolean b){
        startButton.setEnabled(b);
    }

}
