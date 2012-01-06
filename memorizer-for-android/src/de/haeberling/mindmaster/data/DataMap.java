/*
 * Copyright 2012 Sascha Häberling
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.haeberling.mindmaster.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.Log;

/**
 * A class that maps the data to be memorized.
 *
 * @author Sascha Häberling
 */
public class DataMap implements Serializable {
    private static final long serialVersionUID = -713257068596437573L;
    private static final String TAG = DataMap.class.getSimpleName();
    private List<String> keys = new ArrayList<String>();
    private Map<String, String> map = new HashMap<String, String>();

    /**
     * Parses the map from an XML file.
     *
     * @param parser
     *            The already initialized parser.
     * @return A valid {@link DataMap} instance representing the data.
     * @throws XmlPullParserException
     *             In case of an XML parsing exception.
     * @throws IOException
     *             In case of an XML parsing exception.
     */
    public static DataMap parse(XmlResourceParser parser)
            throws XmlPullParserException, IOException {
        DataMap result = new DataMap();
        int event;
        while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG) {
                if (parser.getAttributeCount() == 2) {
                    String from = parser.getAttributeValue(0);
                    String to = parser.getAttributeValue(1);
                    result.addMapping(from, to);
                    Log.d(TAG, "Mapping: " + from + " -> " + to);
                }
            }
        }
        return result;
    }

    private DataMap() {
    }

    /**
     * Return the amount of tuples.
     */
    public int size() {
        return keys.size();
    }

    /**
     * Returns a random key value from the set.
     */
    public String getRandomKey() {
        Random random = new Random();
        int i = random.nextInt(keys.size());
        return getKey(i);
    }

    /**
     * Returns the key at the given position.
     */
    public String getKey(int i) {
        return keys.get(i);
    }

    /**
     * Returns the value for the given key.
     */
    public String getValue(String key) {
        return map.get(key);
    }

    /**
     * Adds a new mapping.
     */
    private void addMapping(String from, String to) {
        if (!map.containsKey(from)) {
            map.put(from, to);
            keys.add(from);
        }
    }

    /**
     * Inverts the mapping.
     */
    public void invert() {
        List<String> newKeys = new ArrayList<String>(map.values());
        Map<String, String> newMap = new HashMap<String, String>();

        for (String newKey : newKeys) {
            for (String oldKey : map.keySet()) {
                String oldValue = map.get(oldKey);
                if (newKey.equals(oldValue)) {
                    newMap.put(oldValue, oldKey);
                }
            }
        }

        this.keys = newKeys;
        this.map = newMap;
    }
}
