package com.whipitupitude.generator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import umontreal.ssj.randvar.NormalGen;
import umontreal.ssj.rng.MRG32k3a;
import umontreal.ssj.stochprocess.BrownianMotion;

public class Stock {

    NormalGen muNormal;
    NormalGen sigmaNormal;
    MRG32k3a stream;
    double lastPrice;
    BrownianMotion bSeq;
    public String symbol;
    Random rand = new Random(); // randomness for setup

    private DateTimeFormatter dateFmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private double generateNewMu() {
        return this.muNormal.nextDouble();
    }

    private double generateNewSigma() {
        return Math.abs(this.sigmaNormal.nextDouble());
    }

    public Stock(MRG32k3a stream, NormalGen muNormal, NormalGen sigmaNormal) {
        this.muNormal = muNormal;
        this.sigmaNormal = sigmaNormal;
        this.stream = stream;

        symbol = RandomStringUtils.randomAlphabetic(4).toUpperCase();
        System.out.println("New Stock: " + symbol);

        double s0 = rand.nextDouble(10.0, 100.0); // set starting stock price
        lastPrice = (double) Math.round(s0 * 10000d) / 10000d;
        ;

        bSeq = new BrownianMotion(s0, generateNewMu(), generateNewSigma(), stream);
        bSeq.setObservationTimes(.5, rand.nextInt(100));
    }

    public Trade getTrade() {
        if (bSeq.getCurrentObservationIndex() == bSeq.getNumObservationTimes()) {
            // reset sequence
            bSeq.setParams(bSeq.getCurrentObservation(), generateNewMu(), generateNewSigma());
            bSeq.resetStartProcess();
        }
        double price = bSeq.nextObservation();
        price = (double) Math.round(price * 10000d) / 10000d;
        String buySell;
        if (price > lastPrice) {
            buySell = "B";
        } else {
            buySell = "S";
        }
        lastPrice = price;

        int quantity = rand.nextInt(1, 1000);

        return new Trade(symbol, price, buySell, quantity);
    }

    public Position getInitialPosition() {

        double lastTradePrice = bSeq.getCurrentObservation();
        lastTradePrice = (double) Math.round(lastTradePrice * 10000d) / 10000d;

        int position = rand.nextInt(1, 10000);

        String lastTradeTime = dateFmt.format(LocalDateTime.now());
        return new Position(symbol, lastTradePrice, position, lastTradeTime);
    }

}

/*
 * 
 * double s0 = 4; double mu = generateNewMu(randNormal); double sigma = 0.89;
 * int d = 50; BrownianMotion bSeq = new BrownianMotion(s0, mu, sigma, stream);
 * bSeq.setObservationTimes(.5, d); // BrownianMotion bSeq2 = new
 * BrownianMotion(s0, .9, sigma, stream); // bSeq2.setObservationTimes(0.2, d);
 * for (int i = 0; i <= periods; i++) { if (bSeq.getCurrentObservationIndex() ==
 * bSeq.getNumObservationTimes()) { // reset sequence
 * bSeq.setParams(bSeq.getCurrentObservation(), generateNewMu(randNormal), .3);
 * bSeq.resetStartProcess(); } //
 * System.out.println(bSeq.getCurrentObservationIndex() + "-" + //
 * bSeq.getNumObservationTimes()); System.out.println( i + "," +
 * bSeq.getCurrentObservationIndex() + "," + bSeq.nextObservation() + "," +
 * bSeq.hasNextObservation());
 * 
 * }
 */
