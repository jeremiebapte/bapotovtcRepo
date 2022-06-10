package com.bapoto.vtc.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapoto.databinding.ItemContainerRecentConversationBinding;
import com.bapoto.vtc.model.ChatMessage;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {


    private final List<ChatMessage> chatMessages;
    //private final ConversionListener conversionListener;

    public RecentConversationsAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConversationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }


    static class ConversionViewHolder extends RecyclerView.ViewHolder {

        ItemContainerRecentConversationBinding binding;

        ConversionViewHolder(ItemContainerRecentConversationBinding itemContainerConversationBinding) {
            super(itemContainerConversationBinding.getRoot());
            binding = itemContainerConversationBinding;
        }

        void setData(ChatMessage chatMessage) {
            binding.imageProfile.setImageBitmap(getConversionImage(chatMessage.conversionImage));
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);

        }

        private Bitmap getConversionImage(String encodedImage) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        }
    }
}