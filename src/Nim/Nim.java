package Nim;
// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t
//

import java.util.Arrays;
import java.util.Random;


public class Nim implements NimGame {
    private Random r = new Random();
    private int[] rows;
    public static Nim of(int... rows) {
        return new Nim(rows);
    }
    private Nim(int... rows) {
        assert rows.length >= 1;
        assert Arrays.stream(rows).allMatch(n -> n >= 0);
        this.rows = Arrays.copyOf(rows, rows.length);
    }
    private Nim play(Move m) {
        assert !isGameOver();
        assert m.row < rows.length && m.number <= rows[m.row];
        Nim nim = Nim.of(rows);
        nim.rows[m.row] -= m.number;
        return nim;
    }

    public Nim play(Move... moves) {
        Nim nim = this;
        for(Move m : moves) nim = nim.play(m);
        return nim;
    }
    public Move randomMove() {
        assert !isGameOver();
        int row;
        do {
            row = r.nextInt(rows.length);
        } while (rows[row] == 0);
        int number = r.nextInt(rows[row]) + 1;
        return Move.of(row, number);
    }
    public Move bestMove() {
        assert !isGameOver();
        if (!NimGame.isWinning(rows)) return randomMove();
        Move m;
        do {
            m = randomMove();
        } while(NimGame.isWinning(play(m).rows));
        return m;
    }
    public boolean isGameOver() {
        return Arrays.stream(rows).allMatch(n -> n == 0);
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int n : rows) s.append("\n").append("I ".repeat(n));
        return s.toString();
    }
}/*

    Nim.Nim nim = Nim.Nim.of(2,3,4);
assert nim != nim.play(Nim.Move.of(1,2)) : "Return a new Nim.Nim instance";

        int[] randomSetup(int... maxN) {
        Random r = new Random();
        int[] rows = new int[maxN.length];
        for(int i = 0; i < maxN.length; i++) {
        rows[i] = r.nextInt(maxN[i]) + 1;
        }
        return rows;
        }

        ArrayList<Nim.Move> autoplay(Nim.NimGame nim) {
        ArrayList<Nim.Move> moves = new ArrayList<>();
        while (!nim.isGameOver()) {
        Nim.Move m = nim.bestMove();
        moves.add(m);
        nim = nim.play(m);
        }
        return moves;
        }

        boolean simulateGame(int... maxN) {
        Nim.NimGame nim = Nim.Nim.of(randomSetup(maxN));
        // System.out.println(nim);
        // System.out.println((Nim.NimGame.isWinning(nim.rows) ? "first" : "second") + " to win");
        ArrayList<Nim.Move> moves = autoplay(nim);
        // System.out.println(moves);
        return (Nim.NimGame.isWinning(nim.rows) && (moves.size() % 2) == 1) ||
        (!Nim.NimGame.isWinning(nim.rows) && (moves.size() % 2) == 0);
        }

        assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,5));
        assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,6,8));*/

/* // Beispielhaftes Spiel über JShell

jshell> Nim.Nim n = Nim.Nim.of(2,3,4)
n ==>
I I
I I I
I I I I

jshell> n = n.play(n.bestMove())
n ==>
I I
I I I
I

jshell> n = n.play(Nim.Move.of(2,1))
n ==>
I I
I I I


jshell> n = n.play(n.bestMove())
n ==>
I I
I I


jshell> n = n.play(Nim.Move.of(1,1))
n ==>
I I
I


jshell> n = n.play(n.bestMove())
n ==>
I
I


jshell> n = n.play(Nim.Move.of(1,1))
n ==>
I



jshell> n = n.play(n.bestMove())
n ==>




jshell> n.isGameOver()
$25 ==> true
*/