package com.example.chat_app1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chat_app1.R;
import com.example.chat_app1.model.MediaList;

import java.io.File;
import java.util.ArrayList;

public class MediaListAdapter extends ArrayAdapter<MediaList> {
    public MediaListAdapter(@NonNull Context context, ArrayList<MediaList> arrayList) {
        super(context, 0,arrayList);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // convertView which is recyclable view
        View currentItemView = convertView;
        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.media_list, parent, false);
        }
//        MediaList mediaListItem = getItem(position);
//        String fileExtension = mediaListItem.getFileName().substring(mediaListItem.getFileName().lastIndexOf(".") + 1);
//        File file = new File(mediaListItem.getFilePath());
//
//        if (fileExtension.equals("png") || fileExtension.equals("jpeg") || fileExtension.equals("jpg")) {//if list item any image then
//            File imgFile = new File(mediaListItem.getFilePath());
//            if (imgFile.exists()) {
//                int THUMB_SIZE = 64;
//                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mediaListItem.getFilePath()),
//                        THUMB_SIZE, THUMB_SIZE);
//                holder.imgItemIcon.setImageBitmap(ThumbImage);
//            }
//        } else if (fileExtension.equals("pdf")) {
//            holder.imgItemIcon.setImageResource(R.drawable.ic_pdf_file);
//        } else if (fileExtension.equals("mp3")) {
//            holder.imgItemIcon.setImageResource(R.drawable.ic_audio_file);
//        } else if (fileExtension.equals("txt")) {
//            holder.imgItemIcon.setImageResource(R.drawable.ic_text_file);
//        } else if (fileExtension.equals("zip") || fileExtension.equals("rar")) {
//            holder.imgItemIcon.setImageResource(R.drawable.ic_zip_folder);
//        } else if (fileExtension.equals("html") || fileExtension.equals("xml")) {
//            holder.imgItemIcon.setImageResource(R.drawable.ic_html_file);
//        } else if (fileExtension.equals("mp4") || fileExtension.equals("3gp") || fileExtension.equals("wmv") || fileExtension.equals("avi")) {
//            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(mediaListItem.getFilePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
//            holder.imgItemIcon.setImageBitmap(bMap);
//        } else if (fileExtension.equals("apk")) {
//            holder.imgItemIcon.setImageResource(R.drawable.ic_apk);
//        } else {
//            holder.imgItemIcon.setImageResource(R.drawable.ic_un_supported_file);
//        }

        return currentItemView;
    }
}
