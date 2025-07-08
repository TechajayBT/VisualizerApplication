package Controllers;

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

public class QueueController {

    private Queue<Integer> queue;
    private QueueUpdateListener updateListener;

    public QueueController() {
        this.queue = new LinkedList<>();
    }

    // Set the listener that gets notified when the queue changes
    public void setUpdateListener(QueueUpdateListener listener) {
        this.updateListener = listener;
    }

    // Add an element to the queue
    public void enqueue(int value) {
        queue.offer(value);
        notifyUpdate();
    }

    // Remove an element from the front of the queue
    public Integer dequeue() {
        Integer value = queue.poll();
        notifyUpdate();
        return value;
    }

    // View the front element without removing
    public Integer peek() {
        return queue.peek();
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    // Get the size of the queue
    public int size() {
        return queue.size();
    }

    // Get a snapshot of the queue as a List for UI rendering
    public List<Integer> getQueueSnapshot() {
        return new ArrayList<>(queue);
    }

    // Notify listener of queue changes
    private void notifyUpdate() {
        if (updateListener != null) {
            updateListener.onQueueUpdated(getQueueSnapshot());
        }
    }

    // Listener interface for UI updates
    public interface QueueUpdateListener {
        void onQueueUpdated(List<Integer> updatedQueue);
    }
}
