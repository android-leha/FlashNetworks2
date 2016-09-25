package ru.zapiataia.learn.events;


import ru.zapiataia.learn.connection.ConnData;

public class Event {

    public enum Type {
        START, END
    }

    private String source;
    private String destination;
    private Type type;

    private ConnData connData;

    public Event(String source, String destination, Type type) {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.connData = new ConnData();
    }

    public String getKey() {
        return source + "::" + destination;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Type getType() {
        return type;
    }

    public ConnData getConnData() {
        return connData;
    }
}
