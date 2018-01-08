package io.github.daddytrap.adream.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cesards.cropimageview.CropImageView;
import com.xhinliang.lunarcalendar.utils.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.ADSQLiteOpenHelper;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.model.Comment;
import io.github.daddytrap.adream.model.Passage;
import io.github.daddytrap.adream.model.User;

public class MiaobiDetailActivity extends AppCompatActivity {
    private int passageId;
    private Passage passage;
    private CropImageView mPic;
    private TextView mTitle, mAuthor, mDate, mPassage;
    private EditText mEditComment;
    private LinearLayout mCommentList;
    private ImageView mBack, mZan;
    private boolean zanned;
    private ADSQLiteOpenHelper helper;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private static ADApplication app = ADApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miaobi_detail);
        Intent intent = getIntent();
        Intent dataIntent = getIntent();
        passageId = dataIntent.getIntExtra("id", -1);
        helper = new ADSQLiteOpenHelper(this);
        passage = helper.getPassageById(passageId);

        if (passage == null) {
            Log.e(MiaobiDetailActivity.class.getName(), "Not such passage");
            finish();
            return;
        }

        getView();
        setView();
    }

    void getView() {
        mPic = (CropImageView)findViewById(R.id.miaobi_detail_picture);
        mTitle = (TextView)findViewById(R.id.miaobi_detail_title);
        mAuthor = (TextView)findViewById(R.id.miaobi_detail_author);
        mDate = (TextView)findViewById(R.id.miaobi_detail_date);
        mPassage = (TextView)findViewById(R.id.miaobi_detail_passage);
        mEditComment = (EditText)findViewById(R.id.miaobi_detail_add_comment);
        mCommentList = (LinearLayout) findViewById(R.id.miaobi_detail_comments);
        mBack = (ImageView) findViewById(R.id.miaobi_detail_back_icon);
        mZan = (ImageView) findViewById(R.id.miaobi_detail_zan_icon);
    }

    void setView() {
        mPic.setImageBitmap(app.getBitmap(passage.getAvatarBase64()));
        mTitle.setText(passage.getTitle());
        mTitle.setTypeface(app.SIM_KAI_FONT);
        mDate.setText(formatter.format(passage.getDate()));
        mDate.setTypeface(app.SIM_KAI_FONT);
        mAuthor.setText(passage.getAuthor());
        mAuthor.setTypeface(app.SIM_KAI_FONT);
        mPassage.setText(passage.getContent());
        mPassage.setTypeface(app.SIM_KAI_FONT);
        mEditComment.setTypeface(app.SIM_KAI_FONT);
        ((TextView)findViewById(R.id.comment_title)).setTypeface(app.SIM_KAI_FONT);
        List<Comment> comments = helper.getCommentByPassageId(passageId);
        for (Comment comment : comments) {
            appendComment(comment);
        }
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MiaobiDetailActivity.this.finish();
            }
        });

        zanned = helper.getPraiseByUserIdAndPassageId(app.currentUserId, passageId);
        mZan.setImageResource(zanned ? R.mipmap.zanned_icon : R.mipmap.zan_icon);

        mZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zanned = !zanned;
                try {
                    if (zanned) {
                        helper.insertPraise(app.currentUserId, passage.getPassageId());
                    } else {
                        helper.deletePraise(app.currentUserId, passage.getPassageId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mZan.setImageResource(zanned ? R.mipmap.zanned_icon : R.mipmap.zan_icon);
            }
        });
        mEditComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {

                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    MiaobiDetailActivity.this.addComment(mEditComment.getText().toString());
                    if(imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);

                    }

                }
                return false;
            }
        });
    }

    void appendComment(Comment comment) {
        View commentView = View.inflate(this, R.layout.miaobi_comment, null);
        CircleImageView avatar = (CircleImageView) commentView.findViewById(R.id.comment_avatar);
        User user = comment.getUser();
        avatar.setImageBitmap(app.getBitmap(user.getAvatarBase64()));
        TextView username = (TextView) commentView.findViewById(R.id.comment_user);
        username.setText(user.getUserName());
        username.setTypeface(app.SIM_KAI_FONT);
        TextView date = (TextView) commentView.findViewById(R.id.comment_date);
        date.setText(formatter.format(comment.getDate()));
        date.setTypeface(app.SIM_KAI_FONT);
        TextView content = (TextView) commentView.findViewById(R.id.comment_content);
        content.setText(comment.getContent());
        content.setTypeface(app.SIM_KAI_FONT);
        mCommentList.addView(commentView);
    }

    void addComment(String content) {
        if (TextUtils.isEmpty(content)) return;
        User user = helper.getUserById(app.currentUserId);
        Comment comment = new Comment(-1, user, content, new Date());
        helper.insertComment(comment, passageId);
        appendComment(comment);
    }
}
