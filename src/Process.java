public class Process {
    int number;
    double arrivalTime;
    double burstTime;
    int priority;
    double waitTime;
    double turnAroundTime;

    public Process(int f_number, double f_arrivalTime, double f_burstTime, int f_priority)
    {
        number = f_number;
        arrivalTime = f_arrivalTime;
        burstTime = f_burstTime;
        priority = f_priority;
    }

    public int getNumber() {
        return number;
    }

    public double getArrivalTime()
    {
        return arrivalTime;
    }

    public double getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setWaitTime(double t){waitTime=t;}
    public void setTurnAroundTime(double t){turnAroundTime=t;}
}
