package org.example;

import java.sql.Array;
import java.util.*;

public class PokerHandChecker {

    public static String getWinner(String hand) {
        String winner = "";
        while (winner == "") {

            int Player1Score = 0;
            int Player1Rank = 0;
            int Player2Score = 0;
            int Player2Rank = 0;

            String[] HandArr = hand.split(" ");

            if (HandArr.length < 10) {
                System.out.println("Wrong number of cards dealt");
                winner = "Tie";
                break;
            }

//            Lists for the cards: numerical list for number, the same list but to be sorted later

            ArrayList<Integer> player1Num = new ArrayList<Integer>();
            ArrayList<Character> player1Suit = new ArrayList<Character>();
            ArrayList<Integer> player2Num = new ArrayList<Integer>();
            ArrayList<Character> player2Suit = new ArrayList<Character>();

            for (int i = 0; i < HandArr.length; i++) {
                if (i < 5) {
                    player1Num.add(cardValue(HandArr[i].charAt(0)));
                    player1Suit.add(HandArr[i].charAt(1));
                } else {
                    player2Num.add(cardValue(HandArr[i].charAt(0)));
                    player2Suit.add(HandArr[i].charAt(1));
                }
            }

            ArrayList<Integer> player1NumSorted = new ArrayList<Integer>(player1Num);
            Collections.sort(player1NumSorted, Collections.<Integer>reverseOrder());
            ArrayList<Integer> player2NumSorted = new ArrayList<Integer>(player2Num);
            Collections.sort(player2NumSorted, Collections.<Integer>reverseOrder());

            Set<Integer> player1NumSet = new HashSet<Integer>(player1Num);
            Set<Character> player1SuitSet = new HashSet<Character>(player1Suit);
            Set<Integer> player2NumSet = new HashSet<Integer>(player2Num);
            Set<Character> player2SuitSet = new HashSet<Character>(player2Suit);

//            System.out.println(player1Num);
//            System.out.println(player1NumSorted);
//            System.out.println(player2NumSorted);
//            System.out.println(player1NumSet);
//            System.out.println(player1Suit);
//            System.out.println(player1SuitSet);
//            System.out.println();
//            System.out.println(player2Num);
//            System.out.println(player2NumSet);
//            System.out.println(player2Suit);
//            System.out.println(player2SuitSet);
//            System.out.println();

//            Check non-paired score first
            Player1Score = nonPairedScore(player1NumSorted, player1NumSet, player1SuitSet);
            Player2Score = nonPairedScore(player2NumSorted, player2NumSet, player2SuitSet);

//            Check for Royal Flush and stop game if found

            if (Player1Score == 10 && Player2Score == 10) winner = "Tie";
            else if (Player1Score == 10) winner = "Player 1";
            else if (Player2Score == 10) winner = "Player 2";

            ArrayList<Integer[]> player1Pairs = new ArrayList<Integer[]>();
            ArrayList<Integer[]> player2Pairs = new ArrayList<Integer[]>();

            if (Player1Score == 0) {
                player1Pairs = paired(player1NumSorted, player1NumSet);
                Player1Score = pairedScore(player1Pairs);
            }
            if (Player2Score == 0) {
                player2Pairs = paired(player2NumSorted, player2NumSet);
                Player2Score = pairedScore(player2Pairs);
            }

//            System.out.println(Player1Score);
//            System.out.println(Player2Score);

//            Simple score check to determine winner if there is no tie
            if (Player1Score > Player2Score) winner = "Player 1";
            else if (Player1Score < Player2Score) winner = "Player 2";

//            Tie check for non-paired cards

            else if (Player1Score == Player2Score) {
                switch (Player1Score) {
                    case 1:
                        if (player1NumSorted.get(0) > player2NumSorted.get(0)) winner = "Player 1";
                        else if (player1NumSorted.get(0) < player2NumSorted.get(0)) winner = "Player 2";
                        else if (player1NumSorted.get(0) == player2NumSorted.get(0)) winner = "Tie";
                        break;
                    case 2:
                    case 4:
                        if (player1Pairs.get(0)[1] > player2Pairs.get(0)[1]) winner = "Player 1";
                        else if (player1Pairs.get(0)[1] < player2Pairs.get(0)[1]) winner = "Player 2";
                        else if (player1Pairs.get(0)[1] == player2Pairs.get(0)[1]) {
                            deleteRemaining(player1NumSorted, player1Pairs);
                            deleteRemaining(player2NumSorted, player2Pairs);
                            winner = pairedTieBreaker(player1NumSorted, player2NumSorted);
                        }
                        break;
                    case 3:
                        int p1 = 0;
                        int p2 = 0;
                        deleteRemaining(player1NumSorted, player1Pairs);
                        deleteRemaining(player2NumSorted, player2Pairs);
                        for (int i = 0; i < player1Pairs.size(); i++) {
                            if (player1Pairs.get(0)[1] > player2Pairs.get(0)[1]) p1++;
                            else if (player1Pairs.get(0)[1] < player2Pairs.get(0)[1]) p2++;
                        }
                        if (p1 > p2) winner = "Player 1";
                        else if (p1 < p2) winner = "Player 2";
                        else if (p1 == p2) winner = pairedTieBreaker(player1NumSorted, player2NumSorted);
                        break;
                    case 7:
                        if (player1Pairs.get(0)[1] > player2Pairs.get(0)[1]) winner = "Player 1";
                        else if (player1Pairs.get(0)[1] < player2Pairs.get(0)[1]) winner = "Player 2";
                        else if (player1Pairs.get(0)[1] == player2Pairs.get(0)[1]) {
                            if (player1Pairs.get(1)[1] > player2Pairs.get(1)[1]) winner = "Player 1";
                            else if (player1Pairs.get(1)[1] < player2Pairs.get(1)[1]) winner = "Player 2";
                            else if (player1Pairs.get(1)[1] == player2Pairs.get(1)[1]) winner = "Tie";
                        }
                        break;
                    case 5:
                    case 6:
                    case 9:
                        if (player1Num.get(0) > player2Num.get(0)) winner = "Player 1";
                        else if (player1NumSorted.get(0) < player2NumSorted.get(0)) winner = "Player 2";
                        else if (player1NumSorted.get(0) == player2NumSorted.get(0)) winner = "Tie";
                        break;
                    case 10:
                        winner = "Tie";
                }
            }
//            Check for paired cards
        }
        return winner;
    }

    private static int cardValue(Character cardNum) {
        int value = 0;
            switch (cardNum) {
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    value = Character.getNumericValue(cardNum);
                    break;
                case 'T':
                    value = 10;
                    break;
                case 'J':
                    value = 11;
                    break;
                case 'Q':
                    value = 12;
                    break;
                case 'K':
                    value = 13;
                    break;
                case 'A':
                    value = 14;
                    break;
                default:
                    System.out.println("Some kind of error");
            }
        return value;
    }

    private static boolean RoyalFlush(Set<Integer> playerNums, Set<Character> playerSuitSet) {
        if (playerNums.contains(10)&&
                playerNums.contains(11) &&
                playerNums.contains(12) &&
                playerNums.contains(13) &&
                playerNums.contains(14) &&
                playerSuitSet.size() == 1) {
    //        System.out.println("Royal Flush");
            return true;
        }
        else return false;
    }

    private static boolean Flush(Set<Character> playerSuits) {
        if (playerSuits.size() == 1) {
//        System.out.println("Flush");
            return true;
        }
        else return false;
    }

    private static boolean Straight(List<Integer> playerNumSorted) {
//        Collections.sort(playerNumSorted, Collections.<Integer>reverseOrder());
//        System.out.println(playerNum);
//        ListIterator<Integer> iter = playerNum.listIterator(1);
        boolean result = true;

        while (result) {
            for (int i = 1; i < playerNumSorted.size(); i++) {
                if ((playerNumSorted.get(i - 1) - playerNumSorted.get(i)) == 1) result = true;
                else {
                    result = false;
                    break;
                }
            }
            break;
        }
        return result;
    }

    private static ArrayList<Integer[]> paired(List<Integer> playerNumSorted, Set<Integer> playerNumSet) {
        ArrayList<Integer[]> pairs = new ArrayList<Integer[]>();
        if (playerNumSet.size() < 5) {
            for (Integer i : playerNumSet) {
                int freq = Collections.frequency(playerNumSorted, i);
                if (freq > 1) pairs.add(new Integer[]{Collections.frequency(playerNumSorted, i), i});
            }
        }
        if (pairs.size() > 1) {
            if (pairs.get(0)[0] != pairs.get(1)[0]) {
                Collections.sort(pairs, Collections.<Integer[]>reverseOrder(
                        new Comparator<Integer[]>() {
                            @Override
                            public int compare(Integer[] o1, Integer[] o2) {
                                return o1[0].compareTo(o2[0]);
                            }
                        }
                ));
            } else {
                Collections.sort(pairs, Collections.<Integer[]>reverseOrder(
                        new Comparator<Integer[]>() {
                            @Override
                            public int compare(Integer[] o1, Integer[] o2) {
                                return o1[1].compareTo(o2[1]);
                            }
                        }
                ));
            }
        }

//        for (int i = 0; i < pairs.size(); i++) {
//            System.out.println(pairs.get(i)[0] + " " + pairs.get(i)[1]);
//        }
//        System.out.println();
        return pairs;
    }

    private static int nonPairedScore (List<Integer> playerNumSorted, Set<Integer> playerNumSet, Set<Character> playerSuitSet) {
        int result = 0;
        if (RoyalFlush(playerNumSet, playerSuitSet)) result = 10;
        else if (Straight(playerNumSorted) && Flush(playerSuitSet)) result = 9;
        else if (Flush(playerSuitSet)) result = 6;
        else if (Straight(playerNumSorted)) result = 5;
        return result;
    }

    private static int pairedScore (ArrayList<Integer[]> playerPairs) {
        int result = 0;
        switch (playerPairs.size()) {
            case 0:
                result = 1;
                break;
            case 1:
                switch (playerPairs.get(0)[0]) {
                    case 2:
                        result = 2;
                        break;
                    case 3:
                        result = 4;
                        break;
                    case 4:
                        result = 8;
                        break;
                }
                break;
            case 2:
                switch (playerPairs.get(0)[0] + playerPairs.get(1)[0]) {
                    case 4:
                        result = 3;
                        break;
                    case 5:
                        result = 7;
                        break;
                }
                break;
        }
        return result;
    }

    private static void deleteRemaining (List<Integer> playerNumSorted, ArrayList<Integer[]> playerPairs) {
        for (int i = 0; i < playerPairs.size(); i++) {
            for (int j = 0; j < playerPairs.get(i)[0]; j++) {
//                playerNumSorted.remove(playerPairs.get(i)[1]);
                int finalI = i;
                playerNumSorted.removeIf(a -> a == playerPairs.get(finalI)[1]);
            }
        }
//        System.out.println("playerNumSorted " + playerNumSorted);
    }

    private static String pairedTieBreaker (ArrayList<Integer> player1NumSorted, ArrayList<Integer> player2NumSorted) {
        String winner = "";

        while (player1NumSorted.size() > 0 || player2NumSorted.size() > 0) {
            if (player1NumSorted.get(0) > player2NumSorted.get(0)) {
                winner = "Player 1";
                break;
            }
            else if (player1NumSorted.get(0) < player2NumSorted.get(0)) {
                winner = "Player 2";
                break;
            }
            else if (player1NumSorted.get(0) == player2NumSorted.get(0)) {
                winner = "Tie";
                player1NumSorted.remove(0);
                player2NumSorted.remove(0);
            }
        }
        return winner;
    }

}
