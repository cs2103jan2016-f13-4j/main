package component;

import skeleton.DispatcherSpec;

/**
 * Created by maianhvu on 8/3/16.
 */
public class Launcher {

    public static void main(String[] args) {
        DispatcherSpec application = new Dispatcher();
        application.pulse();
    }
}
