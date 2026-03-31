package com.openlab.lab11;

import java.util.Arrays;

public class Lab11 {
    // Stubs for 7 problems. Fill with actual solutions.
    public static void solve1(String[] inputs) {
        throw new UnsupportedOperationException("solve1 not implemented yet");
    }

    public static void solve2(String[] inputs) {
        throw new UnsupportedOperationException("solve2 not implemented yet");
    }

    public static void solve3(String[] inputs) {
        throw new UnsupportedOperationException("solve3 not implemented yet");
    }

    public static void solve4(String[] inputs) {
        throw new UnsupportedOperationException("solve4 not implemented yet");
    }

    public static void solve5(String[] inputs) {
        throw new UnsupportedOperationException("solve5 not implemented yet");
    }

    public static void solve6(String[] inputs) {
        throw new UnsupportedOperationException("solve6 not implemented yet");
    }

    public static void solve7(String[] inputs) {
        throw new UnsupportedOperationException("solve7 not implemented yet");
    }

    // Simple CLI runner: first arg selects problem 1-7, rest are inputs
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: Lab11 <problemNumber 1-7> [inputs...] ");
            return;
        }
        int idx = Integer.parseInt(args[0]);
        String[] rest = Arrays.copyOfRange(args, 1, args.length);
        switch (idx) {
            case 1:
                solve1(rest);
                break;
            case 2:
                solve2(rest);
                break;
            case 3:
                solve3(rest);
                break;
            case 4:
                solve4(rest);
                break;
            case 5:
                solve5(rest);
                break;
            case 6:
                solve6(rest);
                break;
            case 7:
                solve7(rest);
                break;
            default:
                System.out.println("Invalid problem number. Use 1-7.");
        }
    }
}
