import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Day;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.time.Period;
import java.util.ArrayList;




    public class Chart extends ApplicationFrame {

        public Chart( String applicationTitle, String chartTitle ,ArrayList<Integer> nodesProcesses , ArrayList<Double> nodesTime) {
            super(applicationTitle);
            JFreeChart xylineChart = ChartFactory.createXYLineChart(
                    chartTitle ,
                    "Time" ,
                    "Process number" ,
                    createDataset(nodesProcesses,nodesTime) ,
                    PlotOrientation.VERTICAL ,
                    true , true , false);

            ChartPanel chartPanel = new ChartPanel( xylineChart );
            chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
            final XYPlot plot = xylineChart.getXYPlot( );

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
            renderer.setSeriesPaint( 0 , Color.RED );

            renderer.setSeriesStroke( 0 , new BasicStroke( 2.0f ) );


            plot.setRenderer( renderer );
            setContentPane( chartPanel );
        }

        private XYDataset createDataset( ArrayList<Integer> nodesProcesses , ArrayList<Double> nodesTime) {
            final XYSeries firefox = new XYSeries( "Firefox" );
            firefox.add( nodesTime.get(0), nodesProcesses.get(0));
            for(int i=1; i<nodesProcesses.size(); i++)
            {
                firefox.add(nodesTime.get(i),nodesProcesses.get(i-1));
                firefox.add(nodesTime.get(i),nodesProcesses.get(i));
            }



            final XYSeriesCollection dataset = new XYSeriesCollection( );
            dataset.addSeries( firefox );

            return dataset;
        }

    public static void main( final String[ ] args ) {

        FileIO manager = new FileIO();
        ArrayList<Process> y = manager.readInputFIle();

        ArrayList<Process> list = new ArrayList<Process>();
        for(int i=0 ; i<y.size(); i++) {
            Process temp = y.get(i);
            list.add(new Process(temp.getNumber(),temp.getArrivalTime(), temp.getBurstTime(), temp.getPriority()));
        }

        Schedule x = NPHPF.schedule(list,1.0);

        Chart chart = new Chart("Browser Usage Statistics",
                "Which Browser are you using?",x.m_NodesProcessesNumbers,x.m_NodesTime);
        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }
}