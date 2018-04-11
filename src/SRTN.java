import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.DoubleBinaryOperator;

import static java.util.Comparator.comparing;

public class SRTN {

    public static Schedule schedule(ArrayList<Process> processesList, Double contextSwitch) {
        //arrange the processes ascendingly according to arrival time, breaking ties by descending priority
        processesList.sort(comparing(Process::getArrivalTime).thenComparing(Process::getBurstTime).thenComparing(Process::getNumber));

        ArrayList<Double> nodesTime = new ArrayList<>();
        //initialize time with first process arrival time
        nodesTime.add(processesList.get(0).getArrivalTime());

        //store process numbers in a list
        ArrayList<Integer> ProcessNumbers = new ArrayList<>();
        for(Process process : processesList) {
            ProcessNumbers.add(process.getNumber());
        }

        //initially remainingTime for all processes equals burst time.
        ArrayList<Double> remainingTimeList = new ArrayList<>();
        for(int i = 0; i < processesList.size(); i++) {
            remainingTimeList.add(processesList.get(i).getBurstTime());
        }

        ArrayList<Double> waitingTime = new ArrayList<>();
        ArrayList<Integer> nodesProcessNumbers = new ArrayList<>();

        //Turnaround time List
        ArrayList<Double> turnaroundTimeList = new ArrayList<Double>();

        //Weighted turnaround time List
        ArrayList<Double> weightedTurnaroundTime = new ArrayList<Double>();


        //remainingTime for the processes to finish.
        Double timePassed = processesList.get(0).getArrivalTime();
        Double nextArrivalTime = 0.0;
        Double turnaroundTime = 0.0;

        int arrivalTimeIndex = 0;
        //the process executing right now
        int currentProcess = 0;
        int previousProcess = 0;
        Double SRTN = 0.0;

        while(true) {

            if(getSum(remainingTimeList) == 0.0) {
                break;
            }

            if(timePassed < processesList.get(processesList.size() - 1).getArrivalTime()) {
                currentProcess = getNextSRTNIndex(timePassed, remainingTimeList, processesList);
                if(currentProcess < processesList.size() - 1) {
                    nextArrivalTime = getNextArrivalTime(timePassed, processesList, currentProcess);
                }
                if(previousHasFinished(timePassed, remainingTimeList, processesList)) {
                    timePassed = nextArrivalTime;
                    continue;
                }
                if(nextArrivalTime - timePassed >= remainingTimeList.get(currentProcess)) {
                    if(previousProcess != currentProcess) {
                        timePassed += contextSwitch;
                        nodesProcessNumbers.add(0);
                        nodesTime.add(timePassed);
                        turnaroundTime = timePassed -
                                processesList.get(previousProcess).getArrivalTime();
                    }
                    nodesProcessNumbers.add(
                            processesList.get(currentProcess).getNumber());
                    timePassed += remainingTimeList.get(currentProcess);
                    remainingTimeList.set(currentProcess, 0.0);
                    nodesTime.add(timePassed);
                    previousProcess = currentProcess;
                }
                else if(nextArrivalTime.equals(timePassed)) {
                   while(nextArrivalTime.equals(timePassed)) {
                       arrivalTimeIndex++;
                       nextArrivalTime = processesList.get(arrivalTimeIndex).getArrivalTime();
                   }
                   if(remainingTimeList.get(currentProcess) < nextArrivalTime) {
                       if(currentProcess != previousProcess) {
                           timePassed += contextSwitch;
                           nodesProcessNumbers.add(0);
                           nodesTime.add(timePassed);
                           turnaroundTime = timePassed -
                                   processesList.get(previousProcess).getArrivalTime();
                       }
                       nodesProcessNumbers.add(
                               processesList.get(currentProcess).getNumber());
                       timePassed += remainingTimeList.get(currentProcess);
                       remainingTimeList.set(currentProcess, 0.0);
                       nodesTime.add(timePassed);
                       previousProcess = currentProcess;
                   }
                   else {
                       nodesProcessNumbers.add(currentProcess);
                       remainingTimeList.set(currentProcess,
                               remainingTimeList.get(currentProcess) -
                                       (nextArrivalTime - timePassed));
                       timePassed += (nextArrivalTime - timePassed);
                       nodesTime.add(timePassed);
                    }
                }
                else {
                    if(previousProcess != currentProcess) {
                        timePassed += contextSwitch;
                        nodesProcessNumbers.add(0);
                        nodesTime.add(timePassed);
                        turnaroundTime = timePassed -
                                processesList.get(previousProcess).getArrivalTime();
                    }
                    nodesProcessNumbers.add(
                            processesList.get(currentProcess).getNumber());
                    remainingTimeList.set(currentProcess,
                            remainingTimeList.get(currentProcess) -
                                    (nextArrivalTime - timePassed));
                    timePassed += (nextArrivalTime - timePassed);
                    nodesTime.add(timePassed);
                    previousProcess = currentProcess;
                }

            }
            else {
                currentProcess = getSRTNIndex(remainingTimeList, processesList);
                if(previousProcess != currentProcess) {
                    timePassed += contextSwitch;
                    nodesProcessNumbers.add(0);
                    nodesTime.add(timePassed);
                    turnaroundTime = timePassed -
                            processesList.get(previousProcess).getArrivalTime();
                }
                nodesProcessNumbers.add(
                        processesList.get(currentProcess).getNumber());
                timePassed += remainingTimeList.get(currentProcess);
                remainingTimeList.set(currentProcess, 0.0);
                nodesTime.add(timePassed);
                previousProcess = currentProcess;
            }
        }

        nodesProcessNumbers.add(0);

        Schedule schedule = new Schedule(nodesTime, nodesProcessNumbers, null, null, null, null);

        return schedule;
    }

    public static int getSRTNIndex(ArrayList<Double> remainingTimeList, ArrayList<Process> processesList) {
        Double nearestSRTN = 1000.0;
        int nearestSRTNIndex = 0;
        for(int i = 0; i < remainingTimeList.size(); i++) {
                if(remainingTimeList.get(i) > 0.0 && remainingTimeList.get(i) < nearestSRTN) {
                    nearestSRTN = remainingTimeList.get(i);
                    nearestSRTNIndex = i;
                }
                else if(remainingTimeList.get(i) == nearestSRTN && remainingTimeList.get(i) > 0.0) {
                    if(processesList.get(nearestSRTNIndex).getNumber()
                            > processesList.get(i).getNumber()) {
                        nearestSRTNIndex = i;
                    }
                }
        }
        return nearestSRTNIndex;


    }

    public static int getNextSRTNIndex( Double timePassed,
            ArrayList<Double> remainingTimeList, ArrayList<Process> processes) {
        Double nearestSRTN = 1000.0;
        int nearestSRTNIndex = 0;
        for(int i = 0; i < remainingTimeList.size(); i++) {
                if (remainingTimeList.get(i) > 0.0 && remainingTimeList.get(i) < nearestSRTN
                        && timePassed >= processes.get(i).getArrivalTime()) {
                    nearestSRTN = remainingTimeList.get(i);
                    nearestSRTNIndex = i;
                } else if (remainingTimeList.get(i) > 0.0 && remainingTimeList.get(i) == nearestSRTN
                        && timePassed >= processes.get(i).getArrivalTime()) {
                    if (processes.get(nearestSRTNIndex).getNumber()
                            > processes.get(i).getNumber()) {
                        nearestSRTNIndex = i;
                    }
                }
        }
        return nearestSRTNIndex;
    }

    public static Double getSum(ArrayList<Double> remainingTimeList) {
        Double sum = 0.0;
        for(int i = 0; i < remainingTimeList.size(); i++) {
            sum += remainingTimeList.get(i);
        }
        return sum;
    }

    public static Double getNextArrivalTime(Double timePassed, ArrayList<Process> processes, int currentProcess) {
        Double arrivalTime = 10000.0;
        for(int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getArrivalTime() < arrivalTime
                    && timePassed < processes.get(i).getArrivalTime()
                    && i > currentProcess) {
                arrivalTime = processes.get(i).getArrivalTime();
            }
        }
        return arrivalTime;
    }

    public static boolean previousHasFinished(Double timePassed, ArrayList<Double> remainingTimeList,
                                       ArrayList<Process> processesList) {
        int processCount = 0;
        int finishedCount = 0;
        for(int i = 0; i < processesList.size(); i++) {
            if(timePassed > processesList.get(i).getArrivalTime()) {
                processCount++;
            }
        }
        for(int i = 0; i < processesList.size(); i++) {
            if(remainingTimeList.get(i).equals(0.0));
            finishedCount++;
        }
        if(finishedCount == processCount) {
            return true;
        }
        return false;
    }

    public static int getSRTNIndex(Double timePassed, ArrayList<Double> remainingTimeList,
                                   ArrayList<Process> processesList) {
        Double SRTN = 1000.0;
        int index = 0;
        for(int i = 0; i < processesList.size(); i++) {
            if(timePassed <= processesList.get(i).getArrivalTime()
                    && remainingTimeList.get(i) > 0 &&
                    remainingTimeList.get(i) < SRTN) {
                index = i;
                SRTN = remainingTimeList.get(i);
            }
            if(timePassed <= processesList.get(i).getArrivalTime()
                    && remainingTimeList.get(i) > 0 &&
                    remainingTimeList.get(i) < SRTN &&
                    processesList.get(i).getNumber() < processesList.get(index).getNumber()) {
                index = i;
                SRTN = remainingTimeList.get(i);
            }
        }
        return index;
    }

    public static Double getSRTN(ArrayList<Double> remainingTimeList, ArrayList<Process> processesList) {
        Double SRTN = 1000.0;
        int index = 0;
        for(int i = 0; i < processesList.size(); i++) {
            if(remainingTimeList.get(i) > 0 && remainingTimeList.get(i) < SRTN) {
                SRTN = remainingTimeList.get(i);
            }
            if(remainingTimeList.get(i) > 0 && remainingTimeList.get(i) < SRTN &&
                    processesList.get(i).getNumber() < processesList.get(index).getNumber()) {
                index = i;
                SRTN = remainingTimeList.get(i);
            }
        }
        return SRTN;
    }


}
