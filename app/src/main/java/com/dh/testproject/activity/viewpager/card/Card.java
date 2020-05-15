package com.dh.testproject.activity.viewpager.card;

import android.text.BidiFormatter;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Card {
    private static final String ARGS_BUNDLE = Card.class.getSimpleName() + ":Bundle";
    private String suit;
    private String value;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }


    private static Set<String> SUITS = new HashSet<String>() {
        {
            add("♣" /* clubs*/);
            add("♦" /* diamonds*/);
            add("♥" /* hearts*/);
            add("♠" /*spades*/);
        }
    };
    private static Set<String> VALUES = new HashSet<String>() {
        {
            add("2");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
            add("8");
            add("9");
            add("10");
            add("J");
            add("Q");
            add("K");
            add("A");
        }
    };

    public static List<Card> getDECK() {
        List<Card> list = new ArrayList<>();
        Iterator<String> iterator = SUITS.iterator();
        for (String suit : SUITS) {
            Log.e(ARGS_BUNDLE, "suit: " + suit);
            for (String value : VALUES) {
                Log.e(ARGS_BUNDLE, "value: " + value);
                list.add(new Card(suit,value));
            }
        }
        return list;
    }

    @NonNull
    @Override
    public String toString() {
        BidiFormatter bidiFormatter = BidiFormatter.getInstance();
        if (bidiFormatter.isRtlContext()) {
            return bidiFormatter.unicodeWrap(suit + " " + value);
        }else {
            return bidiFormatter.unicodeWrap(value + " " + suit);
        }
    }
}
