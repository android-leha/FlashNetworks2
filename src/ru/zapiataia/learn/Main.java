package ru.zapiataia.learn;

import ru.zapiataia.learn.events.EventController;
import ru.zapiataia.learn.logger.LoggerController;
import ru.zapiataia.learn.source.EventGenerator;

import java.io.IOException;

public class Main {


    public static void main(String[] args) {

        try (LoggerController logger = new LoggerController()) {

            EventController eventController = new EventController(logger);
            long start = System.currentTimeMillis();
            new EventGenerator(eventController);

            System.out.println("Press enter to STOP");

            System.in.read();

            long duration = (System.currentTimeMillis() - start) / 1000;
            System.out.println("Speed is ~"
                    + (eventController.received.get() / duration)
                    + " msg/sec");
            System.out.println(eventController.lost.get() + " lost packets");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
