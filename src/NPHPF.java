import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

public  class NPHPF {


    static Schedule schedule (ArrayList<Process> processesList , double contextSwitch)
    {


        //arrange the processes ascendingly according to arrival time, breaking ties by descending priority
        processesList.sort(comparing(Process::getArrivalTime).thenComparing(reverseOrder(comparing(Process::getPriority))));

        //handle the case of more than one initial process
        //
        /*   int similarCount=0;
        double arrivalTimeInitial = processesList.get(0).getArrivalTime();
        for(int i=1; i< processesList.size(); i++)
        {
            if(processesList.get(i).getArrivalTime()== arrivalTimeInitial)
            {
                similarCount++;
            }
            else
            {
             break;
            }
        }

        //If found, arrange descendingly according to priority
        if(similarCount !=0)
        {
            processesList.subList(0,similarCount).sort(Comparator.comparing(Process::getPriority).reversed());
        }
*/

        //First node is the start
        ArrayList<Double> NodesTime = new ArrayList<Double>();
        NodesTime.add(processesList.get(0).getArrivalTime());
        ArrayList<Integer> NodesProcessesNumbers = new ArrayList<Integer>();
        NodesProcessesNumbers.add(processesList.get(0).getNumber());

        //Waiting time list
        ArrayList<Double> waitingTime = new ArrayList<Double>();
        waitingTime.add(0.0);

        //Turnaround time List
        ArrayList<Double> turnaroundTime = new ArrayList<Double>();
        turnaroundTime.add(processesList.get(0).getBurstTime());

        //Weighted turnaround time List
        ArrayList<Double> weightedTurnaroundTime = new ArrayList<Double>();
        weightedTurnaroundTime.add(1.0);

        //Process number for waiting etc list
        ArrayList<Integer> processNumber = new ArrayList<Integer>();
        processNumber.add(processesList.get(0).getNumber());

        double endTime = processesList.get(0).getBurstTime()+processesList.get(0).getArrivalTime();

        for( int i=1; i<processesList.size() ; i++)
        {
            for(int j=i+1 ; j<processesList.size(); j++)
            {
                if ((processesList.get(j).getArrivalTime() <= endTime) &&(( processesList.get(j).getPriority() > processesList.get(i).getPriority())
                        || (processesList.get(j).getPriority() == processesList.get(i).getPriority()&& (processesList.get(j).getNumber() < processesList.get(i).getNumber())) ))
                {
                    Collections.swap(processesList, i, j);
                }

               else {break;}

            }

            if(processesList.get(i).getArrivalTime()<=endTime) {

                processNumber.add(processesList.get(i).getNumber());

                waitingTime.add(endTime + contextSwitch - processesList.get(i).getArrivalTime());

                NodesTime.add(endTime);
                NodesProcessesNumbers.add(0);

                NodesTime.add(endTime + contextSwitch);
                NodesProcessesNumbers.add(processesList.get(i).getNumber());

                endTime = endTime + contextSwitch + processesList.get(i).getBurstTime();

                turnaroundTime.add(endTime - processesList.get(i).getArrivalTime());

                weightedTurnaroundTime.add((endTime - processesList.get(i).getArrivalTime()) / (contextSwitch + processesList.get(i).getBurstTime()));
            }
            else
            {
                processNumber.add(processesList.get(i).getNumber());
                waitingTime.add(0.0);

                NodesTime.add(endTime);
                NodesProcessesNumbers.add(0);

                NodesTime.add(processesList.get(i).getArrivalTime());
                NodesProcessesNumbers.add(processesList.get(i).getNumber());

                endTime = processesList.get(i).getArrivalTime() + processesList.get(i).getBurstTime();

                turnaroundTime.add(processesList.get(i).getBurstTime());

                weightedTurnaroundTime.add(1.0);
            }

        }
        return new Schedule(NodesTime,NodesProcessesNumbers,waitingTime,turnaroundTime,weightedTurnaroundTime,processNumber);
    }




}
