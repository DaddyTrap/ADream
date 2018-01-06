package io.github.daddytrap.adream.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;

public class PeopleActivity extends AppCompatActivity {

    private ADApplication app = ADApplication.getInstance();

    private TextView design, zhang, develop, lu, chen, liu;

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
    }
}
