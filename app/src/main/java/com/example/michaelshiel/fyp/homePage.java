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
    private DatabaseReference mBreakfastDatabaseReference;
    private DatabaseReference mLunchReference;
    private DatabaseReference mDinnerReference;
    private DatabaseReference mTescoPricesReference;
    private DatabaseReference mSupervaluPricesReference;
    private DatabaseReference mAldiReference;
    private DatabaseReference mUserReference;
    private DatabaseReference mGroceryListReference;


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
    String mPrice;
    String mCurrency;
    String mWeight;
    List mAmount;
    List<List> mIngredients;
    List<String> mInstructions;

    String mWriter;
    String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        texx = (TextView) findViewById(R.id.tex1);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSnackDatabaseReference = mFirebaseDatabase.getReference().child("snacks");
        mBreakfastDatabaseReference = mFirebaseDatabase.getReference().child("breakfast");
        mLunchReference = mFirebaseDatabase.getReference().child("lunch");
        mDinnerReference = mFirebaseDatabase.getReference().child("dinner");
        mTescoPricesReference = mFirebaseDatabase.getReference().child("tescoPrices");
        mSupervaluPricesReference = mFirebaseDatabase.getReference().child("supervalu prices");
        mAldiReference = mFirebaseDatabase.getReference().child("aldi prices");
        mUserReference = mFirebaseDatabase.getReference().child("user");
        mGroceryListReference = mFirebaseDatabase.getReference().child("grocery list");
        new doit().execute();


    }


    public class doit extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://www.tesco.ie/groceries/product/browse/default.aspx?N=4294954026&Ne=4294954028";
                Document document = Jsoup.connect(url).get();

                //OrderedList(document, url);
                //UnorderedList(document, url);
                TescoPrices(document, url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            Recipe theRecipe = new Recipe(name, mCalories, mPrepTime, mCookTime, mProtein, mCarbs, mFat, mMeasurement,
//                    mIngredients, mInstructions, mSurvingSuggestion, mWriter, mUrl, mSnackDatabaseReference);
//            mSnackDatabaseReference.push().setValue(theRecipe);

          //  TescoPrice price = new TescoPrice(name,mAmount,mCurrency, mPrice);
          //  mTescoPricesReference.push().setValue(price);

        }
        public void OrderedList( Document document, String url) throws IOException
        {
            mIngredients = new ArrayList();
            mInstructions = new ArrayList();
            name = document.title();

//            Elements prepTime = document.getElementsByClass("recipe-time-bar left").first().getElementsByClass("info");
//            mPrepTime = prepTime.text();
//
//            Elements cookTime = document.getElementsByClass("recipe-time-bar right").first().getElementsByClass("info");
//            mCookTime = cookTime.text();
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



//            Elements ingredients = document.getElementsByClass("recipe-check-list").first().getElementsByTag("li");
//            for (Element el : ingredients) {
//                List<String> singleRecipe = new ArrayList<>();
//                Element recipe3 = el.select("[itemprop=recipeIngredient]").first();
//                //mRecipe.add(recipe3.text());
//                String test = recipe3.text();
//                Scanner scan = new Scanner(test);
//                scan.useDelimiter(", ");
//                while(scan.hasNext())
//                {
//                    String s = scan.next();
//                    if (s.matches(".*\\d+.*"))
//                    {
//                        String st = extractNumber(s);
//                        String amount = extractServing(s);
//                        singleRecipe.add(st);
//                        singleRecipe.add(amount);
//                    }
//                    else
//                    {
//                        singleRecipe.add(s);
//                    }
//                }
//                mIngredients.add(singleRecipe);
//
//            }

            Elements ingredients = document.getElementsByClass("recipe-check-list").first().getElementsByTag("li");
            for (Element el : ingredients) {
                List singleRecipe = new ArrayList<>();
                List<String> amounts = new ArrayList<>();
                Element recipe3 = el.select("[itemprop=recipeIngredient]").first();

                String test = recipe3.text();
                Scanner scan = new Scanner(test);
                int i=0;
                if(scan.hasNext() &&  i==0)
                {
                    String s = scan.next();
                    if (s.matches(".*\\d+.*"))
                    {
                        String st = extractNumber(s);

                        String amount = extractServing(scan.next());
                        amounts.add(amount);
                        amounts.add(st);

                    }
                }
                String rest = "";
                while(scan.hasNext())
                {
                    String temp = scan.next();
                    if(rest.equals("")==false)
                    {
                        rest = rest.concat(" " + temp);
                    }
                    else
                    {
                        rest = rest.concat( temp);
                    }

                }
                singleRecipe.add(rest);
                singleRecipe.add(amounts);
            }

            Elements instructions = document.getElementsByTag("ol").first().getElementsByTag("li");
            for (Element el : instructions) {

                Element recipe4 = el.getElementsByTag("li").first();
                mInstructions.add(recipe4.text());

            }

            Elements yield = document.select("[itemprop=recipeYield]").first().getElementsByTag("p");
            mSurvingSuggestion = yield.text();

            Elements author =  document.getElementsByClass("aboutAuthor").first().getElementsByClass("name");
            mWriter =  author.text();

            mUrl = url;
        }

        public void UnorderedList( Document document, String url) throws IOException
        {
            mIngredients = new ArrayList();
            mInstructions = new ArrayList();
            name = document.title();

//            Elements prepTime = document.getElementsByClass("recipe-time-bar left").first().getElementsByClass("info");
//            mPrepTime = prepTime.text();
//
//            Elements cookTime = document.getElementsByClass("recipe-time-bar right").first().getElementsByClass("info");
//            mCookTime = cookTime.text();
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


//            Elements ingredients = document.getElementsByClass("recipe-check-list").first().getElementsByTag("li");
//            for (Element el : ingredients) {
//               List<String> singleRecipe = new ArrayList<>();
//                Element recipe3 = el.select("[itemprop=recipeIngredient]").first();
//                //mRecipe.add(recipe3.text());
//                String test = recipe3.text();
//                Scanner scan = new Scanner(test);
//                scan.useDelimiter(", ");
//                while(scan.hasNext())
//                {
//                    String s = scan.next();
//                    if (s.matches(".*\\d+.*"))
//                    {
//                        String st = extractNumber(s);
//                        String amount = extractServing(s);
//                        singleRecipe.add(st);
//                        singleRecipe.add(amount);
//                }
//                else
//                {
//                    singleRecipe.add(s);
//                }
//            }
//            mIngredients.add(singleRecipe);
//
//            }

//            Elements ingredients = document.getElementsByClass("recipe-check-list").first().getElementsByTag("li");
//            for (Element el : ingredients) {
//                List singleRecipe = new ArrayList<>();
//                List<String> amounts = new ArrayList<>();
//                Element recipe3 = el.select("[itemprop=recipeIngredient]").first();
//                //System.out.println( recipe3.text());
//                String test = recipe3.text();
//                Scanner scan = new Scanner(test);
//                //scan.useDelimiter(", ");
//                int i=0;
//                String rest = "";
//                if(scan.hasNext() &&  i==0)
//                {
//                    String s = scan.next();
//                    if (s.matches(".*\\d+.*"))
//                    {
//                        String st = extractNumber(s);
//                        String temp = scan.next();
//                        if(temp.equals("scoops")||temp.equals("ounces")||temp.equals("tbsp")||temp.equals("cups")
//                                ||temp.equals("ounce")||temp.equals("tsps")||temp.equals("tsp"))
//                        {
//                            String amount = extractServing(temp);
//                            amounts.add(st);
//                            amounts.add(amount);
//                        }
//                        else{
//                            rest = rest.concat(temp);
//                            amounts.add(st);
//                        }
//
//                    }
//                }
//
//                while(scan.hasNext())
//                {
//                    String temp = scan.next();
//                    if(rest.equals("")==false)
//                    {
//                        rest = rest.concat(" " + temp);
//                    }
//                    else
//                    {
//                        rest = rest.concat( temp);
//                    }
//
//                }
//                singleRecipe.add(rest);
//                singleRecipe.add(amounts);
//                mIngredients.add(singleRecipe);
//            }

            Elements instructions = document.select("[itemprop=recipeInstructions]").first().getElementsByTag("p");
            for (Element el : instructions)
            {
                Element recipe4 = el.getElementsByTag("p").first();
                mInstructions.add(recipe4.text());
            }

            Elements yield = document.select("[itemprop=recipeYield]").first().getElementsByTag("p");
            mSurvingSuggestion = yield.text();

            Elements author =  document.getElementsByClass("aboutAuthor").first().getElementsByClass("name");
            mWriter =  author.text();

            mUrl = url;
        }

        public void TescoPrices(Document document, String url){
            //mAmount = new ArrayList<>();
            Elements info = document.getElementsByClass("productLists").first().getElementsByTag("li");
            int i = 0;
            for (Element el : info) {
                if (el.getElementsByClass("linePrice").first() != null)
                {
                    mAmount = new ArrayList<>();
                    List amounts = new ArrayList<>();
                    String weight = null;
                    //outputStream.println("		\"price" + i + "\": {");
                    Element names = el.getElementsByTag("a").first();
                    name = names.text();
                    int amount = 0;
                    if(name.contains("Each")||name.contains("Loose"))
                    {
                        amount =1;
                    }
                    else if (name.matches(".*\\d+.*"))
                    {
                        String st = extractNumber(name);
                        amount = Integer.parseInt(st);
                        //int last = 0;
                        if(name.contains("Pack"))
                        {
                            Scanner scan = new Scanner(name);
                            while(scan.hasNext())
                            {
                                //System.out.print(scan.next());
                                if(scan.next().equals("Pack")&&scan.hasNext() ==false)
                                {
                                    mWeight=null;
                                    break;
                                }
                                else if(amount<=10)
                                {
                                    mWeight = "KG";

                                }
                                else if(amount>10)
                                {
                                    mWeight = "G";
                                }
                            }

                        }
                        else if(amount<=10)
                        {
                            mWeight = "KG";

                        }
                        else if(amount>10)
                        {
                            mWeight = "G";
                        }
                    }
                    Element linePrice = el.getElementsByClass("linePrice").first();
                    String temp = linePrice.text();
                    mCurrency = "€";
                    mPrice = extractNumber(temp);
                    //Element linePriceAbbrv = el.getElementsByClass("linePriceAbbr").first();
                    mAmount.add(amount);
                    if(mWeight!=null)
                    {
                        mAmount.add(mWeight);
                    }
                    //mAmount.add(amounts);
                    TescoPrice price = new TescoPrice(name,mAmount,mCurrency, mPrice);
                    mTescoPricesReference.push().setValue(price);
                }

            }



        }

        public String extractNumber(final String str) {

            if(str == null || str.isEmpty()) return "";

            StringBuilder sb = new StringBuilder();
            for(char c : str.toCharArray()){
                if(Character.isDigit(c)||c=='.'){
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
