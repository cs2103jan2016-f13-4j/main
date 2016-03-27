package skeleton;

import ui.view.View;

import java.util.function.Function;

public interface UserInterfaceSpec {

    void initialize();

    void setOnCommandInputHandler(Function<String, Void> commandInputHandler);

    void show();

    void render(View view);

    void setHeader(String title);

    void cleanUp();
}
