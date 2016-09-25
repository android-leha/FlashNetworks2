package ru.zapiataia.learn.connection;

import ru.zapiataia.learn.events.Event;
import ru.zapiataia.learn.logger.LoggerController;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class ConnectionsStorage {

    private static final int MAX_STORAGE_SIZE = 300000;
    private static final int TIME_TO_LIVE = 10800000;

    private final LoggerController logger;

    private class GarbigeCollector extends Thread {
        public GarbigeCollector() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(cleanOldConnections());
                } catch (InterruptedException ignored) {}
            }
        }
    }



    private LinkedHashMap<String, ExtConnData> connections = new LinkedHashMap<String, ExtConnData>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ExtConnData> eldest) {
            if (size() > MAX_STORAGE_SIZE) {
                logger.log(eldest.getKey() + " is removed");
                return true;
            } else {
                return false;
            }
        }
    };

    public ConnectionsStorage(LoggerController logger) {
        this.logger = logger;
        new GarbigeCollector().start();
    }

    public synchronized void put(Event event) {
        if (event.getType() == Event.Type.START) {
            connections.put(event.getKey(), new ExtConnData(event));
        } else {
            ExtConnData data = connections.get(event.getKey());
            if (data != null) {
                data.connData = event.getConnData();
                logger.log(event.getKey() + " is closed");
                connections.remove(event.getKey());
            }

        }
    }

    private synchronized long cleanOldConnections() {
        Iterator<String> it = connections.keySet().iterator();
        while (it.hasNext())
        {
            String key = it.next();
            long diff = System.currentTimeMillis() - connections.get(key).connData.timeStamp;
            if (diff > TIME_TO_LIVE) {
                logger.log(key + " cleaned");
                it.remove();
            } else {
                return TIME_TO_LIVE - diff;
            }
        }
        return TIME_TO_LIVE;
    }

    public int getCount() {
        return connections.size();
    }
}
