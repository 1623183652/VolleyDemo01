package com.example.volleydemo01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class Test01Activity extends Activity {
	private RequestQueue queue;
	private String json_url = "http://m.weather.com.cn/data/101010100.html";
	private JsonObjectRequest jsonObjectRequest;
	private final String tag = "Test01Activity";
	private ListView listView;
	private SimpleAdapter simpleAdapter;
	private List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();

	private void initRequestQueue() {
		queue = Volley.newRequestQueue(this);
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.lv);
		simpleAdapter = new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, new String[] { "title",
						"content" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		listView.setAdapter(simpleAdapter);
	}

	@SuppressWarnings("unchecked")
	private void getWeatherInfo(JSONObject response) {
		Iterator<String> iterator = response.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			try {
				JSONObject object = response.getJSONObject(key);
				if (object != null) {
					Iterator<String> objIterator = object.keys();
					while (objIterator.hasNext()) {
						String objKey = objIterator.next();
						String objValue = (String) object.get(objKey);
						Map<String, String> map = new HashMap<String, String>();
						map.put("title", objKey);
						map.put("content", objValue);
						list.add(map);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private void testJsonObject() {
		jsonObjectRequest = new JsonObjectRequest(json_url, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						getWeatherInfo(response);
						simpleAdapter.notifyDataSetChanged();
						Log.e(tag, "------->" + response.toString());
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(tag, "------->" + error.toString(), error);
					}
				});
		queue.add(jsonObjectRequest);
	}

	@Override
	protected void onDestroy() {
		queue.cancelAll(this);
		super.onDestroy();
	}

	private void initData() {
		testJsonObject();
	}

	private void init() {
		initRequestQueue();
		initView();
		initData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test01);
		init();
	}

}
