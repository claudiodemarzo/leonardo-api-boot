package it.leonardo.leonardoapiboot.utils;

import it.leonardo.leonardoapiboot.entity.Libro;

import java.util.Comparator;

public class LibriComparator implements Comparator<Libro> {

    private final String orderBy;

    public LibriComparator(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public int compare(Libro o1, Libro o2) {
        if(orderBy.equalsIgnoreCase("prezzoasc")) return o1.getAnnunci().get(0).getPrezzo().compareTo(o2.getAnnunci().get(0).getPrezzo());
        if(orderBy.equalsIgnoreCase("prezzodesc")) return o2.getAnnunci().get(0).getPrezzo().compareTo(o1.getAnnunci().get(0).getPrezzo());
        if(orderBy.equalsIgnoreCase("rec")){
            if (o1.getAnnunci().get(0).getUtente().getAvgRating() == null) return 1;
            if (o2.getAnnunci().get(0).getUtente().getAvgRating() == null) return -1;
            return o2.getAnnunci().get(0).getUtente().getAvgRating().getAvgVoto().compareTo(o1.getAnnunci().get(0).getUtente().getAvgRating().getAvgVoto());
        }
        return o2.getAnnunci().get(0).getCreated_at().compareTo(o1.getAnnunci().get(0).getCreated_at());
    }
}
