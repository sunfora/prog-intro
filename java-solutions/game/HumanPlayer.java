package game;

import java.util.*;

public class HumanPlayer implements Player {

    private static int anon = 0;
    private final Scanner in;
    private final String nickname;

    public HumanPlayer(Scanner in, String nickname) {
        this.in = Objects.requireNonNull(in);
        this.nickname = Objects.requireNonNull(nickname);
    }

    public HumanPlayer(Scanner in) {
        this(in, "Unknown_" + ++anon);
    }

    @Override
    public Move makeMove(Position position) {
        Move move = null;
        while (true) {
            System.out.println();
            System.out.println("Current position");
            System.out.println(position);
            System.out.println(String.format(Messages.getEnter(), nickname));
            try {
                move = new Move(in.nextInt(), in.nextInt(), position.getTurn());
            } catch (InputMismatchException e) {
                System.out.println(Messages.getBadInput());
                continue;
            } catch (NoSuchElementException e) {
                System.out.println("Ooops, input is exhausted");
                throw e;
            } finally {
                if (in.hasNextLine()) {
                    in.nextLine();
                }
            }
            if ((null == move) || !position.isValid(move)) {
                System.out.println(Messages.getIncorrect());
                move = null;
            } else {
                break;
            }
        }
        return move;
    }

    public String toString() {
        return nickname;
    }
}

class Messages {

    public static final Random random = new Random();

    public static final String[] WRONG_INPUT = {
        "Pink Dolphin : Oh, no! These are not even numbers! Or they are rather too big, idk...",
        "Pink Dolphin : What's that? Ahhghh, once again you missed the buttons",
        "Pink Dolphin : Oh, come on, type it again...",
        "Pink Dolphin : Hey, you have these two big white spheres in your head, why don't you use them?",
        "Pink Dolphin : Letting the days go by, " +
        "let the water hold me down... Once in a life.. time you will make your move..."
    };

    public static final String[] ENTER = {
        ">>> And now is your turn! %s <<<",
        ">>> What's that? It's your turn! Put something somewhere! <<<",
        ">>> Welcome back! You play now, %s <<<",
        ">>> You are not tired yet, %s? Woah! That means it's your turn! <<<"
    };

    public static final String[] INCORRECT = {
        "Pink Dolphin : See you later!",
        "Pink Dolphin : Better luck next time!",
        "Pink Dolphin : That's not right, try again with another move!",
        "Pink Dolphin : I like you moving moving, but that's invalid!",
        "Pink Dolphin : If you are wondering, I like lemon juice!"
    };

    public static String getEnter() {
        return ENTER[random.nextInt(ENTER.length)];
    }

    public static String getIncorrect() {
        return System.lineSeparator() + INCORRECT[random.nextInt(INCORRECT.length)];
    }

    public static String getBadInput() {
        return System.lineSeparator() + WRONG_INPUT[random.nextInt(WRONG_INPUT.length)];
    }
}