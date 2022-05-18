package com.bapoto.vtc.manager;

import android.net.Uri;

import com.bapoto.vtc.repository.ChatRepository;
import com.google.firebase.firestore.Query;

public class ChatManager {

    private static volatile ChatManager instance;
    private final ChatRepository chatRepository;

    private ChatManager() {
        chatRepository = ChatRepository.getInstance();
    }

    public static ChatManager getInstance() {
        ChatManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(ChatManager.class) {
            if (instance == null) {
                instance = new ChatManager();
            }
            return instance;
        }
    }

    public Query getAllMessageForChat(String chat){
        return chatRepository.getAllMessageForChat(chat);
    }

    public void createMessageForChat(String message, String chat){
        chatRepository.createMessageForChat(message, chat);
    }

    public void sendMessageWithImageForChat(String message, Uri imageUri, String chat){
        chatRepository.uploadImage(imageUri, chat).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                chatRepository.createMessageWithImageForChat(uri.toString(), message, chat);
            });
        });
    }

}