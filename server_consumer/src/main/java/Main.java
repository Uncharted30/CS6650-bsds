import consumer.ConsumerThread;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 128; i++) {
            new Thread(new ConsumerThread()).start();
        }
        System.out.println("Consumer online...");
    }
}
