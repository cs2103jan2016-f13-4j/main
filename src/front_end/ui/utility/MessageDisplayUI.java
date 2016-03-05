package front_end.ui.utility;

import front_end.ui.core.UserInterface;
import objects.Message;

import java.util.List;

/**
 * Created by maianhvu on 5/3/16.
 */
public class MessageDisplayUI extends UserInterface<List<Message>> {

    public static final String STRING_FLAG_MESSAGE_FORMAT = "[%1$s]: ";
    private static final String COLOR_ANSI_RESET = "\u001B[0m";
    private static final String COLOR_ANSI_RED = "\u001B[31m";
    private static final String COLOR_ANSI_YELLOW = "\u001B[33m";
    private static final String COLOR_ANSI_CYAN = "\u001B[36m";
    private static final String COLOR_MESSAGE_ERROR = COLOR_ANSI_RED;
    private static final String COLOR_MESSAGE_WARNING = COLOR_ANSI_YELLOW;
    private static final String COLOR_MESSAGE_INFO = COLOR_ANSI_CYAN;
    private static final String STRING_FLAG_ERROR = "ERROR";
    private static final String STRING_FLAG_WARNING = "WARNING";
    private static final String STRING_FLAG_INFO = "INFO";

    public MessageDisplayUI(List<Message> data) {
        super(data);
    }

    private static String colorString(String text, String color) {
        return String.format("%s%s%s", color, text, COLOR_ANSI_RESET);
    }

    @Override
    public void render() {
        super.render();
        for (Message message : this.getData()) {
            switch (message.getType()) {
                case ERROR:
                    this.displayError(message.getMessage());
                    break;
                case WARNING:
                    this.displayWarning(message.getMessage());
                    break;
                case INFO:
                    this.displayInfo(message.getMessage());
            }
        }
    }

    private void displayError(String error) {
        this.display(formatMessageFlag(STRING_FLAG_ERROR, COLOR_MESSAGE_ERROR));
        this.displayLine(error);
    }

    private void displayWarning(String warning) {
        this.display(formatMessageFlag(STRING_FLAG_WARNING, COLOR_MESSAGE_WARNING));
        this.displayLine(warning);
    }

    private void displayInfo(String info) {
        this.display(formatMessageFlag(STRING_FLAG_INFO, COLOR_MESSAGE_INFO));
        this.displayLine(info);
    }

    private String formatMessageFlag(String flag) {
        return String.format(STRING_FLAG_MESSAGE_FORMAT, flag);
    }

    private String formatMessageFlag(String flag, String color) {
        return formatMessageFlag(colorString(flag, color));

    }
}
