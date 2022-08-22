package ClientSocket;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args)  {
        try (Socket socket = new Socket("host", 8080)) {
            Thread consoleOutput = new Thread(new PrintOnConsole(socket));
            Thread readerFromConsole = new Thread(new ReadFromConsole(socket));
            consoleOutput.setName("Console printer");
            consoleOutput.setDaemon(true);
            readerFromConsole.setName("Console reader");
            readerFromConsole.setDaemon(true);
            consoleOutput.start();
            readerFromConsole.start();
            readerFromConsole.join();
            consoleOutput.join();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}


