package orederondoor.com.projectorder.Customer.ExtrasClases;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import orederondoor.com.projectorder.Customer.CustomerFirstPage.MainActivity;
import orederondoor.com.projectorder.R;

public class EmptyCart extends AppCompatActivity {

    Button btn_emptyCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty__cart);
        btn_emptyCart=(Button)findViewById(R.id.btn_emptyCart);


        btn_emptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
