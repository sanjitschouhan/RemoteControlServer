import java.awt.*;
import java.awt.event.InputEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by sanjit on 20/4/17.
 */
public class SocketConnection extends Thread {
    private Label label;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;
    private Robot robot;

    public SocketConnection(Label label) throws IOException, AWTException {
        this.label = label;
        serverSocket = new ServerSocket(5000);
        serverSocket.setSoTimeout(10000);
        robot = new Robot();
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                if (socket == null)
                    socket = serverSocket.accept();
                if (socket.isClosed())
                    socket = serverSocket.accept();

                in = new DataInputStream(socket.getInputStream());
                int type = in.readInt();
                if (type == 0) {
                    int key = in.readInt();
                    robot.keyPress(key);
                    robot.keyRelease(key);
                    label.setText(String.valueOf(key));
                    System.out.println("Key Pressed: " + key);
                } else if (type == 1) {
                    int dX = (int) in.readFloat();
                    int dY = (int) in.readFloat();
                    if (dX != 0 || dY != 0) {
                        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                        Point location = pointerInfo.getLocation();
//                        System.out.println("Mouse: " + dX + " " + dY);
                        robot.mouseMove((int) (location.getX() + dX), (int) (location.getY() + dY));
                        label.setText("Mouse Moved");
                    } else {
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        label.setText("Mouse Clicked");
                    }
                } else if (type == 2) {
                    int dY = (int) in.readFloat();
                    robot.mouseWheel(dY);
                    label.setText("Mouse Wheel Scrolled");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        serverSocket.close();
    }
}
