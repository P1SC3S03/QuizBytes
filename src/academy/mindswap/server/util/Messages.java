package academy.mindswap.server.util;

public class Messages {

    //Errors/Problems
    public static final String CLIENT_ERROR = "Something went wrong with this player connection.\n";
    public static final String SOCKET_ERROR = "Something went wrong with the Server. Connection closing...\n";
    public static final String CONNECTION_CLOSED = "Connection closed...\n";
    public static final String INVALID_OPERATION_OR_COMMAND = "Invalid option or command.\n";

    //Normal Messages
    public static final String DEFAULT_NAME = "Player: ";
    public static final String CORRECT_ANSWER = "Correct!\n";
    public static final String INCORRECT_ANSWER = "Incorrect.\n";
    public static final String NO_TOP_PLAYERS = "There are no top players yet. You are lucky! ;) \n";
    public static final String WAIT_FOR_ANOTHER_PLAYER = "Please wait for another player to join you.\n";
    public static final String HIT_ME = "Winner winner chicken dinner! I knew you hold read the commands! " +
            "Time for it to pay off!\n";
    public static final String STEAL = "Oh naughty naughty, but it paid off ^^\n";
    public static final String GREEDY = "A poor person isn’t who has little, but who needs a lot...\n";


    public static final String MAIN_MENU = """
            \nMAIN MENU:
            1 -> Singleplayer
            2 -> Multiplayer
            3 -> Commands
            4 -> MultiPlayer: Top 1O Scores
            0 -> Exit\n""";

    //REVISE!!!!!!!!!!!!!
    public static final String COMMAND_LIST = """
            \nCOMMAND LIST:
            /quit -> Quits the game
            /menu -> Go back to main menu
            /hit me -> [MultiPlayer Only] If you feel extra lucky
            /steal -> Steal someone's points
            /support -> Get emotional support from 'Byty' the Server\n
            """;

    public static final String SINGLEPLAYER_MENU = """
            \nMAIN MENU:
            1 -> Easy
            2 -> Hard\n
            """;

    //Fun Messages
    public static final String WELCOME_SERVER = "\n\n\n\n\n\n\n\n\n" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|-----------(Hello! I'm Byty the Server! Welcome to the funnest day of your life!\n" +
            "/_____/##|           I strongly advise you to check the game commands before you start playing...\n" +
            "|     |##/           They might 'interest' you [If you know what I mean ^^])\n" +
            "|     |#/\n"+
            "|_____|/ \n\n";

    public static final String READ_RULES = "" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|--------(REALLY?! DIDN'T I TELL YOU TO READ THE RULES & COMMANDS??!!)\n" +
            "/_____/##|\n"+
            "|     |##/\n"+
            "|     |#/\n"+
            "|_____|/ \n";

    public static final String EMOTIONAL_SUPPORT = "" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|--------(If at first you don't succeed, call it Version 1.0)\n" +
            "/_____/##|\n"+
            "|     |##/\n"+
            "|     |#/\n"+
            "|_____|/ \n";

    public static final String LEVEL_CHANGE = "" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|--------(Congrats! You are smarter than a 6 year old... Lets spice things up, shall we? ^^)\n" +
            "/_____/##|\n"+
            "|     |##/\n"+
            "|     |#/\n"+
            "|_____|/ \n";


        public static final String INSANE_MODE =  "" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|--------(Hahahaha! Did you really think this was going to be a 'normal quizz??')\n" +
            "/_____/##|\n"+
            "|     |##/\n"+
            "|     |#/\n"+
            "|_____|/ \n";

    public static final String GOODBYE_MESSAGE = "" +
            "   ______ \n" +
            "  /     /| \n" +
            " /     /#|--------(Pfff... I knew from the start you were a quitter... BUUUUUUUHHH!!)\n" +
            "/_____/##|\n"+
            "|     |##/\n"+
            "|     |#/\n"+
            "|_____|/ \n";
}

/*"You are now the top player.But... Are you happy with your miserable result?"
"Try answering now smart pants"
"You are now the top player! But... Are you happy with your life?"*/

