package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String,
            ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if (req.method().equals("POST")) {
            String text = add(req);
            rsl = new Resp(text, 200);
        } else if (req.method().equals("GET")) {
            String message = getMessage(req);
            rsl = new Resp(message, 200);
        }
        return rsl;
    }

    public String add(Req req) {
        String rsl;
        String[] themeId = req.theme().split("/");
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>> map = queue.get(themeId[0]);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            this.queue.putIfAbsent(themeId[0], map);
            rsl = "post was created";
        } else {
            int count = 0;
            for (Integer integer : map.keySet()) {
                map.get(integer).add(req.text());
                count++;
            }
            rsl = String.format("message sent to %s subscribers", count);
        }
        return rsl;
    }

    public String getMessage(Req req) {
        String rsl;
        String[] themeId = req.theme().split("/");
        ConcurrentHashMap<Integer, ConcurrentLinkedQueue<String>> map = queue.get(themeId[0]);
        Integer id = Integer.valueOf(req.theme().split("/")[1]);
        if (map == null) {
            rsl = "post not found";
        } else {
            ConcurrentLinkedQueue<String> messages = map.get(id);
            if (messages == null) {
                map.putIfAbsent(id, new ConcurrentLinkedQueue<>());
                rsl = "was subscribed";
            } else {
                String message = messages.poll();
                if (message == null) {
                    rsl = "queue is empty";
                } else {
                    rsl = message;
                }
            }
        }
        return rsl;
    }
}
