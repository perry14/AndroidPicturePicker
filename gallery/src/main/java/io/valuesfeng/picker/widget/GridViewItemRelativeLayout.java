/*
 * Copyright (C) 2014 nohana, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.valuesfeng.picker.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

import io.valuesfeng.picker.ImageSelectActivity;
import io.valuesfeng.picker.R;
import io.valuesfeng.picker.control.SelectedUriCollection;
import io.valuesfeng.picker.model.Item;
import io.valuesfeng.picker.utils.AlbumHelper;
import io.valuesfeng.picker.utils.PicturePickerUtils;

/**
 * @author KeithYokoma
 * @version 1.0.0
 * @hide
 * @since 2014/03/24
 */
public class GridViewItemRelativeLayout extends RelativeLayout {

    private ImageView imageView;
    private ImageView imageCheck;
    private Item item;
    SelectedUriCollection mCollection;

    private DisplayImageOptions optionsImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.image_not_exist)
            .showImageForEmptyUri(R.drawable.image_not_exist)
            .showImageOnFail(R.drawable.image_not_exist)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();
    private DisplayImageOptions optionsCameraImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.ic_camera)
            .showImageForEmptyUri(R.drawable.ic_camera)
            .showImageOnFail(R.drawable.ic_camera)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public GridViewItemRelativeLayout(Context context) {
        this(context, null);
    }

    public GridViewItemRelativeLayout(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public GridViewItemRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setImageView(ImageView imageView, ImageView imageCheck, SelectedUriCollection mCollection) {
        this.imageView = imageView;
        this.imageCheck = imageCheck;
        this.mCollection = mCollection;
        this.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isCapture()) {
                    ((ImageSelectActivity) getContext()).showCameraAction();
                    return;
                } else if (GridViewItemRelativeLayout.this.mCollection.isSingleChoose()) {
                    GridViewItemRelativeLayout.this.mCollection.add(item.buildContentUri());
                    ((ImageSelectActivity) getContext()).setResult();
                    return;
                }
                if (GridViewItemRelativeLayout.this.mCollection.isCountOver()
                        && !GridViewItemRelativeLayout.this.mCollection.isSelected(item.buildContentUri())) {
                    return;
                }
                if (GridViewItemRelativeLayout.this.mCollection.isSelected(item.buildContentUri())) {
                    GridViewItemRelativeLayout.this.mCollection.remove(item.buildContentUri());
                    GridViewItemRelativeLayout.this.imageCheck.setImageResource(R.drawable.pick_photo_checkbox_normal);
                    GridViewItemRelativeLayout.this.imageView.clearColorFilter();
                } else {
                    GridViewItemRelativeLayout.this.mCollection.add(item.buildContentUri());
                    GridViewItemRelativeLayout.this.imageCheck.setImageResource(R.drawable.pick_photo_checkbox_check);
                    GridViewItemRelativeLayout.this.imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        });
    }

    public void setItem(Item item) {
        this.item = item;
        imageView.clearColorFilter();
        imageCheck.setImageResource(R.drawable.pick_photo_checkbox_normal);
        if (mCollection.isSelected(item.buildContentUri())) {
            imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            imageCheck.setImageResource(R.drawable.pick_photo_checkbox_check);
        }
        imageCheck.setVisibility(mCollection.isSingleChoose() || item.isCapture() ? View.GONE : View.VISIBLE);
        disPlay();
    }

    private void disPlay() {
        if (item.isCapture()) {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_camera, imageView, optionsCameraImage);
        } else {
//            if (AlbumHelper.getInstance(getContext()).getThumbnail().containsKey(item.getId())) {
//                String thumbnailuri = AlbumHelper.getInstance(getContext()).getThumbnail().get(item.getId());
//                File file = new File(thumbnailuri);
//                thumbnailuri = file.exists() && file.isFile() ? "file://" + thumbnailuri : item.buildContentUri().toString();
//                ImageLoader.getInstance().displayImage("file://" + thumbnailuri, imageView, optionsImage);
//            } else {
//                ImageLoader.getInstance().displayImage(item.buildContentUri().toString(), imageView, optionsImage);
//            }
            ImageLoader.getInstance().displayImage(item.buildContentUri().toString(), imageView, optionsImage);
        }
    }
}