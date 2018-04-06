import java.util.ArrayList;

public class Schedule {
    ArrayList<Double> m_NodesTime;
    ArrayList<Integer> m_NodesProcessesNumbers;
    ArrayList<Double> m_waitingTime;
    ArrayList<Double> m_turnaroundTime;
    ArrayList<Double> m_weightedTurnaroundTime;

    public Schedule(ArrayList<Double> NodesTime, ArrayList<Integer> NodesProcessesNumbers,
                    ArrayList<Double> waitingTime,ArrayList<Double> turnaroundTime, ArrayList<Double> weightedTurnaroundTime)
    {
        m_NodesTime= NodesTime;
        m_NodesProcessesNumbers = NodesProcessesNumbers;
        m_waitingTime = waitingTime;
        m_turnaroundTime = turnaroundTime;
        m_weightedTurnaroundTime = weightedTurnaroundTime;
    }
}
