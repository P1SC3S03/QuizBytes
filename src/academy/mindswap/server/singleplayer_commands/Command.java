package academy.mindswap.server.singleplayer_commands;

/**
 * Command Enum of the currently existing modes from the SinglePlayer
 */
public enum Command {

    EASY_MODE("1", new EasyDifficultyMode()),
    HARD_MODE("2", new HardDifficultyMode());

    private String userInput;
    private CommandHandler handler;

    /**
     * Constructor method for the Command Enum
     *
     * @param userInput - player input option from SinglePlayer menu.
     * @param handler   - interface type to create an object depending on the options.
     */
    Command(String userInput, CommandHandler handler) {
        this.userInput = userInput;
        this.handler = handler;
    }

    /**
     * Receives user input and checks if it exists in the enum.
     *
     * @param mode - User input string to be converted.
     * @return the command if existent in the Enumeration, otherwise returns null.
     */
    public static Command getCommandFromMode(String mode) {
        for (Command command : values()) {
            if (mode.equals(command.userInput)) {
                return command;
            }
        }
        return null;
    }

    /**
     * Getter for the chosen handler
     *
     * @return the handler
     */
    public CommandHandler getHandler() {
        return handler;
    }
}
