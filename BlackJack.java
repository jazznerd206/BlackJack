import java.util.Random;
import java.util.Scanner;

class Player {

    String name;
    int bank;
    boolean dealer;

    public Player(String name) {
        this.name = name;
        this.bank = 0;
        this.dealer = false;
    }

    public int wager(int amount) {
        if (amount > bank) {
            System.out.println("Not enough dough");
            return 0;
        }
        return amount;
    }

}

class Hand {

    int size;
    int[] hand;

    public Hand() {
        this.size = 0;
        this.hand = new int[5];
    }
}

class BlackJack {

    // constructor variable
    int players;
    Player[] people;
    boolean[] deck;
    int cardsRemaining;
    int handsPlayed;

    // public constructor
    public BlackJack(int players) {
        this.people = new Player[players];
        this.players = players;
        this.deck = new boolean[52];
        this.cardsRemaining = 52;
        this.handsPlayed = 0;
    }

    public void deal() {
        System.out.println("this is where we will make a shuffle check");
        Hand[] hands = new Hand[players];
        for (int i = 0; i < players; i++) {
            Hand h = new Hand();
            System.out.println(h);
            h.hand[0] = dealToHand();
            h.hand[1] = dealToHand();
            h.size += 2;
            hands[i] = h;
        }
        System.out.println(" -- Hands -- ");
        for (Hand x : hands) {
            System.out.print("|");
            for (int y : x.hand) {
                System.out.print(" " + y + " | ");
            }
            System.out.println();
        }
        return;
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

    public void addPlayer(Player p, int index) {
        this.people[index] = p;
    }

    private int getDealer() {
        return this.players % this.handsPlayed;
    }

    public void printBank() {
        for (Player p : people) {
            System.out.print("--> Player: " + p.name + ", ");
            System.out.print("Bank: " + p.bank + " <--");
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
        System.out.println("How many players are getting these hands?");
        int playerCount = s.nextInt();
        s.nextLine();
        BlackJack bj = new BlackJack(playerCount);
        for (int i = 0; i < playerCount; i++) {
            System.out.println("Player " + (i + 1) + " name: ");
            String name = s.nextLine();
            Player newPlayer = new Player(name);
            System.out.println("Player " + (i + 1) + " bank: ");
            int bank = s.nextInt();
            s.nextLine();
            newPlayer.bank = bank;
            bj.people[i] = newPlayer;
        }
        bj.printBank();
        // bj.printDeck();
        // bj.deal();
        s.close();
    }
}