
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.*;
import org.jfree.data.*;

import java.util.ArrayList;


public class BarGraph extends ApplicationFrame{

    public BarGraph(String title) {
        super(title);

        JFreeChart barChart = ChartFactory.createBarChart(
                "Scheduled processes",
                "Time",
                "Process",
                createDataSet(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataSet() {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        ArrayList<Process> processesList = new ArrayList<>();
        processesList.add(new Process(1, 2, 2, 1));
        processesList.add(new Process(2, 3, 1, 1));
        processesList.add(new Process(3, 8, 8, 1));
        Schedule schedule = SRTN.schedule(processesList, 1.0);
        //dataSet.addValue();
        return null;
    }
}
