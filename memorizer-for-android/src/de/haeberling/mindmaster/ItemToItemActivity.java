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

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import de.haeberling.mindmaster.data.DataMap;

/**
 * The activity which display the main UI for training the key/value pairs.
 *
 * @author Sascha Häberling
 */
public class ItemToItemActivity extends Activity {
    private static final String TAG = ItemToItemActivity.class.getSimpleName();

    private final Random random = new Random();
    private DataMap data;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // No title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Switch to fullscreen view, getting rid of the status bar as well.
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.item_to_item);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getExtras().getCharSequence("title"));
        data = (DataMap) getIntent().getExtras().get("data");
        installListeners();

        // Load initial data set.
        nextMapping();
    }

    private void installListeners() {
        findViewById(R.id.root).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked");
                nextMapping();
            }
        });
    }

    private void nextMapping() {
        Log.d(TAG, "Next Mapping");
        String solutionFrom = "-";
        String solutionTo = "-";

        if (currentIndex >= 0) {
            solutionFrom = data.getKey(currentIndex);
            solutionTo = data.getValue(solutionFrom);
        }

        currentIndex = nextIndex();
        String newFrom = data.getKey(currentIndex);
        updateView(newFrom, solutionFrom, solutionTo);
    }

    private void updateView(String newFrom, String solutionFrom,
            String solutionTo) {
        Log.d(TAG, "updateView");
        TextView fromItem = (TextView) findViewById(R.id.from_item);
        TextView solutionFromItem = (TextView) findViewById(R.id.solution_from_item);
        TextView solutionToItem = (TextView) findViewById(R.id.solution_to_item);

        fromItem.setText(newFrom);
        solutionFromItem.setText(solutionFrom);
        solutionToItem.setText(solutionTo);
    }

    private int nextIndex() {
        int result = currentIndex;
        while (result == currentIndex) {
            result = random.nextInt(data.size());
        }
        return result;
    }
}
