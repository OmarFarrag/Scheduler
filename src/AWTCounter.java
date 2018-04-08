import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.util.ArrayList;

// An AWT program inherits from the top-level container java.awt.Frame
public class AWTCounter extends Frame  {
    private Label lblFileName;    // Declare a Label component
    private TextField txtFileName; // Declare a TextField component
         // Counter's value

    // Constructor to setup GUI components and event handlers
    public AWTCounter () {
        setLayout(new FlowLayout());
        // "super" Frame, which is a Container, sets its layout to FlowLayout to arrange
        // the components from left-to-right, and flow to next row from top-to-bottom.

        lblFileName = new Label("Input file name:");  // construct the Label component

        add(lblFileName);                    // "super" Frame container adds Label component

        txtFileName = new TextField(10); // construct the TextField component with initial text
        txtFileName.setEditable(true);       // set to read-only
        add(txtFileName);                     // "super" Frame container adds TextField component

        setTitle("AWT Counter");  // "super" Frame sets its title
        setSize(1000, 100);        // "super" Frame sets its initial window size


        setVisible(true);         // "super" Frame shows

    }

    // The entry main() method
    public static void main(String[] args) {
       FileIO manager = new FileIO();
       Process p1 = new Process(1,2,4,5);
       Process p2 = new Process(2,3,2,8);
        Process p4 = new Process(8,8,8,9);
        Process p3 = new Process(9,8,8,8);
        ArrayList<Process> list = new ArrayList<Process>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(new Process(10, 3, 1, 1));


        Schedule x = SRTN.schedule(list,1.0);


    }

    // ActionEvent handler - Called back upon button-click.
   /* @Override
    public void actionPerformed(ActionEvent evt) {
        ++count; // Increase the counter value
        // Display the counter value on the TextField tfCount
        tfCount.setText(count + ""); // Convert int to String
    }*/
}