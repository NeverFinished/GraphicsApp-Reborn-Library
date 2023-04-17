package pg2in;

import java.util.Arrays;
import java.util.Stack;

public class TowersOfHanoi {

    private final Stack<Integer>[] stacks = new Stack[3];
    private final int numDiscs;
    private StepCallback cb;

    public TowersOfHanoi(int numDiscs) {
        this.numDiscs = numDiscs;
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new Stack<>();
        }
        var left = stacks[0];
        for (int n = 0; n < numDiscs; n++) {
            int discNumber = numDiscs - n;
            left.push(discNumber);
        }
    }

    void printPeg(int row, Stack<Integer> stack) {
        if (stack.size() > row) {
            System.out.printf("%d", stack.get(row));
        } else {
            System.out.print("|");
        }
        System.out.print("\t\t");
    }

    void printHanoi() {
        System.out.println();
        for (int row = numDiscs - 1; row >= 0; row--) {
            System.out.print("\t");
            printPeg(row, stacks[0]);
            printPeg(row, stacks[1]);
            printPeg(row, stacks[2]);
            System.out.println();
        }
        System.out.println("#########################");
    }

    void hanoi(int n, Stack<Integer> from, Stack<Integer> to, Stack<Integer> tmp) {
        if (n == 1) { // Basisfall: Eine Scheibe
            to.push(from.pop());
            if (cb != null) {
                cb.step(to, Arrays.asList(stacks).indexOf(to));
            } else {
                printHanoi();
            }
        } else {
            hanoi(n - 1, from, tmp, to); // Alles außer die aktuelle Scheibe auf den Hilfsstapel legen...
            to.push(from.pop()); // "Eigene" Scheibe umlegen
            if (cb != null) {
                cb.step(to, Arrays.asList(stacks).indexOf(to));
            } else {
                printHanoi();
            }
            hanoi(n - 1, tmp, to, from); // ... und wieder zurück legen, auf die eigene Scheibe
        }
    }

    public static void main(String[] args) {
        new TowersOfHanoi(3).solve(null);
    }

    public void solve(StepCallback scb) {
        printHanoi();
        this.cb = scb;
        hanoi(numDiscs, stacks[0], stacks[2], stacks[1]);
    }

    public int getNumDiscs() {
        return numDiscs;
    }

    public static interface StepCallback {
        public void step(Stack<Integer> to, int toIndex);
    }
}
