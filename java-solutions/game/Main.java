package game;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
           System.out.println("Welcome to the Hex tournament!");
           int cnt = askHowManyPlayers(in);
           List<Player> players = new ArrayList<>();
           for (int i : new Range(0, cnt)) {
               String nick;
               do {
                   System.out.println("Player no " + (i+1) + " nickname:");
                   nick = in.next();
               } while(!askIfSure(in));
               players.add(new HumanPlayer(in, nick));
           }
           Tournament tourn = new Tournament(players);
           for (Pair<Player, Player> pair : tourn) {
               int result = new TwoPlayerGame(
                   new HexBoard(11, 11),
                   pair.first,
                   pair.second
               ).play(false);
               switch (result) {
                   case 1:
                       System.out.println(pair.first + " won");
                       break;
                   case 2:
                       System.out.println(pair.second + " won");
                       break;
                   case 0:
                       System.out.println("Draw");
                       break;
                   default:
                       throw new AssertionError("Unknown result " + result);
               }
               tourn.regResult(pair, result);
           }
           System.out.println(tourn.getScores());
        } catch (NoSuchElementException e) {

        } finally {
            in.close();
        }
    }

    public static int askHowManyPlayers(Scanner in) {
        System.out.println("How many players?");
        int cnt = -1;
        while (true) {
            try {
                cnt = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Typed value is not an integer.");
                continue;
            } catch (NoSuchElementException e) {
                System.out.println("Ooops, input is exhausted");
                throw e;
            } finally {
                if (in.hasNextLine()) {
                    in.nextLine();
                }
            }
            if (cnt <= 1) {
                System.out.println("At least two players needed");
            } else {
                break;
            }
        }
        return cnt;
    }

    public static boolean askIfSure(Scanner in) {
        System.out.println("Are you sure? Y/N");
        try {
            while (true) {
                switch (in.next()) {
                case "Y":
                    return true;
                case "N":
                    return false;
                default:
                    continue;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Oops, input is exhausted");
            throw e;
        }
    }
}
