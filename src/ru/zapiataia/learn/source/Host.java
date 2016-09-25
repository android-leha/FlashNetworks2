package ru.zapiataia.learn.source;


import ru.zapiataia.learn.events.Event;

import java.util.HashSet;

public class Host extends Thread {


    private static final int MAX_DELAY = 300;
    private static final int MIN_CONNECTION_TIME = 50;

    private final EventGenerator eventGenerator;

    private HashSet<Host> connectedHosts = new HashSet<>();

    Host(String name, EventGenerator eventGenerator) {
        this.eventGenerator = eventGenerator;
        setName(name);
        setDaemon(true);
    }

    @Override
    public void run() {
        synchronized (eventGenerator) {}
        while (true) {
            fireEvent();
            try {
                sleep((long) (Math.random() * MAX_DELAY));
            } catch (InterruptedException ignored) {}
        }
    }

    private void fireEvent() {
        int id = (int) (Math.random() * EventGenerator.N_HOSTS);
        Host host = eventGenerator.hosts[id];
        if (connectedHosts.contains(host)) {
            endEvent(host);
        } else {
            startEvent(host);
        }
    }

    private void startEvent(Host host) {
        eventGenerator.controller.receive(new Event(getName(), host.getName(), Event.Type.START));
        connectedHosts.add(host);
    }

    private void endEvent(Host host) {
        try {
            sleep(MIN_CONNECTION_TIME);
        } catch (InterruptedException ignored) {}
        eventGenerator.controller.receive(new Event(getName(), host.getName(), Event.Type.END));
        connectedHosts.remove(host);
    }

}
