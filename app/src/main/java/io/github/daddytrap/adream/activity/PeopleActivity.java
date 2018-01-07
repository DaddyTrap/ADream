package io.github.daddytrap.adream.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;

public class PeopleActivity extends AppCompatActivity {

    private ADApplication app = ADApplication.getInstance();

    private TextView design, zhang, develop, lu, chen, liu;
    private ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        design = (TextView) findViewById(R.id.design);
        zhang = (TextView) findViewById(R.id.zhang);
        develop = (TextView) findViewById(R.id.develop);
        lu = (TextView) findViewById(R.id.lu);
        chen = (TextView) findViewById(R.id.chen);
        liu = (TextView) findViewById(R.id.liu);

        design.setTypeface(app.KAI_TI_FONT);
        zhang.setTypeface(app.KAI_TI_FONT);
        develop.setTypeface(app.KAI_TI_FONT);
        lu.setTypeface(app.KAI_TI_FONT);
        chen.setTypeface(app.KAI_TI_FONT);
        liu.setTypeface(app.KAI_TI_FONT);

        backIcon = (ImageView) findViewById(R.id.activity_people_back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeopleActivity.this.finish();
            }
        });
    }
}
