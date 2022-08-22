package ClientSocket;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ReadFromConsole implements Runnable, AutoCloseable{
    BufferedWriter bufferedWriter;
    boolean cannotBeCalled;
    //constructors
    public ReadFromConsole()  {
        cannotBeCalled = false;
        bufferedWriter = null;
    }
    public ReadFromConsole(Socket socket)  {
        cannotBeCalled = false;
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                //close statement
                if (cannotBeCalled){
                    close();
                    break;
                }
                String outputStream = scanner.nextLine();
                //append
                bufferedWriter.append(outputStream).append('\n');
                //exit
                if (outputStream.equals("exit")) {
                    Thread.sleep(500);
                    cannotBeCalled = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void close()  {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


