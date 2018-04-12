import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;




public class FileIO {

    public ArrayList<Process> readInputFIle(String file) {

        ArrayList<Process> processes = null;
        try {

            Scanner s = new Scanner(new File("E:/Scheduler/"+file+".txt"));
            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNext()) {
                list.add(s.next());
            }
            s.close();


            //Remove the processes number from the list
            list.remove(0);

            //
            ArrayList<Integer> IDs = new ArrayList<Integer>();
            ArrayList<Double> arrivalTime = new ArrayList<Double>();
            ArrayList<Double> burstTime = new ArrayList<Double>();
            ArrayList<Integer> priority = new ArrayList<Integer>();

           //get parameters
            setParameters(list,IDs , arrivalTime , burstTime,priority);


            //Create process objects
            processes = createProcesses(IDs , arrivalTime , burstTime,priority);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


        return processes;
    }

    public  void setParameters(ArrayList<String> stringArr,
                               ArrayList<Integer> IDs, ArrayList<Double> arrivalTime ,
                               ArrayList<Double> burstTime, ArrayList<Integer> priority) {


        for(int i = 0; i < stringArr.size(); i+=4) {
            IDs.add(Integer.parseInt(stringArr.get(i)));
            arrivalTime.add(Double.parseDouble(stringArr.get(i+1)));
            burstTime.add(Double.parseDouble(stringArr.get(i+2)));
            priority.add(Integer.parseInt(stringArr.get(i+3)));
        }

    }

    public ArrayList<Process> createProcesses(ArrayList<Integer> IDs, ArrayList<Double> arrivalList,
                                              ArrayList<Double> burstList, ArrayList<Integer> priority)
    {
        ArrayList<Process> processesList = new ArrayList<Process>();
        for(int i = 0; i< IDs.size(); i++)
        {
            processesList.add(new Process(IDs.get(i),arrivalList.get(i),burstList.get(i),priority.get(i)));
        }
        return processesList;
    }

    public void generateOutput(ArrayList<Integer> processNumber, ArrayList<Double> waitingTime,ArrayList<Double> turnaroundTime,ArrayList<Double> weightedTurnaroundTime)
    {
        BufferedWriter writer=null;
        double sumWeighted=0;
        double sumTurnaround=0;
        try{
            writer = new BufferedWriter(new FileWriter("output.txt"));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        for(int i = 0 ; i<processNumber.size()-1; i++)
        {
            for(int j=i+1; j<processNumber.size();j++) {
                if (processNumber.get(i)>processNumber.get(j))
                {
                    Collections.swap(processNumber,i,j);
                    Collections.swap(waitingTime,i,j);
                    Collections.swap(turnaroundTime,i,j);
                    Collections.swap(weightedTurnaroundTime,i,j);
                }
            }
        }

        try {
            writer.write(processNumber.get(0)+" ");
            writer.append(waitingTime.get(0).toString()+" ");
            writer.append(turnaroundTime.get(0).toString()+" ");
            writer.append(weightedTurnaroundTime.get(0).toString()+System.lineSeparator() );
            sumWeighted+=weightedTurnaroundTime.get(0);
            sumTurnaround+=turnaroundTime.get(0);

            for(int i=1; i<processNumber.size(); i++)
            {
                writer.write(processNumber.get(i)+" ");
                writer.append(waitingTime.get(i).toString()+" ");
                writer.append(turnaroundTime.get(i).toString()+" ");
                writer.append(weightedTurnaroundTime.get(i).toString()+System.lineSeparator() );
                sumWeighted+=weightedTurnaroundTime.get(i);
                sumTurnaround+=turnaroundTime.get(i);
            }
            double avgWait = (sumWeighted/processNumber.size());
            double avgTurnaround = (sumTurnaround/processNumber.size());
            writer.append(Double.toString(avgWait) + System.lineSeparator());
            writer.append(Double.toString(avgTurnaround));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}

