package br.com.senaigo.mobile.northwindtrandersrest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.senaigo.mobile.northwindtrandersrest.R;


public class MainActivity extends Activity {

	private Button btnStartEmployeeActivity;
	private Button btnStartShipperActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnStartEmployeeActivity = (Button) findViewById(R.id.btnStartActivity);
		btnStartEmployeeActivity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), EmployeeActivity.class);
				v.getContext().startActivity(intent);
			}
		});

		btnStartShipperActivity = (Button) findViewById(R.id.btnShipperActivity);
		btnStartShipperActivity.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), ShipperActivity.class);
				v.getContext().startActivity(intent);
			}
		});
	}
}
