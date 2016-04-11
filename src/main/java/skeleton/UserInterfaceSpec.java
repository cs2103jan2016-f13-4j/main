package skeleton;

import ui.view.View;

import java.util.function.Function;

/**
 * UserInterface is a singleton class which construct the General User Interface structure and define most
 * of the components behavior.
 *
 * The only changing element of the UserInterface class is the viewWrapper which display the
 * view constructed from the View object. Other than that, the stage and its other children stage are all persistent.
 *
 * the following interface define the behavior of the class
 * @@author A0124772E
 */
public interface UserInterfaceSpec {

    /**
     * This method is called when the GUI is first set up and the component is linked to the code.
     *
     * Should only be called once, when the UserInterface is first instantiated
     */
    void initialize();
    /**
     * this method set the behavior for the commandInput Box when receiving input from the user
     *
     * @param commandInputHandler the function that defines the actions taken after receiving input
     */
    void setOnCommandInputHandler(Function<String, Void> commandInputHandler);
    /**
     * This method is called when the GUI is going to be displayed. initialize() method should be called before hand
     *
     * Should only be called once, when the UserInterface is first instantiated
     */
    void show();

    /***
     * the render() method is called when you want to update the display in the main container.
     *
     * @param view View object which component is going to be displayed
     */
    void render(View view);

    /***
     * This method will update the notification message following the given input parameter, and play
     * the animation to bring the notification bar on-screen for a given duration.
     *
     * @param title message to be displayed in the notification bar
     */
    void showNotification(String title);

    /***
     *
     */
    void cleanUp();

    void setHeaderTitle(String title);
}
