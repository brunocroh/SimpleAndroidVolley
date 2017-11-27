package br.com.senaigo.mobile.northwindtrandersrest.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import br.com.senaigo.mobile.northwindtrandersrest.AppController;
import br.com.senaigo.mobile.northwindtrandersrest.R;
import br.com.senaigo.mobile.northwindtrandersrest.entities.Shipper;


public class ShipperActivity extends Activity {

    private static final String URL_GET = "https://urbano.eti.br/northwind/shipper";
    private static final String URL_POST = "https://urbano.eti.br/northwind/shipper";
    private static final String URL_JSON_ARRAY = "https://urbano.eti.br/northwind/shipper";
    private static final String TAG = "voley";
    private String jsonResponse;
    private Shipper shipper;

    private EditText etShipperID;
    private EditText etShipperCompanyName;
    private EditText etShipperPhone;
    private Button btnShipperSalvar;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);
        hiddenKeyboard();
        bindView();
        createEventClickSave();
        makeJsonArrayRequest();
    }

    private void createEventClickSave() {
        btnShipperSalvar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clickBtnSalvar();
                makeJsonArrayRequest();

            }
        });
    }

    private void hiddenKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void bindView() {
        etShipperID = (EditText) findViewById(R.id.etShipperID);
        etShipperCompanyName = (EditText) findViewById(R.id.etShipperCompanyName);
        etShipperPhone = (EditText) findViewById(R.id.etShipperPhone);
        btnShipperSalvar = (Button) findViewById(R.id.btnShipperSalvar);
        listview = (ListView) findViewById(R.id.listview);
    }

    private void clickBtnSalvar() {
        makeJsonObjectRequestPost();
        etShipperID.setText("");
        etShipperCompanyName.setText("");
        etShipperPhone.setText("");
        shipper = null;
    }

    private void makeJsonObjectRequest() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, URL_GET, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String shipperID = response.getString("shipperID");
                    String shipperCompanyName = response.getString("companyName");
                    String shipperPhone = response.getString("phone");

                    jsonResponse = "";
                    jsonResponse += "shipperID: " + shipperID + "\n\n";
                    jsonResponse += "companyName: " + shipperCompanyName + "\n\n";
                    jsonResponse += "phone: " + shipperPhone + "\n\n";
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void makeJsonObjectRequestPost() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tag", "test");
        JSONObject parametros = new JSONObject();
        try {

            if (shipper != null) {
                parametros.put("shipperID", shipper.getShipperID());
                parametros.put("companyName", shipper.getCompanyName());
                parametros.put("phone", shipper.getPhone());
                parametros.put("isUpdate", true);
            } else {
                parametros.put("shipperID", etShipperID.getText().toString());
                parametros.put("companyName", etShipperCompanyName.getText().toString());
                parametros.put("phone", etShipperPhone.getText().toString());
                parametros.put("isUpdate", false);
            }


        } catch (JSONException e1) {
            Log.e(TAG, "Erro: " + e1.getMessage());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, URL_POST, parametros, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                ((ArrayAdapter) listview.getAdapter()).notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void makeJsonArrayRequest() {

        JsonArrayRequest req = new JsonArrayRequest(URL_JSON_ARRAY, new Response.Listener<JSONArray>() {


            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                try {
                    final ArrayList<Shipper> list = new ArrayList<Shipper>();


                    for (int i = 0; i < response.length(); i++) {

                        JSONObject shippe = (JSONObject) response.get(i);

                        String shipperID = shippe.getString("shipperID");
                        String companyName = shippe.getString("companyName");
                        String phone = shippe.getString("phone");

                        Shipper shipper = new Shipper();
                        shipper.setShipperID(shipperID);
                        shipper.setCompanyName(companyName);
                        shipper.setPhone(phone);

                        list.add(shipper);
                    }


                    final StableArrayShipperAdapter adapter = new StableArrayShipperAdapter(
                            ShipperActivity.this, android.R.layout.simple_list_item_1, list);
                    listview.setAdapter(adapter);

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            final Shipper item = (Shipper) parent.getItemAtPosition(position);
                            shipper = item;

                            etShipperID.setText(item.getShipperID());
                            etShipperCompanyName.setText(item.getCompanyName());
                            etShipperPhone.setText(item.getPhone());

                            Toast.makeText(ShipperActivity.this, "O shipper já pode ser editado", Toast.LENGTH_SHORT).show();
                        }

                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }

}

class StableArrayShipperAdapter extends ArrayAdapter<Shipper> {

    HashMap<Shipper, Integer> mIdMap = new HashMap<Shipper, Integer>();

    public StableArrayShipperAdapter(Context context, int textViewResourceId, List<Shipper> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        Shipper item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

