package ru.besuglovs.nu.brs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity {

    public static Integer StudentBarMax = 30;
    public static Integer NominalBarMax = 30;
    public static Integer StudentBarInit = 15;
    public static Integer NominalBarInit = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final SeekBar StudentScore = (SeekBar) findViewById(R.id.studentScore);
        final SeekBar NominalScore = (SeekBar) findViewById(R.id.Nominal);

        final TextView StudentScoreTextView = (TextView) findViewById(R.id.StudentScoreText);
        final TextView NominalTextView = (TextView) findViewById(R.id.NominalText);

        StudentScore.setMax(StudentBarMax);
        StudentScore.setProgress(StudentBarInit);
        StudentScoreTextView.setText(StudentBarInit.toString());

        NominalScore.setMax(NominalBarMax);
        NominalScore.setProgress(NominalBarInit);
        NominalTextView.setText(NominalBarInit.toString());

        StudentScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                UpdateCoeff();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        NominalScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 0) {
                    NominalScore.setProgress(1);
                } else {
                    UpdateCoeff();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        StudentScoreTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.Mian_StudentScore))
                        .setView(inflater.inflate(R.layout.popupdialog, null))
                        .setPositiveButton(getString(R.string.Main_OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.score);
                                String inputString = edit.getText().toString();

                                try {
                                    Integer score = Integer.parseInt(inputString);

                                    if ((score > StudentScore.getMax()) && (score <= 200)) {
                                        StudentScore.setMax(score);
                                        NominalScore.setMax(score);

                                        // Set to bogus value and back to force refresh
                                        // http://stackoverflow.com/questions/7438198/android-seekbar-doesnt-refresh-after-dynamically-setting-max-value-with-setmax
                                        Integer tmpProgress = NominalScore.getProgress();
                                        NominalScore.setProgress(1);
                                        NominalScore.setProgress(tmpProgress);
                                    }
                                    if ((score >= 0) && (score <= StudentScore.getMax())) {

                                        StudentScore.setProgress(score);
                                        StudentScoreTextView.setText(inputString);
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.Main_Cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Do nothing.
                            }
                        })
                        .show();
                return true;
            }
        });

        NominalTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.Main_NominalScore))
                        .setView(inflater.inflate(R.layout.popupdialog, null))
                        .setPositiveButton(getString(R.string.Main_OK), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.score);
                                String inputString = edit.getText().toString();

                                try {
                                    Integer score = Integer.parseInt(inputString);

                                    if ((score > StudentScore.getMax()) && (score <= 200)) {
                                        StudentScore.setMax(score);
                                        NominalScore.setMax(score);

                                        // Set to bogus value and back to force refresh
                                        // http://stackoverflow.com/questions/7438198/android-seekbar-doesnt-refresh-after-dynamically-setting-max-value-with-setmax
                                        Integer tmpProgress = StudentScore.getProgress();
                                        StudentScore.setProgress(1);
                                        StudentScore.setProgress(tmpProgress);
                                    }
                                    if ((score >= 0) && (score <= NominalScore.getMax())) {
                                        NominalScore.setProgress(score);
                                        NominalTextView.setText(inputString);
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.Main_Cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Do nothing.
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    private void UpdateCoeff() {
        final SeekBar StudentScore = (SeekBar) findViewById(R.id.studentScore);
        final SeekBar NominalScore = (SeekBar) findViewById(R.id.Nominal);
        final TextView Coeff = (TextView) findViewById(R.id.Coeff);

        TextView studentText = (TextView) findViewById(R.id.StudentScoreText);
        Integer StudentScoreInt = StudentScore.getProgress();

        TextView NominalText = (TextView) findViewById(R.id.NominalText);
        Integer NominalInt = NominalScore.getProgress();

        studentText.setText(StudentScoreInt.toString());
        NominalText.setText(NominalInt.toString());

        String CoeffString;
        Double CoeffDouble = 0.0;
        DecimalFormat df = new DecimalFormat("0.00");
        if (NominalInt != 0) {
            CoeffDouble = 0.5 + (StudentScoreInt.doubleValue() / (2 * NominalInt));

            CoeffDouble = (double) Math.round(CoeffDouble * 100) / 100;

            CoeffString = df.format(CoeffDouble);
        } else {
            CoeffString = getString(R.string.ERROR);
        }

        Coeff.setText(CoeffString);

        long TwoConvertsIn = MultiplyAndRoundBetweenTwoAndFive(2, CoeffDouble);
        long ThreeConvertsIn = MultiplyAndRoundBetweenTwoAndFive(3, CoeffDouble);
        long FourConvertsIn = MultiplyAndRoundBetweenTwoAndFive(4, CoeffDouble);
        long FiveConvertsIn = MultiplyAndRoundBetweenTwoAndFive(5, CoeffDouble);

        DecimalFormat dfMark = new DecimalFormat("0");
        String TwoString = dfMark.format(TwoConvertsIn);
        String ThreeString = dfMark.format(ThreeConvertsIn);
        String FourString = dfMark.format(FourConvertsIn);
        String FiveString = dfMark.format(FiveConvertsIn);

        TextView mark2 = (TextView) findViewById(R.id.textViewMark22);
        LinearLayout l2 = (LinearLayout) findViewById(R.id.layoutMark2);
        SetLayoutColor(l2, TwoString);

        TextView mark3 = (TextView) findViewById(R.id.textViewMark32);
        LinearLayout l3 = (LinearLayout) findViewById(R.id.layoutMark3);
        SetLayoutColor(l3, ThreeString);

        TextView mark4 = (TextView) findViewById(R.id.textViewMark42);
        LinearLayout l4 = (LinearLayout) findViewById(R.id.layoutMark4);
        SetLayoutColor(l4, FourString);

        TextView mark5 = (TextView) findViewById(R.id.textViewMark52);
        LinearLayout l5 = (LinearLayout) findViewById(R.id.layoutMark5);
        SetLayoutColor(l5, FiveString);

        mark2.setText(TwoString);
        mark3.setText(ThreeString);
        mark4.setText(FourString);
        mark5.setText(FiveString);

        TextView Multiplied2 = (TextView) findViewById(R.id.result2);
        TextView Multiplied3 = (TextView) findViewById(R.id.result3);
        TextView Multiplied4 = (TextView) findViewById(R.id.result4);
        TextView Multiplied5 = (TextView) findViewById(R.id.result5);

        String Multiplied2String = MultiplyAndRound(2, CoeffDouble);
        String Multiplied3String = MultiplyAndRound(3, CoeffDouble);
        String Multiplied4String = MultiplyAndRound(4, CoeffDouble);
        String Multiplied5String = MultiplyAndRound(5, CoeffDouble);

        Multiplied2.setText(Multiplied2String);
        Multiplied3.setText(Multiplied3String);
        Multiplied4.setText(Multiplied4String);
        Multiplied5.setText(Multiplied5String);
    }

    private void SetLayoutColor(LinearLayout l, String mark) {

        if (mark.equals("2")) {
            l.setBackgroundColor(Color.RED);
        }
        if (mark.equals("3")) {
            l.setBackgroundColor(Color.YELLOW);
        }
        if (mark.equals("4")) {
            l.setBackgroundColor(Color.BLUE);
        }
        if (mark.equals("5")) {
            l.setBackgroundColor(Color.GREEN);
        }
    }

    private long MultiplyAndRoundBetweenTwoAndFive(int mark, Double coeffDouble) {
        long result = Math.round(mark * coeffDouble);
        if (result > 5) {
            result = 5;
        }
        if (result < 2) {
            result = 2;
        }

        return result;
    }

    private String MultiplyAndRound(Integer mark, Double coeffDouble) {
        Double result = (double) (Math.round(mark.doubleValue() * coeffDouble * 100)) / 100;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(result);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_About) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
