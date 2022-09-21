package it.leonardo.leonardoapiboot.utils;

import it.leonardo.leonardoapiboot.entity.Libro;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Comparator;

public class LibriComparator implements Comparator<Libro> {
    //private Log log = LogFactory.getLog(LibriComparator.class);

    private final String orderBy;

    public LibriComparator(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public int compare(Libro o1, Libro o2) {
        if (orderBy.equalsIgnoreCase("priceAsc")) {
            //log.info("Confronto "+o1.getAnnunci().get(0).getPrezzo()+" con "+o2.getAnnunci().get(0).getPrezzo()+", ritorno " + o1.getAnnunci().get(0).getPrezzo().compareTo(o2.getAnnunci().get(0).getPrezzo()));
            return o1.getAnnunci().get(0).getPrezzo().compareTo(o2.getAnnunci().get(0).getPrezzo());
        }
        if (orderBy.equalsIgnoreCase("priceDesc")) {
            //log.info("Confronto "+o2.getAnnunci().get(0).getPrezzo()+" con "+o1.getAnnunci().get(0).getPrezzo()+", ritorno " + o2.getAnnunci().get(0).getPrezzo().compareTo(o1.getAnnunci().get(0).getPrezzo()));
            return o2.getAnnunci().get(0).getPrezzo().compareTo(o1.getAnnunci().get(0).getPrezzo());
        }
        if (orderBy.equalsIgnoreCase("rec")) {
            if (o1.getAnnunci().get(0).getUtente().getAvgRating() == null) return 1;
            if (o2.getAnnunci().get(0).getUtente().getAvgRating() == null) return -1;
            //log.info("Confronto "+o2.getAnnunci().get(0).getUtente().getAvgRating().getAvgVoto()+" con "+o1.getAnnunci().get(0).getUtente().getAvgRating().getAvgVoto()+", ritorno " + o2.getAnnunci().get(0).getUtente().getAvgRating().getAvgVoto().compareTo(o1.getAnnunci().get(0).getUtente().getAvgRating().getAvgVoto()));
            return o2.getAnnunci().get(0).getUtente().getAvgRating().getAvgVoto().compareTo(o1.getAnnunci().get(0).getUtente().getAvgRating().getAvgVoto());
        }
        return o2.getAnnunci().get(0).getCreated_at().compareTo(o1.getAnnunci().get(0).getCreated_at());
    }
}
