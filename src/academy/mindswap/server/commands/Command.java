package academy.mindswap.server.commands;

public enum Command {

    SINGLE_PLAYER_MODE("1", new SingleplayerCommand()),
    MULTI_PLAYER_MODE("2", new MultiplayerCommand());

    private String message;
    private CommandHandler handler;

    Command(String message, CommandHandler handler) {
        this.message = message;
        this.handler = handler;
    }

    public static Command getCommandFromMessage(String message) {
        for (Command command : values()) {
            if (message.equals(command.message)) {
                return command;
            }
        }
        return null;
    }

    public CommandHandler getHandler() {
        return handler;
    }

}
