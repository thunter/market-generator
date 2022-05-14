package com.whipitupitude.generator;

import umontreal.ssj.rng.MRG32k3a;
import umontreal.ssj.randvar.NormalGen;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Market m = new Market(5);

        System.out.println(m);
        Trade t;
        for (int i = 0; i <= 1000; i++) {
            t = m.getEvent();
            // System.out.println("Got event for: " + t);
            // System.out.println(t.symbol() + "," + t.price());
            System.out.println(m.stocks.get(0).lastPrice + "," + m.stocks.get(1).lastPrice + ","
                    + m.stocks.get(2).lastPrice + "," + m.stocks.get(3).lastPrice + "," + m.stocks.get(4).lastPrice);
        }

    }

    public static void event() {
        Random rand = new Random();
        long rngSeed = rand.nextLong() % 4294944443L;
        long[] packageSeed = { rngSeed, rngSeed, rngSeed, rngSeed, rngSeed, rngSeed };
        MRG32k3a.setPackageSeed(packageSeed);
        MRG32k3a stream = new MRG32k3a();
        NormalGen randNormal = new NormalGen(stream, 0, .3);

        System.out.println("Hello World!");
        int periods = 1000;

    }

}
