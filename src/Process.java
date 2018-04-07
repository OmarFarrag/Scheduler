public class Process {
    int number;
    double arrivalTime;
    double burstTime;
    int priority;
    double waitTime;
    double remainingTime;
    double turnAroundTime;
    boolean running ;

    public Process(int f_number, double f_arrivalTime, double f_burstTime, int f_priority)
    {
        number = f_number;
        arrivalTime = f_arrivalTime;
        burstTime = f_burstTime;
        priority = f_priority;
        remainingTime = f_burstTime;
        running =false;
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
    public void setRunning(boolean b){running = b;}
    public boolean getRunning(){return running;}

    public void setWaitTime(double t){waitTime=t;}
    public double getWaitTime(){return waitTime;}
    public void setTurnAroundTime(double t){turnAroundTime=t;}
    public void decreaseRemainingTime(double t){remainingTime-=t;}
    public double getRemainingTime(){ return remainingTime;}
}
