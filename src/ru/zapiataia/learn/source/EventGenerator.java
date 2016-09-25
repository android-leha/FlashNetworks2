package ru.zapiataia.learn.source;


import ru.zapiataia.learn.events.EventController;

public class EventGenerator {

    static final int N_HOSTS = 6000;

    Host[] hosts = new Host[N_HOSTS];

    EventController controller;

    public EventGenerator(EventController controller) {
        synchronized (this) {
            this.controller = controller;
            for (int i = 0; i < N_HOSTS; i++) {
                hosts[i] = new Host(String.format("HOST-%05d", i), this);
                hosts[i].start();
            }
        }
    }

}
