package orederondoor.com.projectorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;

public class SplashActivity extends AppCompatActivity {


        Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView=(ImageView)findViewById(R.id.iv_logo_splash);
        TextView textView=(TextView)findViewById(R.id.tv_logo_splash);
        Animation anim_clockWise_rotat=AnimationUtils.loadAnimation(this,R.anim.clock_wise_rotation);
        Animation animation=AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        imageView.startAnimation(animation);
        imageView.startAnimation(anim_clockWise_rotat);

        textView.startAnimation(animation);
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },8000);

        }

}
