package academy.mindswap.server.util;

public class Messages {

    //Errors/Problems
    public static final String CLIENT_ERROR = "Something went wrong with this player connection.\n";
    public static final String SOCKET_ERROR = "Something went wrong with the Server. Connection closing...\n";
    public static final String CONNECTION_CLOSED = "Connection closed...\n";
    public static final String INVALID_OPERATION_OR_COMMAND = "Invalid option or command.\n";

    //Normal Messages
    public static final String WELCOME_MESSAGE = "Hello! Welcome to the funnest day of your life! " +
            "I strongly advise you to check the game commands before you start playing... " +
            "They might 'interest' you [If you know what I mean ^^] Have fun! :) \n";

    public static final String GOODBYE_MESSAGE = "HAHAHA I knew from the start you were a quitter... BUUUUHHHH\n";
    public static final String DEFAULT_NAME = "Player: ";
    public static final String CORRECT_ANSWER = "Correct!\n";
    public static final String INCORRECT_ANSWER = "Incorrect.\n";
    public static final String NO_TOP_PLAYERS = "There are no top players yet. You are lucky! ;) \n";
    public static final String WAIT_FOR_ANOTHER_PLAYER = "Please wait for another player to join you.\n";
    public static final String INSANE_MODE = "Hahaha! Did you really think this was going to be a 'normal quizz??'";
    public static final String MAIN_MENU = """
            MAIN MENU:
            1 -> Singleplayer
            2 -> Multiplayer
            3 -> Commands
            4 -> Top 1O Scores
            0 -> Exit\n""";

    //REVISE!!!!!!!!!!!!!
    public static final String COMMAND_LIST = """
            COMMAND LIST:
            /quit -> Quits the game.
            /menu -> Go back to main menu.
            /freeze -> Freeze your opponent for a round.
            /steal -> Steal his points for the current round.
            /support -> Get emotional support from your BFF.
            \n""";

    public static final String SINGLEPLAYER_MENU = """
            MAIN MENU:
            1 -> Easy
            2 -> Hard
            """;
    //Fun Messages

    public static final String READ_RULES = "" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|--------(REALLY?! DIDN'T I TELL YOU TO READ THE RULES & COMMANDS??!!)\n" +
            "/_____/##|\n"+
            "|     |##/\n"+
            "|     |#/\n"+
            "|_____|/ \n";

    public static final String SLEEPING_SERVER = "" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|--------(ZZZZZZzzzzZZZZZzzzzzzzzz)\n" +
            "/_____/##|\n"+
            "|     |##/\n"+
            "|     |#/\n"+
            "|_____|/ \n";


    public static final String WELCOME_SERVER = "" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|-----------(Hello! Welcome to the funnest day of your life!\"\n" +
            "/_____/##|           I strongly advise you to check the game rules/key/commands before you start playing...\n" +
            "|     |##/           It/They might 'interest' you [If you know what I mean ^^])\n" +
            "|     |#/\n"+
            "|_____|/ \n";


    public static final String HOOKED = "  " +
            "          FISHKISSFISHKIS               \n" +
            "       SFISHKISSFISHKISSFISH            F\n" +
            "    ISHK   ISSFISHKISSFISHKISS         FI\n" +
            "  SHKISS   FISHKISSFISHKISSFISS       FIS\n" +
            "HKISSFISHKISSFISHKISSFISHKISSFISH    KISS\n" +
            "  FISHKISSFISHKISSFISHKISSFISHKISS  FISHK\n" +
            "      SSFISHKISSFISHKISSFISHKISSFISHKISSF\n" +
            "  ISHKISSFISHKISSFISHKISSFISHKISSF  ISHKI\n" +
            "SSFISHKISSFISHKISSFISHKISSFISHKIS    SFIS\n" +
            "  HKISSFISHKISSFISHKISSFISHKISS       FIS\n" +
            "    HKISSFISHKISSFISHKISSFISHK         IS\n" +
            "       SFISHKISSFISHKISSFISH            K\n" +
            "         ISSFISHKISSFISHK               ";
}
