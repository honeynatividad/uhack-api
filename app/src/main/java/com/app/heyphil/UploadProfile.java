package com.app.heyphil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.mikhaellopez.circularimageview.CircularImageView;

public class UploadProfile extends Activity {
        private static final int CAMERA_REQUEST = 1888;
        private static int RESULT_LOAD_IMAGE = 1;
        private CircularImageView profile;
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
            setContentView(R.layout.upload_profile);
            this.profile = (CircularImageView) this.findViewById(R.id.profile);
            Button btn_capture = (Button) this.findViewById(R.id.capture);
            Button btn_upload = (Button) this.findViewById(R.id.upload);
            btn_capture.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });
            btn_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            });
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Data.bitmap=photo;
                profile.setImageBitmap(photo);
            }
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap photo = BitmapFactory.decodeFile(picturePath, options);
                cursor.close();
                profile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                Data.bitmap=photo;

            }

        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i= new Intent(UploadProfile.this, ChatBubbleActivity.class);
        startActivity(i);
        finish();
    }
}
