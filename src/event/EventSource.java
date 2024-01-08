package event;

public interface EventSource {
    void addObserver(EventObserver eventObserver);
    void removeObserver(EventObserver eventObserver);
    boolean hasObserver(EventObserver eventObserver);
}
