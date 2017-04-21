import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by sanjit.
 */
class RemoteControl extends JFrame {
    private Label keyPressLabel;


    RemoteControl() throws HeadlessException, IOException, AWTException {
        super();
        init();
        start();
    }

    public static void main(String[] args) throws IOException, AWTException {
        RemoteControl remoteControl = new RemoteControl();
    }

    private void init() throws IOException, AWTException {
        setLayout(new GridLayout(1, 2));

        keyPressLabel = new Label("None");
        add(keyPressLabel);

        new SocketConnection(keyPressLabel).start();
    }

    private void start() {
        setSize(200, 200);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
