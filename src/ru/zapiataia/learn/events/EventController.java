package ru.zapiataia.learn.events;


import ru.zapiataia.learn.connection.ConnectionsStorage;
import ru.zapiataia.learn.logger.LoggerController;

import java.util.concurrent.atomic.AtomicLong;

public class EventController {
    private static final int LOST_PERCENTS = 15;

    public AtomicLong received = new AtomicLong(0);
    public AtomicLong lost = new AtomicLong(0);

    private ConnectionsStorage connectionStorage;


    public EventController(LoggerController logger) {
        this.connectionStorage = new ConnectionsStorage(logger);
    }

    public void receive(Event event) {
        int v = (int) (Math.random() * 100);
        if (v >= LOST_PERCENTS) {
            connectionStorage.put(event);
            received.incrementAndGet();
        } else {
            lost.incrementAndGet();
        }
    }

    public int getCount() {
        return connectionStorage.getCount();
    }
}
