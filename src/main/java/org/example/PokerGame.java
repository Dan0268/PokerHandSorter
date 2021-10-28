package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.*;

/**
 * Hello world!
 *
 */
public class PokerGame
{


    public static void main( String[] args ) throws IOException {
        int Player1Score = 0;
        int Player2Score = 0;

//        Scanner scan = new Scanner(System.in);
//        String HandStr = scan.nextLine();
//        String winner = PokerHandChecker.getWinner(HandStr);

        BufferedReader HandsReader = new BufferedReader(new FileReader(args[0]));
        String hand = null;

        while ((hand = HandsReader.readLine()) != null){
            String winner = PokerHandChecker.getWinner(hand);
            switch (winner) {
                case "Player 1":
                    Player1Score++;
//                    System.out.println("Player 1 wins!");
                    break;
                case "Player 2":
                    Player2Score++;
//                    System.out.println("Player 2 wins!");
                    break;
                case "Tie":
//                    System.out.println("It's a Tie!");
                    break;
                default:
                    System.out.println("Something's wrong");
                    break;
            }

        }

        System.out.println("Player 1: " + Player1Score + " hands");
        System.out.println("Player 2: " + Player2Score + " hands");
    }
}
