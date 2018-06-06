package lib.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonParser {
	public static Object parseJson(String inJsonString) throws JSONException {
		JsonParser parser = new JsonParser();

		return (parser.parseJsonString(inJsonString));
	}

	@SuppressWarnings("unchecked")
	private Object parseJsonString(String jsonString) throws JSONException {
		Object receivedData = null;
		Object jsonObject = new JSONTokener(jsonString).nextValue();

		if (jsonObject instanceof JSONObject) {
			receivedData = new HashMap<String, Object>();

			insertJsonData((JSONObject) jsonObject,
					(Map<String, Object>) receivedData);
		} else if (jsonObject instanceof JSONArray) {
			receivedData = new ArrayList<Object>();

			insertJsonData((JSONArray) jsonObject, (List<Object>) receivedData);
		}
		return receivedData;
	}
	
	/**
	 * 
	 * @param jsonObject
	 * @param container
	 */
	@SuppressWarnings("unchecked")
	public static void insertJsonToObject(JSONObject jsonObject, Object container) {
		if (jsonObject == null || container == null) {
			return;
		}
		
		if (container instanceof Map<?, ?>) {
			try {
				new JsonParser().insertJsonData(jsonObject, (Map<String, Object>)container);
			} catch (JSONException e) {
			} 
		}		
	}
	
	@SuppressWarnings("unchecked")
	private void insertJsonData(JSONObject jsonObject, Map<String, Object> map)
			throws JSONException {
		Iterator<String> iterator = jsonObject.keys();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Object value = jsonObject.get(key);

			if (value instanceof JSONObject) {
				Map<String, Object> internalMap = new HashMap<String, Object>();

				insertJsonData((JSONObject) value, internalMap);
				map.put(key, internalMap);
			} else if (value instanceof JSONArray) {
				List<Object> internalList = new ArrayList<Object>();

				insertJsonData((JSONArray) value, internalList);
				map.put(key, internalList);
			} else {
				map.put(key, value);
			}
		}
	}

	private void insertJsonData(JSONArray jsonObject, List<Object> list)
			throws JSONException {
		for (int i = 0; i < jsonObject.length(); i++) {
			Object object = jsonObject.get(i);

			if (object instanceof JSONObject) {
				Map<String, Object> internalMap = new HashMap<String, Object>();

				insertJsonData((JSONObject) object, internalMap);
				list.add(internalMap);
			} else if (object instanceof JSONArray) {
				List<Object> internalList = new ArrayList<Object>();

				insertJsonData((JSONArray) object, internalList);
				list.add(internalList);
			} else {
				list.add(object);
			}
		}
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public static JSONObject createJsonObject(Map<String, Object> map) {
		JSONObject jsonObject = new JSONObject();
		if (map == null) {
			return jsonObject;
		}
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Object value = map.get(key);
			try {
				jsonObject.put(key, value);
			} catch (JSONException e) {
				return null;
			}
		}
		
		return jsonObject;
	}
}
