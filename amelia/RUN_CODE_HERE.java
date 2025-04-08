package amelia;

/**
 * This is the main class that starts the GUI application.
 */
public class RUN_CODE_HERE 
{
    /**
     * This is the main method. It runs when the program starts.
     */
    public static void main(String[] args) 
    {
        // This line makes sure the MainFrame (GUI window) opens safely on the correct thread.
        // The part () -> new MainFrame() is a lambda — a short way to say: “run this code”.
        
        javax.swing.SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
