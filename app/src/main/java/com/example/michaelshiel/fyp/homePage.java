package com.example.michaelshiel.fyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
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

    private ListView mRecipeListView;
    private static final int RC_SIGN_IN = 1;

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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSnackDatabaseReference = mFirebaseDatabase.getReference().child("snacks");
        mBreakfastDatabaseReference = mFirebaseDatabase.getReference().child("breakfast");
        mLunchReference = mFirebaseDatabase.getReference().child("lunch");
        mDinnerReference = mFirebaseDatabase.getReference().child("dinner");
        mTescoPricesReference = mFirebaseDatabase.getReference().child("tescoPrices");
        mSupervaluPricesReference = mFirebaseDatabase.getReference().child("supervaluPrices");
        mAldiReference = mFirebaseDatabase.getReference().child("aldiPrices");
        mUserReference = mFirebaseDatabase.getReference().child("user");
        mGroceryListReference = mFirebaseDatabase.getReference().child("groceryList");
       // new doit().execute();
        mRecipeListView = (ListView) findViewById(R.id.recipeListView);

//        ValueEventListener recipeListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                Recipe recipe = dataSnapshot.getValue(Recipe.class);
//                // ...
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//        mBreakfastDatabaseReference.addValueEventListener(recipeListener);
//       // mRecipeListView.setAdapter(adapter);
        mBreakfastDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds: dataSnapshot.getChildren())
        {

            Recipe recipe1 = new Recipe();
            recipe1.setName(ds.getValue(Recipe.class).getName());
            recipe1.setCalories(ds.getValue(Recipe.class).getCalories());
            recipe1.setPrepTime(ds.getValue(Recipe.class).getPrepTime());
            recipe1.setCookTime(ds.getValue(Recipe.class).getCookTime());
            recipe1.setProtein(ds.getValue(Recipe.class).getProtein());
            recipe1.setCarbs(ds.getValue(Recipe.class).getCarbs());
            recipe1.setFat(ds.getValue(Recipe.class).getFat());
            recipe1.setMeasurement(ds.getValue(Recipe.class).getMeasurement());
            recipe1.setIngredients(ds.getValue(Recipe.class).getIngredients());
            recipe1.setInstructions(ds.getValue(Recipe.class).getInstructions());
            recipe1.setSurvingSuggestion(ds.getValue(Recipe.class).getSurvingSuggestion());
            recipe1.setWriter(ds.getValue(Recipe.class).getWriter());
            recipe1.setUrl(ds.getValue(Recipe.class).getUrl());

            ArrayList array = new ArrayList<>();
            array.add(recipe1.getName());
            array.add(recipe1.getCalories());
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,array);
            mRecipeListView.setAdapter(adapter);
        }
    }


    public class doit extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://www.muscleandstrength.com/recipes/mini-chocolate-coconut-protein-cookies";
                Document document = Jsoup.connect(url).get();

                getRecipe(document, url);
                //TescoPrices(document, url);
                //SupervaluPrices(document, url);
                //AldiPrices(document, url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Recipe theRecipe = new Recipe(name, mCalories, mPrepTime, mCookTime, mProtein, mCarbs, mFat, mMeasurement,
                    mIngredients, mInstructions, mSurvingSuggestion, mWriter, mUrl, mSnackDatabaseReference);
            mSnackDatabaseReference.push().setValue(theRecipe);

          //  TescoPrice price = new TescoPrice(name,mAmount,mCurrency, mPrice);
          //  mTescoPricesReference.push().setValue(price);

        }


        public void getRecipe( Document document, String url) throws IOException
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

            //When amounts were at the end
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

            //When amounts were at the start
            Elements ingredients = document.getElementsByClass("recipe-check-list").first().getElementsByTag("li");
            for (Element el : ingredients) {
                List singleRecipe = new ArrayList<>();
                List<String> amounts = new ArrayList<>();
                Element recipe3 = el.select("[itemprop=recipeIngredient]").first();
                //System.out.println( recipe3.text());
                String test = recipe3.text();
                Scanner scan = new Scanner(test);
                //scan.useDelimiter(", ");
                int i=0;
                String rest = "";
                if(scan.hasNext() &&  i==0)
                {
                    String s = scan.next();
                    if (s.matches(".*\\d+.*")|| s.contains("½")||s.contains("¼")||s.contains("⅓")||s.contains("¾"))
                    {
                        String st = extractNumber(s);
                        String temp = scan.next();
                        if(temp.equals("scoops")||temp.equals("scoop")||temp.equals("ounces")||temp.equals("tbsp")||temp.equals("cups")||
                                temp.equals("cup")||temp.equals("ounce")||temp.equals("tsps")||temp.equals("tsp")||temp.equals("oz")
                                ||temp.equals("tbs")||temp.equals("tablespoon")||temp.equals("grams")||temp.equals("tbsp.")
                                ||temp.equals("slices")||temp.equals("lbs")||temp.equals("packet")||temp.equals("Cans")
                                ||temp.equals("lb")||temp.equals("batch")||temp.equals("bag")||temp.equals("tsp.")||temp.equals("pinch"))
                        {
                            String amount = extractServing(temp);
                            amounts.add(st);
                            amounts.add(amount);
                        }
                        else{
                            rest = rest.concat(temp);
                            amounts.add(st);
                        }

                    }
                    else
                    {
                        rest = rest.concat(s);
                    }
                }

                while(scan.hasNext())
                {
                    String temp = scan.next();
                    if(rest.equals("")==false)
                    {
                        rest = rest.concat(" " + temp);
                    }
                    else if(temp.equals("of")==false)
                    {
                        rest = rest.concat( temp);
                    }

                }
                singleRecipe.add(rest);
                singleRecipe.add(amounts);
                mIngredients.add(singleRecipe);
            }

//            //Bullet Points
//            Elements instructions = document.select("[itemprop=recipeInstructions]").first().getElementsByTag("li");
//            for (Element el : instructions)
//            {
//                Element recipe4 = el.getElementsByTag("li").first();
//                mInstructions.add(recipe4.text());
//            }

//            //Unordered List
//            Elements instructions = document.select("[itemprop=recipeInstructions]").first().getElementsByTag("p");
//            for (Element el : instructions)
//            {
//                Element recipe4 = el.getElementsByTag("p").first();
//                mInstructions.add(recipe4.text());
//            }

            //Ordered List
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

        public void TescoPrices(Document document, String url){
            //mAmount = new ArrayList<>();
            Elements info = document.getElementsByClass("productLists").first().getElementsByTag("li");
            int i = 0;
            for (Element el : info) {
                if (el.getElementsByClass("linePrice").first() != null)
                {
                    mAmount = new ArrayList<>();
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
                                    mWeight = "Grams";
                                }
                            }

                        }
                        else if(amount<=10)
                        {
                            mWeight = "KG";

                        }
                        else if(amount>10)
                        {
                            mWeight = "Grams";
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

        public void SupervaluPrices(Document document, String url){
            Elements info = document.getElementsByClass("row product-list ga-impression-group").first().getElementsByClass("product-list-item-details-title-link ga-product-link");
            int i = 0;
            for (Element el : info) {
                mAmount = new ArrayList<>();
                Element names = el.getElementsByClass("product-list-item-details-title").first();
                Element linePrice = el.getElementsByClass("product-details-price-item").first();
                Element linePriceAbbrv = el.getElementsByClass("product-details-price-per-kg").first();
                String fullPrice = linePrice.text();
                mCurrency = "€";
                mPrice = extractNumber(fullPrice);
                name = names.text();
                String amount = null;
                String serving = null;
                if (name.contains("(")) {
                    String[] values = name.split("[\\(||\\)]");
                    if (values[1] != null) {
                        String amountString = values[1];
                        if (amountString.matches(".*\\d+.*")) {
                            amount = extractNumber(amountString);
                            mWeight = extractServing(amountString);
                        }
                    }
                } else if (name.contains("Loose")) {
                    amount = "1";
                } else if (linePriceAbbrv.text().equals("per kg")) {
                    amount = "1";
                    mWeight = "KG";
                }
                mAmount.add(amount);
                if(mWeight!=null)
                {
                    mAmount.add(mWeight);
                }
                TescoPrice price = new TescoPrice(name,mAmount,mCurrency, mPrice);
                mSupervaluPricesReference.push().setValue(price);
            }
        }

        public void AldiPrices(Document document, String url){
            Elements info = document.getElementsByClass("col-xs-12 col-sm-12 category-grid category-grid--groceries").first().getElementsByClass(" category-item invisible js-category-item");;
            int i = 0;
            for (Element el : info) {
                mAmount = new ArrayList<>();
                Element names = el.getElementsByClass("category-item__title").first();
                Element linePrice = el.getElementsByClass("category-item__price js-category-item-price").first();
                Element linePriceAbbrv = el.getElementsByClass("category-item__pricePerUnit").first();
                String fullPrice = linePrice.text();
                String perKg = linePriceAbbrv.text();
                double amount=0;
                double x = 0;
                String priceNumber=null;
                if( perKg.equals("")==false)
                {
                    if(perKg.contains("/"))
                    {
                        Scanner scan= new Scanner(perKg);
                        scan.useDelimiter("/");
                        if(scan.hasNext())
                        {
                            perKg = scan.next();
                        }
                    }
                    Scanner scan = new Scanner(fullPrice);
                    scan.useDelimiter("€");
                    String scanned = scan.next();
                    priceNumber = extractNumber(scanned);
                    String perKgNo = extractNumber(perKg);
                    x = Double.parseDouble(priceNumber);
                    double y = Double.parseDouble(perKgNo);
                    y= y/1000;
                    amount = x/y;
                    double mweight = amount - (int) amount;
                    if(mweight>=.5)
                    {
                        amount = Math.ceil(amount);
                    }
                    else{
                        amount = (int) amount;
                    }

                }
                mPrice = "" + x;
                name = names.text();
                mWeight = "Grams";
                mCurrency = "€";
                mAmount.add(amount);
                mAmount.add(mWeight);
                TescoPrice price = new TescoPrice(name,mAmount,mCurrency, mPrice);
                mAldiReference.push().setValue(price);
            }
        }


        public String extractNumber(final String str) {

            if(str == null || str.isEmpty()) return "";

            StringBuilder sb = new StringBuilder();
            char lastChar = ' ';
            for(char c : str.toCharArray()){
                if(Character.isDigit(c)||c=='.'||c=='/'||c=='-'){
                    sb.append(c);
                    lastChar = c;
                }
                else if(c=='½'&&Character.isDigit(lastChar))
                {
                    sb.append(" ");
                    sb.append("1");
                    sb.append("/");
                    sb.append("2");
                }
                else if(c=='½')
                {
                    sb.append("1");
                    sb.append("/");
                    sb.append("2");
                }
                else if(c=='¼'&&Character.isDigit(lastChar))
                {
                    sb.append(" ");
                    sb.append("1");
                    sb.append("/");
                    sb.append("4");
                }
                else if(c=='¼')
                {
                    sb.append("1");
                    sb.append("/");
                    sb.append("4");
                }
                else if(c=='⅓'&&Character.isDigit(lastChar))
                {
                    sb.append(" ");
                    sb.append("1");
                    sb.append("/");
                    sb.append("3");
                }
                else if(c=='⅓')
                {
                    sb.append("1");
                    sb.append("/");
                    sb.append("3");
                }
                else if(c=='¾'&&Character.isDigit(lastChar))
                {
                    sb.append(" ");
                    sb.append("3");
                    sb.append("/");
                    sb.append("4");
                }
                else if(c=='¾')
                {
                    sb.append("3");
                    sb.append("/");
                    sb.append("4");
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
