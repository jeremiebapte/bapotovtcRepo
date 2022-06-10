package com.bapoto.vtc.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapoto.databinding.ItemContainerAdminBinding;
import com.bapoto.vtc.listeners.AdminListener;
import com.bapoto.vtc.model.Admin;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder>{

    private final List<Admin> admins;
    private final AdminListener adminListener;

    ItemContainerAdminBinding binding;

    public AdminAdapter(List<Admin> admins, AdminListener adminListener) {
        this.admins = admins;
        this.adminListener = adminListener;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerAdminBinding itemContainerAdminBinding = ItemContainerAdminBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AdminViewHolder(itemContainerAdminBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        holder.setUserData(admins.get(position));
    }

    @Override
    public int getItemCount() {
        return admins.size();
    }

    class AdminViewHolder extends RecyclerView.ViewHolder {
        AdminViewHolder(ItemContainerAdminBinding itemContainerAdminBinding) {
            super(itemContainerAdminBinding.getRoot());
            binding = itemContainerAdminBinding;
        }

        void setUserData(Admin admin) {
            binding.textName.setText(admin.name);
            binding.textEmail.setText(admin.email);
            binding.imageProfile.setImageBitmap(getAdminsImage(admin.image));
            binding.getRoot().setOnClickListener(view -> adminListener.onClickedAdmin(admin));


        }
    }

    private Bitmap getAdminsImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);

    }
}
