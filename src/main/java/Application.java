import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Application {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(Constants.LOOK_AND_FEEL);
            AppFrame frame = new AppFrame(Constants.FRAME_TITLE);
            SwingUtilities.updateComponentTreeUI(frame);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
