

import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class GUI extends JFrame {
    private JLabel enterFileNameLabel;
    private JTextField enterFileNameText;
    private JButton chooseFileButton;
    private Handler handler;
    private ArrayList<Process> processes;

    public GUI(){
        super("Scheduler");
        setLayout(new FlowLayout(FlowLayout.LEADING, 15, 30));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        handler = new Handler();

        enterFileNameLabel = new JLabel("Enter file name: ");
        enterFileNameText = new JTextField("fileName.txt");
        chooseFileButton = new JButton("OK");
        chooseFileButton.addActionListener(handler);
        setSize(340, 100);

        add(enterFileNameLabel);
        add(enterFileNameText);
        add(chooseFileButton);

        setLocationRelativeTo(null);
        setVisible(true);

    }


    private class Handler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == chooseFileButton) {
                String filename= enterFileNameText.getText();
                FileIO filemanager = new FileIO();
                processes = filemanager.readInputFIle(filename);
                ScheduleViewer scheduleViewer = new ScheduleViewer(processes);
                scheduleViewer.setVisible(true);
            }
        }
    }
}

class ScheduleViewer extends JFrame {
    /*
    GridBagConstraints to set the coordinates of components
     */
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    /*
    first step: choose algorithm from radioButtons
     */
    private JRadioButton RR;
    private JRadioButton SRTNBtn;
    private JRadioButton NPHPFBtn;
    private ButtonGroup buttonGroup;
    private JButton chooseAlgorithmButton;
    private ButtonModel buttonModel;
    private JTextField chosenAlgorithm;
    private EventHandler eventHandler;

    private ArrayList<Process> processes;

    /*
    second step: choose context switch
     */
    private  JTextField contextSwitchTxt;
    private JTextField quantumTxt;

    ScheduleViewer(ArrayList<Process> f_processes) {



        super("Scheduler");

        processes=f_processes;

        setLayout(new GridBagLayout());
        setSize(500, 400);

        RR = new JRadioButton("Round Robin");
        RR.setActionCommand("RR");
        SRTNBtn = new JRadioButton("SRTN");
        SRTNBtn.setActionCommand("SRTN");
        SRTNBtn.setToolTipText("Shortest Remaining Time Next Algorithm");
        NPHPFBtn = new JRadioButton("NPHPF");
        NPHPFBtn.setActionCommand("NPHPF");
        NPHPFBtn.setToolTipText("Non Preemtive Highest Priority First Algorithm");
        JLabel contextSwitchLbl= new JLabel("Context switch: ");
        contextSwitchTxt= new JTextField("Context switch");
        JLabel quantumLbl= new JLabel("Quantum: ");
        quantumTxt= new JTextField("Quantum");
        chooseAlgorithmButton = new JButton("OK");
        chooseAlgorithmButton.setSize(50, 20);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(RR);
        buttonGroup.add(SRTNBtn);
        buttonGroup.add(NPHPFBtn);

        eventHandler = new EventHandler();
        chooseAlgorithmButton.addActionListener(eventHandler);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        add(RR, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(SRTNBtn, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(NPHPFBtn, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(contextSwitchLbl, gridBagConstraints);
        gridBagConstraints.gridx++;
        add(contextSwitchTxt, gridBagConstraints);
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx--;
        add(quantumLbl, gridBagConstraints);
        gridBagConstraints.gridx++;
        add(quantumTxt, gridBagConstraints);
        gridBagConstraints.gridy++;
        gridBagConstraints.gridx--;
        add(chooseAlgorithmButton, gridBagConstraints);
    }


    private class EventHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == chooseAlgorithmButton) {
                buttonModel = buttonGroup.getSelection();
                double contextSwitch = Double.parseDouble(contextSwitchTxt.getText());
                double quantum = Double.parseDouble(quantumTxt.getText());
                chosenAlgorithm = new JTextField(40);
                chosenAlgorithm.setEditable(false);
                String r = null;
                Schedule schedule = null;
                try {
                    r = buttonModel.getActionCommand();
                }catch (Exception e) {}
                if(r == "RR") {
                    chosenAlgorithm.setText("Round Robin Algorithm Selected!");
                    schedule = RoundRobin.schedule(processes,contextSwitch,quantum);
                }
                else if (r == "SRTN") {
                    chosenAlgorithm.setText("shortest remaining time Algorithm next selected!");
                    schedule = SRTN.schedule(processes,contextSwitch);
                }
                else if (r == "NPHPF") {
                    chosenAlgorithm.setText("non preemtive highest priority first Algorithm selected");
                    schedule = NPHPF.schedule(processes,contextSwitch);
                }
                if(r != null) {
                    chooseAlgorithmButton.setEnabled(false);
                    gridBagConstraints.gridx = 0;
                    gridBagConstraints.gridy = 4;
                    gridBagConstraints.gridwidth = 5;
                    add(chosenAlgorithm, gridBagConstraints);
                    revalidate();
                }

                FileIO manager= new FileIO();
                Chart chart = new Chart("Browser Usage Statistics",
                        "Which Browser are you using?",schedule.m_NodesProcessesNumbers,schedule.m_NodesTime);
                chart.pack( );
                RefineryUtilities.centerFrameOnScreen( chart );
                chart.setVisible( true );
                manager.generateOutput(schedule.m_processNumber,schedule.m_waitingTime,schedule.m_turnaroundTime,schedule.m_weightedTurnaroundTime);
            }
        }
    }
}
