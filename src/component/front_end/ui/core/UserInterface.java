package component.front_end.ui.core;

import component.front_end.ui.CommandPromptView;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Created by maianhvu on 6/3/16.
 */
public class UserInterface extends UserInterfaceSpec {

    private static final InputStream STREAM_INPUT_DEFAULT = System.in;
    private static final PrintStream STREAM_OUTPUT_DEFAULT = System.out;
    private static final CommandPromptView VIEW_COMMAND_PROMPT = new CommandPromptView();

    private final Scanner _inputScanner;
    private final PrintStream _outputPrinter;

    public UserInterface() {
        this._inputScanner = new Scanner(STREAM_INPUT_DEFAULT);
        this._outputPrinter = STREAM_OUTPUT_DEFAULT;
    }

    @Override
    public void render(View viewToRender) {
        this._outputPrinter.print(viewToRender.getContent());
    }

    @Override
    public String queryInput() {
        this.render(VIEW_COMMAND_PROMPT);
        return this._inputScanner.nextLine();
    }
}
