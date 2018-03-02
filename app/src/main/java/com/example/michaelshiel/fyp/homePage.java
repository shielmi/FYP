package com.example.michaelshiel.fyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class homePage extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSnackDatabaseReference;
    private DatabaseReference mInstructionsDatabaseReference;
    TextView texx;
    String name;
    String mPrepTime;
    String mCookTime;
    String mCalories;
    String mProtein;
    String mCarbs;
    String mFat;
    String mMeasurement;
    String mSurvingSuggestion;
    List<List> mRecipe = new ArrayList();
    List<String> mInstructions = new ArrayList();

    String mWriter;
    String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        texx = (TextView) findViewById(R.id.tex1);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSnackDatabaseReference = mFirebaseDatabase.getReference().child("snacks");
        mInstructionsDatabaseReference = mFirebaseDatabase.getReference().child("ingredients");
        new doit().execute();


    }


    public class doit extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://www.muscleandstrength.com/recipes/whey-honey-peanut-butter-protein-bar";
                Document document = Jsoup.connect(url).get();

                //OrderedList(document, url);
                UnorderedList(document, url);
                //texx.setText(words);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Recipe theRecipe = new Recipe(name, mCalories, mPrepTime, mCookTime, mProtein, mCarbs, mFat, mMeasurement,
                    mRecipe, mInstructions, mSurvingSuggestion, mWriter, mUrl, mSnackDatabaseReference);
            mSnackDatabaseReference.push().setValue(theRecipe);

        }

        public void UnorderedList( Document document, String url) throws IOException
        {


            name = document.title();

//		Elements prepTime = document.getElementsByClass("recipe-time-bar left").first().getElementsByClass("info");
//            mPrepTime = prepTime.text();
//
//		Elements cookTime = document.getElementsByClass("recipe-time-bar right").first().getElementsByClass("info");
//        mCookTime = cookTime.text();
            mPrepTime = null;
            mCookTime = null;

            Elements calories = document.getElementsByClass("field field-name-field-recipe-calories field-type-text field-label-hidden");
         mCalories = calories.text();


            Elements protein = document.select("[itemprop=proteinContent]");
            Element measure = document.getElementsByClass("amount").first();
            mProtein = protein.text();
         mMeasurement = measure.text();

            Elements carbs = document.select("[itemprop=carbohydrateContent]");
           mCarbs = carbs.text();


            Elements fat = document.select("[itemprop=fatContent]");
            mFat = fat.text();



        Elements recipe = document.getElementsByClass("recipe-check-list").first().getElementsByTag("li");
        for (Element el : recipe) {
            List<String> singleRecipe = new ArrayList<>();
            Element recipe3 = el.select("[itemprop=recipeIngredient]").first();
            //mRecipe.add(recipe3.text());
            String test = recipe3.text();
            Scanner scan = new Scanner(test);
            scan.useDelimiter(", ");
            while(scan.hasNext())
            {
                String s = scan.next();
                if (s.matches(".*\\d+.*"))
                {
                    String st = extractNumber(s);
                    //int a = Integer.parseInt(st);
                    String amount = extractServing(s);
                    singleRecipe.add(st);
                    singleRecipe.add(amount);
                    //System.out.println(a + " " + amount);

                }
                else
                {
                    singleRecipe.add(s);
                }
            }
            mRecipe.add(singleRecipe);

        }

        Elements instructions = document.select("[itemprop=recipeInstructions]").first().getElementsByTag("p");
        for (Element el : instructions) {

            Element recipe4 = el.getElementsByTag("p").first();
            mInstructions.add(recipe4.text());
        }

        Elements yield = document.select("[itemprop=recipeYield]").first().getElementsByTag("p");
        mSurvingSuggestion = yield.text();

        Elements author =  document.getElementsByClass("aboutAuthor").first().getElementsByClass("name");
        mWriter =  author.text();

        mUrl = url;
        }

        public String extractNumber(final String str) {

            if(str == null || str.isEmpty()) return "";

            StringBuilder sb = new StringBuilder();
            for(char c : str.toCharArray()){
                if(Character.isDigit(c)){
                    sb.append(c);

                }
            }

            return sb.toString();
        }
        public String extractServing(final String str) {

            if(str == null || str.isEmpty()) return "";

            StringBuilder sb = new StringBuilder();
            for(char c : str.toCharArray()){
                if(Character.isDigit(c)==false && c != ' '){
                    sb.append(c);

                }
            }

            return sb.toString();
        }
    }
}
