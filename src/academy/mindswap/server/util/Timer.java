package academy.mindswap.server.util;

import java.io.*;
import java.net.Socket;

public class Timer extends java.util.Timer implements Runnable {
    private int minutes;
    private int seconds;
    private BufferedWriter out;
    private Socket socket;

    public Timer(int minutes, Socket socket) throws IOException {
        this.socket = socket;
        this.minutes = minutes - 1;
        this.seconds = 30;
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            if (minutes < 0) {
                return;
            }
            seconds--;
            setSeconds(seconds);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (seconds == 0) {
                try {
                    out.write("GAME OVER!\nPress enter to check your results. :)");
                    out.newLine();
                    out.flush();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}

