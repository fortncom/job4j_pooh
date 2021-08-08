package ru.job4j.pooh;

public class Req {

    private final String method;
    private final String mode;
    private final String text;
    private final String theme;

    private Req(String method, String mode, String theme, String text) {
        this.method = method;
        this.mode = mode;
        this.theme = theme;
        this.text = text;
    }

    public static Req of(String content) {
        String[] lines = content.split(System.lineSeparator());
        return new Req(parseMethod(lines[0]),
                       parseMode(lines[0]),
                       parseTheme(lines[0]),
                       lines[lines.length - 1]);
    }

    private static String parseMethod(String firstLine) {
        String rsl = "";
        if (firstLine.contains("POST")) {
            rsl = "POST";
        } else if (firstLine.contains("GET")) {
            rsl = "GET";
        }
        return rsl;
    }

    private static String parseMode(String firstLine) {
        return firstLine.split("/")[1];
    }

    private static String parseTheme(String firstLine) {
        String[] s = firstLine.split("\\s+")[1].split("/");
        return s.length > 3 ? s[2].concat("/").concat(s[3]) : s[2];
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String text() {
        return text;
    }

    public String theme() {
        return theme;
    }
}
