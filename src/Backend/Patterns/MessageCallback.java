package Backend.Patterns;

import java.util.ArrayList;

public interface MessageCallback {
    ArrayList<Observer> messageObservers = new ArrayList<>();

    void send(String message);

    static void subscribe(Observer o) {
        messageObservers.add(o);
    }

    static void unSubscribe(Observer o) {
        messageObservers.remove(o);
    }
}