package ru.zapiataia.learn.logger;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class LoggerController implements AutoCloseable {

    private static final int MEMORY_LOG_SIZE = 1000;
    private final PrintWriter out;
    private final BufferedWriter bw;
    private final FileWriter fw;

    class Saver extends Thread {

        final LoggerController controller;

        Saver(LoggerController controller) {
            this.controller = controller;
        }

        @Override
        public void run() {
            synchronized (controller) {
                while (true) {
                    try {
                        controller.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    saveOnDisk();
                }
            }

        }
    }


    private LinkedList<String> log = new LinkedList<>();

    private Saver saver = new Saver(this);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");

    public LoggerController() throws IOException {
        Path path = Paths.get("out.log");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        fw = new FileWriter("out.log", true);
        bw = new BufferedWriter(fw);
        out = new PrintWriter(bw);

        saver.start();
    }

    public synchronized void log(String data) {
        log.add(dateFormat.format(new Date()) + " " + data);
        if (log.size() > MEMORY_LOG_SIZE) {
            this.notify();
        }
    }

    private void saveOnDisk() {
        LinkedList<String> data;
        synchronized (this) {
            data = log;
            log = new LinkedList<>();
        }

        data.forEach(out::println);
    }

    public void close() throws IOException {
        saver.interrupt();
        saveOnDisk();
        out.close();
        bw.close();
        fw.close();
    }

}
