import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

public class CustomRR {
    static Schedule schedule (ArrayList<Process> processesList , double contextSwitch, double quantum)
    {
        processesList.sort(comparing(Process::getArrivalTime));

        Queue<Process> activeProcesses = new LinkedList<Process>();


        ArrayList<Double> NodesTime = new ArrayList<Double>();

        ArrayList<Integer> NodesProcessesNumbers = new ArrayList<Integer>();
        //Waiting time list
        ArrayList<Double> waitingTime = new ArrayList<Double>();


        //Turnaround time List
        ArrayList<Double> turnaroundTime = new ArrayList<Double>();


        //Weighted turnaround time List
        ArrayList<Double> weightedTurnaroundTime = new ArrayList<Double>();


        //Process number for waiting etc list
        ArrayList<Integer> processNumber = new ArrayList<Integer>();


        double time=0;
        int done =0;
        Process temp = null;
        int prevProcess = 0;
        int numberOfProcesses = processesList.size();


        while(done<numberOfProcesses )
        {

            while(!processesList.isEmpty() ) {
                if(processesList.get(0).getArrivalTime()<=time) {
                    activeProcesses.add(processesList.remove(0));
                }else{break;}

            }
            if(temp!=null){ activeProcesses.add(temp);}
            if(activeProcesses.peek()!= null) {
                temp = activeProcesses.remove();

                if(prevProcess!=0 && prevProcess!=temp.getNumber())
                {
                    NodesTime.add(time);
                    NodesProcessesNumbers.add(0);
                    time += contextSwitch;
                }

                prevProcess=temp.getNumber();

                NodesTime.add(time);
                NodesProcessesNumbers.add(temp.getNumber());

                double tempRemTime = temp.getRemainingTime();
                temp.decreaseRemainingTime(quantum);

                if(temp.getRunning()==false){
                    temp.setRunning(true);
                    temp.setWaitTime(time-temp.getArrivalTime());
                }
                if(temp.getRemainingTime()>0) {
                    time = time + quantum;
                }
                else
                {
                    time += tempRemTime;
                }
                if(temp.getRemainingTime()<=0)
                {
                    processNumber.add(temp.getNumber());

                    waitingTime.add(temp.getWaitTime());

                    double t = time-temp.getArrivalTime();
                    turnaroundTime.add(t);

                    weightedTurnaroundTime.add(t/temp.getBurstTime());

                    done++;
                    if(done==numberOfProcesses)
                    {
                        NodesTime.add(time);
                        NodesProcessesNumbers.add(temp.getNumber());
                        NodesTime.add(time);
                        NodesProcessesNumbers.add(0);
                    }

                    temp=null;
                }






            }
            else
            {
                NodesTime.add(time);
                NodesProcessesNumbers.add(0);
                prevProcess=0;
                time=processesList.get(0).getArrivalTime();

            }


        }
        return new Schedule(NodesTime,NodesProcessesNumbers,waitingTime,turnaroundTime,weightedTurnaroundTime,processNumber);
    }
}
