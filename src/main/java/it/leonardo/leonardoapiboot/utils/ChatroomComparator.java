package it.leonardo.leonardoapiboot.utils;

import it.leonardo.leonardoapiboot.entity.Chatroom;

import java.util.Comparator;

public class ChatroomComparator implements Comparator<Chatroom> {
    @Override
    public int compare(Chatroom o1, Chatroom o2) {
        if(o1.getLastMessageDate() == null) return -1;
        if(o2.getLastMessageDate() == null) return 1;
        return o2.getLastMessageDate().compareTo(o1.getLastMessageDate());
    }
}
