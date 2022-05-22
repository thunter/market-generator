package com.whipitupitude.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.util.concurrent.RateLimiter;

import umontreal.ssj.randvar.NormalGen;
import umontreal.ssj.rng.MRG32k3a;

public class Market {
    private int size; // no of elements in the market
    public List<Stock> stocks = new ArrayList<Stock>(); // list of stocks in the market
    Random rand = new Random();

    private RateLimiter rl;

    public Market(int size, double rateLimit) {
        // Lets make it random!
        // This just picks an arbitary seed for the random number generator
        Random rand = new Random();
        long rngSeed = rand.nextLong() % 4294944443L;
        long[] packageSeed = { rngSeed, rngSeed, rngSeed, rngSeed, rngSeed, rngSeed };

        MRG32k3a.setPackageSeed(packageSeed);
        MRG32k3a stream = new MRG32k3a();
        NormalGen muNormal = new NormalGen(stream, 0, 1);
        NormalGen sigmaNormal = new NormalGen(stream, 0, 2);

        this.size = size;
        for (int i = 0; i <= size - 1; i++) {
            stocks.add(new Stock(stream, muNormal, sigmaNormal));
        }

        rl = RateLimiter.create(rateLimit);
    }

    public Trade getEvent() {
        rl.acquire(1);
        return stocks.get(rand.nextInt(size)).getTrade();
    }

    // prints state of market
    public String toString() {
        List<String> a = new ArrayList<String>();
        for (Stock s : stocks) {
            a.add(String.valueOf(s.lastPrice));
        }
        return String.join(",", a);
    }

    // print list of stocks in the market
    public String marketStocksNames() {
        List<String> a = new ArrayList<String>();
        for (Stock s : stocks) {
            a.add(s.symbol);
        }
        return String.join(",", a);
    }
}
