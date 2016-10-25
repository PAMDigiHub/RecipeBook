package com.benoitarsenault.recipebook;

import android.location.GpsStatus;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.benoitarsenault.recipebook.dialogs.PresentationLastPageDialog;
import com.benoitarsenault.recipebook.model.Recipe;
import com.benoitarsenault.recipebook.model.RecipesProvider;

public class PresentationActivity extends AppCompatActivity implements PresentationFragment.PresentationFragmentListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private Recipe recipe;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        int recipeId = getIntent().getExtras().getInt(EditRecipeActivity.EXTRA_RECIPE_ID);
        recipe = RecipesProvider.getInstance().getItemById(recipeId,this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipe.getTitle());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new PageChangeListener());

    }

    public class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
           if(position==mSectionsPagerAdapter.getCount()-1){
               // TODO: 2016-10-25 pop a dialog
               PresentationLastPageDialog dialog = PresentationLastPageDialog.newInstance();
               dialog.show(getSupportFragmentManager(),"lastPage");
           }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_presentation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPresentationFragmentPreviousButtonClicked() {
        int previousItem = mViewPager.getCurrentItem()-1;

        if(previousItem>=0) {
            mViewPager.setCurrentItem(previousItem);
        }else{
            Toast.makeText(this, "Rendu au debut", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPresentationFragmentNextButtonClicked() {
        int nextItem = mViewPager.getCurrentItem()+1;

        if(nextItem<mSectionsPagerAdapter.getCount()) {
            mViewPager.setCurrentItem(nextItem);
        }else{
            Toast.makeText(this, "Rendu au boutte", Toast.LENGTH_SHORT).show();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PresentationFragment.newInstance(recipe.getId(),position);
        }


        @Override
        public int getCount() {
            return recipe.getSteps().size();
        }
    }
}
