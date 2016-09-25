package ru.zapiataia.learn.connection;


import ru.zapiataia.learn.events.Event;

class ExtConnData {

    String sourceHostName;
    String destinationHostName;
    ConnData connData;

    ExtConnData(Event event) {
        sourceHostName = event.getSource();
        destinationHostName = event.getDestination();
        connData = event.getConnData();
    }

}
