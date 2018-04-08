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
        nodesProcessNumbers.add(processesList.get(0).getNumber());

        //Turnaround time List
        ArrayList<Double> turnaroundTime = new ArrayList<Double>();

        //Weighted turnaround time List
        ArrayList<Double> weightedTurnaroundTime = new ArrayList<Double>();


        //remainingTime for the processes to finish.
        Double timePassed = processesList.get(0).getArrivalTime();
        Double nextArrivalTime;

        int arrivalTimeIndex;
        //the process executing right now
        int currentProcess = 0;
        int previousProcess = 0;

        while(true) {

            if(getSum(remainingTimeList) == 0.0) {
                break;
            }

            if(timePassed < processesList.get(processesList.size() - 1).getArrivalTime()) {
                nextArrivalTime = processesList.get(
                        getNextArrivalTimeIndex(timePassed, processesList)).getArrivalTime();
                if(timePassed < nextArrivalTime) {
                    currentProcess = getNextSRTNIndex(timePassed, remainingTimeList, processesList);
                    if(nextArrivalTime - timePassed < remainingTimeList.get(currentProcess)) {
                        timePassed += (nextArrivalTime
                                - processesList.get(currentProcess).getArrivalTime());
                        remainingTimeList.set(currentProcess,
                                remainingTimeList.get(currentProcess)
                                        - (nextArrivalTime - processesList.get(currentProcess).getArrivalTime()));
                        previousProcess = currentProcess;
                        currentProcess = getNextSRTNIndex(timePassed, remainingTimeList, processesList);
                        if(previousProcess != currentProcess) {
                            nodesProcessNumbers.add(processesList.get(previousProcess).getNumber());
                            nodesTime.add(timePassed);
                            timePassed += contextSwitch;
                            nodesTime.add(timePassed);
                            nodesProcessNumbers.add(processesList.get(currentProcess).getNumber());
                        }
                    }
                    else {
                        timePassed += remainingTimeList.get(currentProcess);
                        remainingTimeList.set(currentProcess, 0.0);
                        nodesTime.add(timePassed);
                        nodesProcessNumbers.add(processesList.get(currentProcess).getNumber());
                        timePassed += contextSwitch;
                        nodesTime.add(timePassed);
                    }
                }
                else {
                    currentProcess = getNextSRTNIndex(timePassed, remainingTimeList, processesList);
                    nodesTime.add(timePassed);
                    nodesProcessNumbers.add(processesList.get(getNextSRTNIndex(
                            timePassed, remainingTimeList, processesList
                    )).getNumber());
                    timePassed += remainingTimeList.get(currentProcess);
                    nodesProcessNumbers.add(processesList.get(currentProcess).getNumber());
                    nodesTime.add(timePassed);
                    remainingTimeList.set(currentProcess, 0.0);
                    timePassed += contextSwitch;
                }

            }
            else {
                currentProcess = getSRTNIndex(remainingTimeList, processesList);
                nodesTime.add(timePassed);
                nodesProcessNumbers.add(processesList.get(getNextSRTNIndex(
                        timePassed, remainingTimeList, processesList
                )).getNumber());
                timePassed += remainingTimeList.get(currentProcess);
                nodesProcessNumbers.add(processesList.get(currentProcess).getNumber());
                nodesTime.add(timePassed);
                remainingTimeList.set(currentProcess, 0.0);
                timePassed += contextSwitch;

            }
        }

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
                else if( remainingTimeList.get(i) == nearestSRTN && remainingTimeList.get(i) > 0.0) {
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

    public static int getNextArrivalTimeIndex(Double timePassed, ArrayList<Process> processes) {
        Double arrivalTime = 10000.0;
        int index = 0;
        for(int i = 0; i < processes.size(); i++) {
            if(processes.get(i).getArrivalTime() < arrivalTime
                    && timePassed < processes.get(i).getArrivalTime()) {
                index = i;
                arrivalTime = processes.get(i).getArrivalTime();
            }
        }
        return index;
    }

    public static void setWaitingTimeList(ArrayList<Double> waitingTimeList) {
        for(int i = 0; i < waitingTimeList.size(); i++) {

        }
    }

}
