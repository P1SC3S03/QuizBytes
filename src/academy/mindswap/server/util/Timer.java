package academy.mindswap.server.util;

public class Timer {

    int minutes;
    int seconds;

    public Timer (int minutes){
        this.minutes =  minutes-1;
    }

    public void timer() {

        if (minutes < 0){
            return;
        }

        System.out.print("Time   ");

        while (true) {
            seconds--;
            if (seconds == -1) {
                seconds = 59;
            }

            System.out.print(minutes+":"+seconds);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("\b\b\b\b"); //VERIFY

            if (minutes == 0 && seconds == 0) {
                System.out.println("Time is over!");
            }
        }
    }

    public static void main(String[] args) {
        Timer timer = new Timer(1);
        timer.timer();
    }

}

