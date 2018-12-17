package com.manikshakya.hangman;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{


    LinearLayout linearLayout;
    String hiddenWord;

    // Images for the Hangman Game
    ImageView head;
    ImageView body;
    ImageView lHand;
    ImageView rHand;
    ImageView lLeg;
    ImageView rLeg;

    // Checks if the letter is found
    boolean founds;

    // Tracks the part of the body to show.
    int track;

    // Checks for the game to finish. if the count is equal to all the found letters.
    int count;

    // Gameover display
    TextView gameover;
    Button playAgain;

    public void playAgain(View view){
        linearLayout.removeAllViews();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            if (relativeLayout.getChildAt(i) instanceof TextView) {
                if(!relativeLayout.getChildAt(i).isClickable()){
                    relativeLayout.getChildAt(i).setClickable(true);
                    ((TextView) relativeLayout.getChildAt(i)).setTextColor(-1979711488);
                }

            }
        }
        generatePuzzle();
        Log.i("Button", "Clicked");
    }





    public void letterTapped(View view) {

        // Returns the id in Integer form.
        String realId = getResources().getResourceEntryName(view.getId());

        // Search TextView by ID (Integer)
        TextView tappedLetter = (TextView) findViewById(getResources().getIdentifier(realId, "id", getPackageName()));
        Log.i("Tapped Letter: ", String.valueOf(tappedLetter.getText()));

        ArrayList<View> list = new ArrayList<>();
        TextView temp;

        Log.i("Child Count", linearLayout.getChildCount() + "");

        int puzzleLength = linearLayout.getChildCount();


        for (int i = 0; i < puzzleLength; i++) {
            if (linearLayout.getChildAt(i) instanceof TextView) {
                temp = (TextView) linearLayout.getChildAt(i);
                if (String.valueOf(tappedLetter.getText()).equals(Character.toString(hiddenWord.charAt(i)))) {
                    temp.setText(String.valueOf(tappedLetter.getText()));
                    founds = true;
                    count++;
                }

            }
        }

        if(count == puzzleLength){
            String html = "<font><big><big><big><big>You Win</big></big></big></big></font>";

            gameover.setText(Html.fromHtml(html));

            RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams) gameover.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            gameover.setLayoutParams(layoutParams);
            gameover.setVisibility(View.VISIBLE);

            playAgain.setVisibility(View.VISIBLE);
            disableKeyboard();
            playAgain.setClickable(true);
        }

        Log.i("Total List", Integer.toString(list.size()));
        Log.i("Total List1", Integer.toString(linearLayout.getChildCount()));
        Log.i("All List: ", list.toString());


        if(!founds){
            track++;
            if(track == 1){
                head.setVisibility(View.VISIBLE);
            }else if(track == 2){
                body.setVisibility(View.VISIBLE);
            }else if(track == 3){
                lHand.setVisibility(View.VISIBLE);
            }else if(track == 4){
                rHand.setVisibility(View.VISIBLE);
            }else if(track == 5){
                lLeg.setVisibility(View.VISIBLE);
            }else if(track == 6){
                rLeg.setVisibility(View.VISIBLE);
            }
        }

        tappedLetter.setClickable(false);
        tappedLetter.setTextColor(Color.RED);

        if(track == 6){

            String html = "<font><big><big><big><big>Game Over</big></big></big></big><br/><small> The Correct word is " + hiddenWord + "</small></font>";

            gameover.setText(Html.fromHtml(html));

            RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams) gameover.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            gameover.setLayoutParams(layoutParams);
            gameover.setVisibility(View.VISIBLE);

            playAgain.setVisibility(View.VISIBLE);


            disableKeyboard();

            playAgain.setClickable(true);
        }

        founds = false;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Initiate all the images
        head = (ImageView) findViewById(R.id.head);
        body = (ImageView) findViewById(R.id.body);
        lHand = (ImageView) findViewById(R.id.lHand);
        rHand = (ImageView) findViewById(R.id.rHand);
        lLeg = (ImageView) findViewById(R.id.lLeg);
        rLeg = (ImageView) findViewById(R.id.rLeg);

        founds = false;


        gameover = (TextView) findViewById(R.id.gameover);
        playAgain = (Button) findViewById(R.id.playAgain);




        generatePuzzle();


    }

    private void generatePuzzle(){
        // Make the image disappear in the beginning
        head.setVisibility(View.GONE);
        body.setVisibility(View.GONE);
        lHand.setVisibility(View.GONE);
        rHand.setVisibility(View.GONE);
        lLeg.setVisibility(View.GONE);
        rLeg.setVisibility(View.GONE);

        gameover.setVisibility(View.GONE);
        playAgain.setVisibility(View.GONE);

        linearLayout = (LinearLayout) findViewById(R.id.getPuzzle);
        track = 0;
        count = 0;


        BufferedReader reader;
        //String word = "";

        try{
            reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.dictionary)));
            String line;
            ArrayList<String> dictionary = new ArrayList<>();

            while((line = reader.readLine()) != null){
                //Log.i("Hello ", reader.readLine());
                dictionary.add(line);
            }

            int lineNumber = (int) (Math.random() * dictionary.size());

            Log.i("Word found: ", dictionary.get(lineNumber));

            String word = dictionary.get(lineNumber).trim().toUpperCase();

            hiddenWord = word;

            char[] temp = word.toCharArray();


            for(int i = 0; i < word.length(); i++){
                TextView textView = new TextView(this);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);



                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                textView.setText(getResources().getString(R.string.underline));
                //textView.setText(Character.toString(word.charAt(i)));
                textView.setPadding(5,5,5,5);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);

                linearLayout.addView(textView);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disableKeyboard(){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            if (relativeLayout.getChildAt(i) instanceof TextView) {
                if(relativeLayout.getChildAt(i).isClickable()){
                    relativeLayout.getChildAt(i).setClickable(false);
                    Log.i("Id",getResources().getResourceEntryName(relativeLayout.getChildAt(i).getId()));
                }

            }
        }
    }

}
