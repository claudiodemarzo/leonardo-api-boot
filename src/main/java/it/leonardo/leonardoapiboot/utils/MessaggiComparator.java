package it.leonardo.leonardoapiboot.utils;

import it.leonardo.leonardoapiboot.entity.Messaggio;

import java.util.Comparator;

public class MessaggiComparator implements Comparator<Messaggio> {
    @Override
    public int compare(Messaggio o1, Messaggio o2) {
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}
