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
import br.com.senaigo.mobile.northwindtrandersrest.entities.Employee;


public class EmployeeActivity extends Activity {

    private static final String URL_GET = "https://urbano.eti.br/northwind/employee";
    private static final String URL_POST = "https://urbano.eti.br/northwind/employee";
    private static final String URL_JSON_ARRAY = "https://urbano.eti.br/northwind/employee";
    private static final String TAG = "voley";
    private String jsonResponse;
    private Employee employee;

    private EditText etEmployeeFirstName;
    private EditText etEmployeeLastName;
    private EditText etEmployeePhoneNumber;
    private EditText etEmployeeBirthDate;
    private Button btnEmployeeSalvar;
    private Button btnEmployeeEditar;
    private Button btnEmployeeDeletar;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        hiddenKeyboard();
        bindView();
        createEventClickSave();
        createEventEdit();
        createEventDelete();
        makeJsonArrayRequest();
    }

    private void createEventDelete() {
        btnEmployeeDeletar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Deletar();
                makeJsonArrayRequest();
            }
        });
    }

    private void Deletar() {
        final JSONObject parametros = new JSONObject();
        String urlDelete= "";

        urlDelete = URL_POST+"/"+employee.getId();

        JsonObjectRequest deleteRequest = new JsonObjectRequest
                (Method.DELETE, urlDelete, parametros,
                        new Response.Listener<JSONObject>() {

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

        AppController.getInstance().addToRequestQueue(deleteRequest);


    }

    private void createEventEdit() {
        btnEmployeeEditar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Editar();
                makeJsonArrayRequest();
            }

        });
    }

    private void Editar() {
        Map<String, String> params = new HashMap<String, String>();
        JSONObject parametros = new JSONObject();
        try {

            String id = employee.getId();
            String firstName = etEmployeeFirstName.getText().toString();
            String lastName = etEmployeeLastName.getText().toString();
            String homePhone = etEmployeePhoneNumber.getText().toString();
            String birthDate = etEmployeeBirthDate.getText().toString();

            parametros.put("employeeID",id);
            parametros.put("firstName",firstName);
            parametros.put("lastName", lastName);
            parametros.put("homePhone", homePhone);
            parametros.put("isUpdate", true);

        } catch (JSONException e1) {
            Log.e(TAG, "Erro: " + e1.getMessage());
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Method.PUT, URL_POST,
                parametros, new Response.Listener<JSONObject>() {

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

    private void createEventClickSave() {
        btnEmployeeSalvar.setOnClickListener(new OnClickListener() {

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
        etEmployeeFirstName = (EditText) findViewById(R.id.etEmployeeFirstName);
        etEmployeeLastName = (EditText) findViewById(R.id.etEmployeeLastName);
        etEmployeePhoneNumber = (EditText) findViewById(R.id.etEmployeePhoneNumber);
        etEmployeeBirthDate = (EditText) findViewById(R.id.etEmployeeBirthDate);
        btnEmployeeSalvar = (Button) findViewById(R.id.btnEmployeeSalvar);
        btnEmployeeEditar = (Button) findViewById(R.id.btnEmployeeEditar);
        btnEmployeeDeletar = (Button) findViewById(R.id.btnEmployeeDeletar);
        listview = (ListView) findViewById(R.id.listview);
    }

    private void clickBtnSalvar() {

        makeJsonObjectRequestPost();
        etEmployeeFirstName.setText("");
        etEmployeeLastName.setText("");
        etEmployeePhoneNumber.setText("");
        etEmployeeBirthDate.setText("");
        employee = null;

    }

    private void makeJsonObjectRequest() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, URL_GET, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String employeeID = response.getString("employeeID");
                    String firstName = response.getString("firstName");
                    String lastName = response.getString("lastName");
                    String homePhone = response.getString("homePhone");
                    String birthDate = response.getString("birthDate");

                    jsonResponse = "";
                    jsonResponse += "employeeID: " + employeeID + "\n\n";
                    jsonResponse += "firstName: " + firstName + "\n\n";
                    jsonResponse += "lastName: " + lastName + "\n\n";
                    jsonResponse += "homePhone: " + homePhone + "\n\n";
                    jsonResponse += "birthDate: " + birthDate + "\n\n";
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

            if (employee != null) {
                parametros.put("employeeID", employee.getId());
                parametros.put("firstName", employee.getFirstName());
                parametros.put("lastName", employee.getLastName());
                parametros.put("homePhone", employee.getPhoneNumber());
                parametros.put("birthDate", employee.getBirthDate());
                parametros.put("isUpdate", true);
            } else {
                parametros.put("firstName", etEmployeeFirstName.getText().toString());
                parametros.put("lastName", etEmployeeLastName.getText().toString());
                parametros.put("homePhone", etEmployeePhoneNumber.getText().toString());
                parametros.put("birthDate", etEmployeeBirthDate.getText().toString());
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
                    final ArrayList<Employee> list = new ArrayList<Employee>();


                    for (int i = 0; i < response.length(); i++) {

                        JSONObject employe = (JSONObject) response.get(i);

                        String employeeID = employe.getString("employeeID");
                        String firstName = employe.getString("firstName");
                        String lastName = employe.getString("lastName");
                        String homePhone = employe.getString("homePhone");
                        String birthDate = "";

                        try {
                            birthDate = employe.getString("birthDate");
                        } catch (Exception e) {
                        }

                        Employee employee = new Employee();
                        employee.setId(employeeID);
                        employee.setFirstName(firstName);
                        employee.setLastName(lastName);
                        employee.setPhoneNumber(homePhone);
                        employee.setBirthDate(birthDate);

                        list.add(employee);

                    }


                    final StableArrayAdapter adapter = new StableArrayAdapter(
                            EmployeeActivity.this, android.R.layout.simple_list_item_1, list);
                    listview.setAdapter(adapter);

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            final Employee item = (Employee) parent.getItemAtPosition(position);
                            employee = item;

                            etEmployeeFirstName.setText(item.getFirstName());
                            etEmployeeLastName.setText(item.getLastName());
                            etEmployeePhoneNumber.setText(item.getPhoneNumber());
                            etEmployeeBirthDate.setText(item.getBirthDate());

                            Toast.makeText(EmployeeActivity.this, "O employee já pode ser editado", Toast.LENGTH_SHORT).show();
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

class StableArrayAdapter extends ArrayAdapter<Employee> {

    HashMap<Employee, Integer> mIdMap = new HashMap<Employee, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId, List<Employee> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        Employee item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
