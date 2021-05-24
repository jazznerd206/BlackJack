import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import static java.lang.Math.*;

class Player {

    String name;
    int bank;
    boolean dealer;
    Hand currentHand;
    int currentBet;

    public Player(String name) {
        this.name = name;
        this.bank = 0;
        this.dealer = false;
        this.currentHand = null;
        this.currentBet = 0;
    }

    public boolean wager(int amount) {
        if (amount > this.bank) {
            System.out.println("Not enough dough");
            return false;
        }
        this.bank -= amount;
        this.currentBet += amount;
        return true;
    }
}

class Hand {

    ArrayList<Integer> hand;

    public Hand() {
        this.hand = new ArrayList<Integer>();
    }
}

class BlackJack {

    // constructor variable
    int players;
    Player[] table;
    boolean[] deck;
    int cardsRemaining;
    int handsPlayed;
    int ante;
    boolean handActive;
    int openHands;
    boolean openBet;
    int pot;
    int minBet;

    // public constructor
    public BlackJack(int players) {
        this.table = new Player[players];
        this.players = players;
        this.deck = new boolean[52];
        this.cardsRemaining = 52;
        this.handsPlayed = 0;
        this.handActive = false;
        this.openHands = 0;
        this.openBet = false;
        this.pot = 0;
        this.ante = 0;
        this.minBet = 1;
    }

    public void gatherMinimumBet(Scanner scanner) {
        Scanner s = scanner;
        for (Player p : table) {
            System.out.println(p.name + ": Minimum bet is " + this.minBet + ". You have " + p.bank + ". Your wager?");
            int bet = s.nextInt();
            s.nextLine();
            if (bet >= this.minBet && bet <= p.bank)
                p.wager(bet);
        }
        this.openBetting();
    }

    public void dealAfterBet(Scanner scanner) {
        for (Player p : table) {
            ArrayList<Integer> l = p.currentHand.hand;
            if (l.size() == 0) {
                continue;
            }
            int handValue = 0;
            for (int i = 0; i < l.size(); i++) {
                int[] card = cardConvertor(l.get(i));
                if (card[1] > 9) {
                    handValue += 10;
                } else {
                    handValue += card[1];
                }
            }
            if (handValue > 21)
                l = null;
            if (p.dealer == true && handValue < 17) {
                l.add(dealToHand());
            }
            if (p.dealer == false) {
                System.out.println(p.name + ": Do you want another card... y/n?");
                String answer = scanner.nextLine();
                if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
                    l.add(dealToHand());
                }
            }
        }
        this.openBet = false;
    }

    public void open() {
        this.handsPlayed++;
        this.handActive = true;
        setDealer();
        for (Player p : table) {
            this.openHands++;
            Hand h = new Hand();
            // if (p.dealer != true) {
            // p.bank -= ante;
            // this.pot += ante;
            // }
            int card1 = dealToHand();
            int card2 = dealToHand();
            h.hand.add(card1);
            h.hand.add(card2);
            p.currentHand = h;
            this.openHands++;
        }
    }

    public int dealToHand() {
        Random rand = new Random();
        int int1 = rand.nextInt(52);
        if (this.deck[int1] == true) {
            int1 = rand.nextInt(52);
        }
        this.deck[int1] = true;
        this.cardsRemaining--;
        return int1;
    }

    // GAME HELPERS
    public int[] cardConvertor(int index) {
        int suit = index / 13;
        int face = index % 13;
        int[] card = { suit, face + 1 };
        return card;
    }

    private void setDealer() {
        int index = this.players % this.handsPlayed;
        // System.out.println(index);
        for (int i = 0; i < this.table.length - 1; i++) {
            if (i == index) {
                this.table[i].dealer = true;
            } else {
                this.table[i].dealer = false;
            }
        }
    }

    private int getAnte() {
        int min = Integer.MAX_VALUE;
        for (Player p : table) {
            if (p.bank < min)
                min = p.bank;
        }
        return (int) Math.round(min * .05) == 0 ? 1 : (int) Math.round(min * .05);
    }

    public void openBetting() {
        this.openBet = true;
    }

    public void nextHand() {
        this.handActive = false;
    }

    // PRINT HELPERS
    public void printHands() {
        System.out.println(" --> CURRENT HANDS <--");
        for (Player p : this.table) {
            ArrayList<Integer> l = p.currentHand.hand;
            if (p.dealer == true) {
                System.out.print("DEALER -> ");
            }
            System.out.print("|");
            for (int i = 0; i < l.size(); i++) {
                int[] card = cardConvertor(l.get(i));
                System.out.print(" ");
                System.out.print(card[0] + " " + card[1]);
                System.out.print(" |");
            }
            System.out.println();
        }
    }

    public void printBank() {
        for (Player p : this.table) {
            System.out.print("--> Player: " + p.name + ", ");
            System.out.print("Bank: " + p.bank + " <--");
            System.out.print("Current Bet: " + p.currentBet + " <--");
            if (p.currentHand != null) {
                ArrayList<Integer> l = p.currentHand.hand;
                if (p.dealer == true) {
                    System.out.print("DEALER -> ");
                }
                System.out.print("|");
                for (int i = 0; i < l.size(); i++) {
                    int[] card = cardConvertor(l.get(i));
                    System.out.print(" ");
                    System.out.print(card[0] + " " + card[1]);
                    System.out.print(" |");
                }
            }
            System.out.println();
        }
    }

    public void printDeck() {
        for (int i = 0; i < this.deck.length; i++) {
            int row = i / 13;
            int col = (i % 13) + 1;
            System.out.print("| ");
            if (row == 0) {
                System.out.print(" | H " + col + " " + this.deck[i]);
            }
            if (row == 1) {
                System.out.print(" | D " + col + " " + this.deck[i]);
            }
            if (row == 2) {
                System.out.print(" | C " + col + " " + this.deck[i]);
            }
            if (row == 3) {
                System.out.print(" | S " + col + " " + this.deck[i]);
            }
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        // GAME CONSTRUCTOR

        // get player count
        System.out.println("How many players are getting these hands?");
        int playerCount = s.nextInt();
        s.nextLine();

        // initialize game object
        BlackJack bj = new BlackJack(playerCount);

        // add players to table
        for (int i = 0; i < playerCount; i++) {
            System.out.println("Player " + (i + 1) + " name: ");
            String name = s.nextLine();
            Player newPlayer = new Player(name);
            System.out.println("Player " + (i + 1) + " bank: ");
            int bank = s.nextInt();
            s.nextLine();
            newPlayer.bank = bank;
            bj.table[i] = newPlayer;
            bj.players++;
        }

        // set table rules (ante, first dealer, print bank)
        // bj.ante = bj.getAnte();
        bj.printBank();

        // game loop
        while (bj.handActive == false) {
            bj.gatherMinimumBet(s);
            bj.open();
        }
        bj.printBank();
        while (bj.openHands > 1 && bj.openBet == true) {
            bj.dealAfterBet(s);
        }
        bj.printBank();
        s.close();
    }
}