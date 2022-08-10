package it.leonardo.leonardoapiboot.utils;

import it.leonardo.leonardoapiboot.entity.Chatroom;

import java.util.Comparator;

public class ChatroomComparator implements Comparator<Chatroom> {
    @Override
    public int compare(Chatroom o1, Chatroom o2) {
        return o1.getLastMessageDate().compareTo(o2.getLastMessageDate());
    }
}
