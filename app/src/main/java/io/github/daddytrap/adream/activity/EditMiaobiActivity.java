package io.github.daddytrap.adream.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.ADSQLiteOpenHelper;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.model.Passage;
import io.github.daddytrap.adream.model.User;
import io.github.daddytrap.adream.util.ADUtil;

public class EditMiaobiActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private TextView miaobiTitle;
    private TextView miaobiContent;

    private ImageView backIcon;
    private ImageView faIcon;

    private ImageView addPhoto;

    private ADApplication app;

    public static final int PICK_IMAGE_REQ_CODE = 100;

    boolean imageSelected = false;
    String imageBase64;

    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_miaobi);

        app = ADApplication.getInstance();

        setResult(RESULT_CANCELED);

        setViews();
    }

    void setViews() {
        toolbarTitle = (TextView) findViewById(R.id.activity_edit_miaobi_toolbar_title);
        miaobiTitle = (TextView) findViewById(R.id.activity_edit_miaobi_title_edittext);
        miaobiContent = (TextView) findViewById(R.id.activity_edit_miaobi_content_edittext);

        toolbarTitle.setTypeface(app.KAI_TI_FONT);
        miaobiTitle.setTypeface(app.SIM_KAI_FONT);
        miaobiContent.setTypeface(app.SIM_KAI_FONT);

        backIcon = (ImageView)findViewById(R.id.activity_edit_miaobi_back_icon);
        faIcon = (ImageView)findViewById(R.id.activity_edit_miaobi_fa_icon);
        addPhoto = (ImageView)findViewById(R.id.activity_edit_miaobi_addphoto);

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("请确认已填入标题/正文 并已选择图片").setPositiveButton(R.string.alertdialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditMiaobiActivity.this.finish();
            }
        });

        faIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = miaobiTitle.getText().toString();
                String content = miaobiContent.getText().toString();

                if (!imageSelected || title.isEmpty() || content.isEmpty()) {
                    dialogBuilder.create().show();
                    return;
                }

                Date date = Calendar.getInstance().getTime();

                ADSQLiteOpenHelper helper = new ADSQLiteOpenHelper(EditMiaobiActivity.this);
                User user = helper.getUserById(app.currentUserId);
                Passage passage = new Passage(-1, title, user.getUserName(), content, date, imageBase64);

                helper.insertMiaoBi(passage);
                EditMiaobiActivity.this.setResult(RESULT_OK);
                EditMiaobiActivity.this.finish();
            }
        });

        // Set add photo button
        View.OnClickListener selectPhotoListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQ_CODE);
            }
        };
        addPhoto.setOnClickListener(selectPhotoListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQ_CODE) {
            Log.i("Info", "Picked an image");
            if (resultCode == RESULT_OK) {
                Uri imgUri = data.getData();
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(imgUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                // Save base64 result in moment
                byte[] bytes = null;
                try {
                    bytes = ADUtil.getBytes(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                imageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);

                // Set preview image
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                addPhoto.setImageBitmap(bm);
                addPhoto.setPadding(0, 0, 0, 0);
                imageSelected = true;
            }
        }
    }
}
