package com.whipitupitude.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import umontreal.ssj.randvar.NormalGen;
import umontreal.ssj.rng.MRG32k3a;

public class Market {
    private int size; // no of elements in the market
    public List<Stock> stocks = new ArrayList<Stock>(); // list of stocks in the market
    Random rand = new Random();

    public Market(int size) {
        // Lets make it random!
        Random rand = new Random();
        long rngSeed = rand.nextLong() % 4294944443L;
        long[] packageSeed = { rngSeed, rngSeed, rngSeed, rngSeed, rngSeed, rngSeed };
        MRG32k3a.setPackageSeed(packageSeed);
        MRG32k3a stream = new MRG32k3a();
        NormalGen muNormal = new NormalGen(stream, 0, 1);
        NormalGen sigmaNormal = new NormalGen(stream, 0, 2);

        this.size = size;
        for (int i = 0; i <= size; i++) {
            stocks.add(new Stock(stream, muNormal, sigmaNormal));
        }
    }

    public Trade getEvent() {
        return stocks.get(rand.nextInt(size)).getTrade();
    }
}
