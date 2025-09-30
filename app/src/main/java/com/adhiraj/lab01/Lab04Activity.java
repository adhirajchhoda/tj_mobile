package com.adhiraj.lab01;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Lab04Activity extends AppCompatActivity {
    ImageButton backButton;

    private final int[] layoutIds = {
            R.layout.layout_one,
            R.layout.layout_two,
            R.layout.layout_three,
            R.layout.layout_four,
            R.layout.layout_five,
            R.layout.layout_six,
            R.layout.layout_seven
    };
    private int currentLayoutIndex = -1;
    private View.OnClickListener incrementingClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab04);

        incrementingClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                int currentValue = Integer.parseInt(textView.getText().toString());
                textView.setText(String.valueOf(currentValue + 1));
            }
        };
        initializeViewsAndListeners();
    }

    private void setupIncrementingView(int viewId) {
        View view = findViewById(viewId);
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setText("0");
            textView.setOnClickListener(incrementingClickListener);
        }
    }

    private void initializeViewsAndListeners() {
        backButton = findViewById(R.id.lab04_back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        switch (currentLayoutIndex) {
            case 0:
                setupIncrementingView(R.id.button_top_left);
                setupIncrementingView(R.id.button_top_right);
                setupIncrementingView(R.id.button_bottom_left);
                setupIncrementingView(R.id.button_bottom_right);
                break;
            case 1:
                setupIncrementingView(R.id.button_center);
                setupIncrementingView(R.id.button_top_left_guideline);
                setupIncrementingView(R.id.button_bottom_right_guideline);
                break;
            case 2:
                setupIncrementingView(R.id.fill_button);
                setupIncrementingView(R.id.middle_button);
                setupIncrementingView(R.id.bottom_button);
                break;
            case 3:
                setupIncrementingView(R.id.large_button_top_left);
                setupIncrementingView(R.id.medium_button_biased);
                setupIncrementingView(R.id.large_button_center_biased);
                setupIncrementingView(R.id.small_button_biased_right_bottom);
                setupIncrementingView(R.id.small_button_biased_overlap);
                break;
            case 4:
                setupIncrementingView(R.id.top_button_layout_five);
                break;
            case 5:
                LinearLayout layoutSixButtons = findViewById(R.id.linear_layout_horizontal_buttons);
                if (layoutSixButtons != null) {
                    for (int i = 0; i < layoutSixButtons.getChildCount(); i++) {
                        View child = layoutSixButtons.getChildAt(i);
                        if (child instanceof Button) {
                            ((Button) child).setText("0");
                            child.setOnClickListener(incrementingClickListener);
                        }
                    }
                }
                break;
            case 6:
                setupIncrementingView(R.id.button);
                LinearLayout layoutSevenCheckboxes = findViewById(R.id.linear_layout_checkboxes);
                if (layoutSevenCheckboxes != null) {
                    for (int i = 0; i < layoutSevenCheckboxes.getChildCount(); i++) {
                        View child = layoutSevenCheckboxes.getChildAt(i);
                        if (child instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) child;
                            final String checkBoxText = checkBox.getText().toString();
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    String state = isChecked ? "checked" : "unchecked";
                                    Toast.makeText(Lab04Activity.this, "Checkbox '" + checkBoxText + "' is " + state, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                break;
            default:
                if (currentLayoutIndex == -1) {
                    int[] navButtonIds = {
                            R.id.button_level_1, R.id.button_level_2, R.id.button_level_3,
                            R.id.button_level_4, R.id.button_level_5, R.id.button_level_6, R.id.button_level_7
                    };
                    for (int i = 0; i < navButtonIds.length; i++) {
                        Button navButton = findViewById(navButtonIds[i]);
                        if (navButton != null) {
                            final int targetLayoutIndex = i;
                            navButton.setOnClickListener(v -> {
                                if (targetLayoutIndex < layoutIds.length) {
                                    currentLayoutIndex = targetLayoutIndex;
                                    setContentView(layoutIds[currentLayoutIndex]);
                                    initializeViewsAndListeners();
                                }
                            });
                        }
                    }
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (currentLayoutIndex == -1) {
                return super.onTouchEvent(event);
            }

            currentLayoutIndex++;
            currentLayoutIndex %= layoutIds.length;
            setContentView(layoutIds[currentLayoutIndex]);
            initializeViewsAndListeners();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
