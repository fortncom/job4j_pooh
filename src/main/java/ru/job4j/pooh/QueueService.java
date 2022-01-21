package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if (req.method().equals("POST")) {
            add(req);
            rsl = new Resp("message was posted", 200);
        } else if (req.method().equals("GET")) {
            String message = getMessage(req);
            rsl = new Resp(message, 200);
        }
        return rsl;
    }

    public void add(Req req) {
        ConcurrentLinkedQueue<String> messages = queue.get(req.theme());
        if (messages == null) {
            messages = new ConcurrentLinkedQueue<>();
            messages.add(req.text());
            queue.putIfAbsent(req.theme(), messages);
        } else {
            messages.add(req.text());
        }
    }

    public String getMessage(Req req) {
        String rsl = queue.get(req.theme()).poll();
        if (rsl == null) {
            rsl = "no messages";
        }
        return rsl;
    }
}
