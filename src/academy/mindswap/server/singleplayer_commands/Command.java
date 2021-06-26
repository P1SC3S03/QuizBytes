package academy.mindswap.server.singleplayer_commands;

public enum Command {

    EASY_MODE("1", new EasyMode()),
    HARD_MODE("2", new HardMode());

    private String message;
    private CommandHandler handler;

    Command(String message, CommandHandler handler) {
        this.message = message;
        this.handler = handler;
    }

    public static Command getCommandFromMode(String mode) {
        for (Command command : values()) {
            if (mode.equals(command.message)) {
                return command;
            }
        }
        return null;
    }

    public CommandHandler getHandler() {
        return handler;
    }
}
