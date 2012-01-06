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

package de.haeberling.mindmaster;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import de.haeberling.mindmaster.data.DataMap;

/**
 * The main activity of this Mnemonic training app.
 *
 * @author Sascha Häberling
 */
public class MindmasterActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Switch to fullscreen view, getting rid of the status bar as well.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);
        addListeners();
    }

    private void addListeners() {
        addListener(R.id.decimal_word, "decimal_to_word.xml", false);
        addListener(R.id.word_decimal, "decimal_to_word.xml", true);
        addListener(R.id.binary_word, "binary_to_word.xml", false);
        addListener(R.id.word_binary, "binary_to_word.xml", true);
    }

    private void addListener(int id, final String name,
            final boolean invertMapping) {
        final AssetManager assetManager = getAssets();
        final TextView view = (TextView) this.findViewById(id);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DataMap data = DataMap.parse(assetManager
                            .openXmlResourceParser("res/xml/" + name));
                    if (invertMapping) {
                        data.invert();
                    }
                    startItemToItemActivity(data, view.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startItemToItemActivity(DataMap data, CharSequence title) {
        Intent intent = new Intent(this, ItemToItemActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}