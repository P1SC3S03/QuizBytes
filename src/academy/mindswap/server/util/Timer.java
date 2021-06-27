package academy.mindswap.server.util;

import java.io.*;
import java.net.Socket;

/**
 * Timer Class
 * Blueprint for the regressive Timer to be used in the game's MultiPlayer.
 */
public class Timer extends java.util.Timer implements Runnable {
    private int seconds;
    private BufferedWriter out;
    private Socket socket;

    /**
     * Constructor method for the Timer.
     *
     * @param minutes - received user minutes
     * @param socket  - socket from/to whom the message is going to be sent
     * @throws IOException If an input or output exception occurs.
     */
    public Timer(int minutes, Socket socket) throws IOException {
        this.socket = socket;
        this.seconds = minutes * 60;
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Starts the countdown from the user's given time in the Timer constructor.
     * Sends the user a GAME OVER message when the time reaches 0.
     */
    @Override
    public void run() {
        while (!socket.isClosed()) {
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

    /**
     * Getter for the seconds
     *
     * @return the current seconds
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Setter for the seconds
     *
     * @param seconds - second to be set
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}

