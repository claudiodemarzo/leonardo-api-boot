package it.leonardo.leonardoapiboot.utils;

import it.leonardo.leonardoapiboot.entity.AnnunciLibri;

import java.util.Comparator;

public class AnnunciComparator implements Comparator<AnnunciLibri> {
	@Override
	public int compare(AnnunciLibri o1, AnnunciLibri o2) {

		String priority = "nobda";
		int compareUsura;
		if(priority.indexOf(o1.getLivello_usura()) < priority.indexOf(o2.getLivello_usura())) compareUsura = 1;
		else if (priority.indexOf(o1.getLivello_usura()) > priority.indexOf(o2.getLivello_usura())) compareUsura = -1;
		else compareUsura = 0;
		if (compareUsura == 0) {
			int comparePrezzo = Float.compare(o2.getPrezzo(), o1.getPrezzo());
			if(comparePrezzo == 0){
				return o1.getCreated_at().compareTo(o2.getCreated_at());
			}
			return comparePrezzo;
		}
		return compareUsura;
	}
}
