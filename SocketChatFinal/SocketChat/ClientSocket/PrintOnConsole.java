package ClientSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PrintOnConsole implements Runnable, AutoCloseable {
    BufferedReader bufferedReader;
    boolean cannotBeCalled;
    //Constructors
    public PrintOnConsole() {
        cannotBeCalled = false;
        bufferedReader = null;
    }
    public PrintOnConsole(Socket socket) {
        cannotBeCalled = false;
        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //override method run from Runnable
    @Override
    public void run() {
        try {
            while (true) {
                if(cannotBeCalled){
                    close();
                    break;
                }
                String userinput = bufferedReader.readLine();
                if (userinput == null || userinput.isEmpty()){
                    Thread.sleep(500);
                }
                //exit from program
                if (userinput != null && userinput.equals("exit")) {
                    //close statement
                    cannotBeCalled = true;
                } else {
                    System.out.println(userinput);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //Override method close from Runnable
    @Override
    public void close() {
        try {
            //close befferedreader stream
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
