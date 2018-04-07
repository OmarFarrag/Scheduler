import com.sun.imageio.plugins.jpeg.JPEGImageMetadataFormatResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JLabel enterFileNameLabel;
    private JTextField enterFileNameText;
    private JButton chooseFileButton;
    private Handler handler;

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
                ScheduleViewer scheduleViewer = new ScheduleViewer();
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
    private JRadioButton SRTN;
    private JRadioButton NPHPF;
    private ButtonGroup buttonGroup;
    private JButton chooseAlgorithmButton;
    private ButtonModel buttonModel;
    private JTextField chosenAlgorithm;
    private EventHandler eventHandler;

    /*
    second step: choose context switch
     */
    private JButton enterContextSwitchTimeButton;
    private JTextField contextSwitchText;

    ScheduleViewer() {
        super("Scheduler");
        setLayout(new GridBagLayout());
        setSize(500, 400);

        RR = new JRadioButton("Round Robin");
        RR.setActionCommand("RR");
        SRTN = new JRadioButton("SRTN");
        SRTN.setActionCommand("SRTN");
        SRTN.setToolTipText("Shortest Remaining Time Next Algorithm");
        NPHPF = new JRadioButton("NPHPF");
        NPHPF.setActionCommand("NPHPF");
        NPHPF.setToolTipText("Non Preemtive Highest Priority First Algorithm");
        chooseAlgorithmButton = new JButton("OK");
        chooseAlgorithmButton.setSize(50, 20);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(RR);
        buttonGroup.add(SRTN);
        buttonGroup.add(NPHPF);

        eventHandler = new EventHandler();
        chooseAlgorithmButton.addActionListener(eventHandler);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        add(RR, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(SRTN, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(NPHPF, gridBagConstraints);
        gridBagConstraints.gridy++;
        add(chooseAlgorithmButton, gridBagConstraints);
    }


    private class EventHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == chooseAlgorithmButton) {
                buttonModel = buttonGroup.getSelection();
                chosenAlgorithm = new JTextField(40);
                chosenAlgorithm.setEditable(false);
                String r = null;
                try {
                    r = buttonModel.getActionCommand();
                }catch (Exception e) {}
                if(r == "RR") {
                    chosenAlgorithm.setText("Round Robin Algorithm Selected!");
                }
                else if (r == "SRTN") {
                    chosenAlgorithm.setText("shortest remaining time Algorithm next selected!");
                }
                else if (r == "NPHPF") {
                    chosenAlgorithm.setText("non preemtive highest priority first Algorithm selected");
                }
                if(r != null) {
                    chooseAlgorithmButton.setEnabled(false);
                    gridBagConstraints.gridx = 0;
                    gridBagConstraints.gridy = 4;
                    gridBagConstraints.gridwidth = 5;
                    add(chosenAlgorithm, gridBagConstraints);
                    revalidate();
                }
            }
        }
    }
}
