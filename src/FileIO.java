import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;




public class FileIO {

    public ArrayList<Process> readInputFIle() {

        ArrayList<Process> processes = null;
        try {

            Scanner s = new Scanner(new File("E:/Scheduler/HPF1.txt"));
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



}

