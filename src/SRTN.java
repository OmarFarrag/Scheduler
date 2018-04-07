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
        Double nextArrivalTime = 0.0;

        int arrivalTimeIndex = 0;
        //the process executing right now
        int currentProcess = 0;

        boolean isLastProcess = false;

        while(true) {

            if(getSum(remainingTimeList) == 0.0) {
                break;
            }

            if(timePassed < processesList.get(processesList.size() - 1).getArrivalTime()) {
                if(arrivalTimeIndex < processesList.size() - 1) {
                    nextArrivalTime = processesList.get(arrivalTimeIndex + 1).getArrivalTime();
                }
                else {
                    isLastProcess = true;
                }
                if(timePassed >= nextArrivalTime && !isLastProcess) {
                    currentProcess = getNextSRTNIndex(timePassed, remainingTimeList, processesList);
                    if (remainingTimeList.get(currentProcess) > nextArrivalTime) {
                        timePassed = remainingTimeList.get(currentProcess)
                                - (nextArrivalTime - processesList.get(currentProcess).getArrivalTime());
                        remainingTimeList.set(currentProcess, timePassed);
                    } else {
                        timePassed += remainingTimeList.get(currentProcess);
                        remainingTimeList.set(currentProcess, 0.0);
                        nodesProcessNumbers.add(processesList.get(currentProcess).getNumber());
                        nodesTime.add(timePassed);
                        //nodesTime.add(timePassed + contextSwitch);
                        timePassed += contextSwitch;
                        currentProcess = getNextSRTNIndex(timePassed, remainingTimeList, processesList);
                        nodesProcessNumbers.add(processesList.get(
                                getNextSRTNIndex(timePassed, remainingTimeList, processesList)
                        ).getNumber());
                    }
                    arrivalTimeIndex++;
                }
                else if (timePassed >= nextArrivalTime && isLastProcess) {
                    timePassed += remainingTimeList.get(currentProcess);
                    remainingTimeList.set(currentProcess, 0.0);
                    nodesTime.add(timePassed);
                    //nodesTime.add(timePassed + contextSwitch);
                    timePassed += contextSwitch;
                    currentProcess = getNextSRTNIndex(timePassed, remainingTimeList, processesList);
                    nodesProcessNumbers.add(processesList.get(
                            getNextSRTNIndex(timePassed, remainingTimeList, processesList)
                    ).getNumber());
                }
                else {
                    if(timePassed < nextArrivalTime && remainingTimeList.get(currentProcess) <= 0.0) {
                        timePassed = nextArrivalTime;
                        currentProcess = getNextSRTNIndex(timePassed,
                                remainingTimeList, processesList);
                    }
                    while(timePassed < nextArrivalTime && remainingTimeList.get(currentProcess) > 0.0) {
                        if(nextArrivalTime - processesList.get(currentProcess).getArrivalTime()
                                > remainingTimeList.get(currentProcess)) {
                            nodesTime.add(timePassed);
                            timePassed += remainingTimeList.get(currentProcess);
                            nodesProcessNumbers.add(processesList.get(currentProcess).getNumber());
                            nodesTime.add(timePassed);
                            remainingTimeList.set(currentProcess, 0.0);
                            //nodesTime.add(timePassed + contextSwitch);
                            timePassed += contextSwitch;
                            arrivalTimeIndex++;
                            currentProcess = getNextSRTNIndex(timePassed, remainingTimeList, processesList);
                        }
                        else {
                            timePassed += remainingTimeList.get(currentProcess)
                                    - (nextArrivalTime - processesList.get(currentProcess).getArrivalTime());
                            remainingTimeList.set(currentProcess,
                                    remainingTimeList.get(currentProcess)
                                            - (nextArrivalTime - processesList.get(currentProcess).getArrivalTime()));
                        }
                    }
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
                //nodesTime.add(timePassed + contextSwitch);
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

    public static void setWaitingTimeList(ArrayList<Double> waitingTimeList) {
        for(int i = 0; i < waitingTimeList.size(); i++) {

        }
    }

}
