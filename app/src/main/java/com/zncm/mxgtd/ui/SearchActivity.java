package com.zncm.mxgtd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.ft.DetailsFragment;
import com.zncm.mxgtd.ft.LikeFragment;
import com.zncm.mxgtd.utils.XUtil;


public class SearchActivity extends BaseActivity {
    DetailsFragment fragment;
    MaterialSearchView searchView;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("搜索");
        bundle = new Bundle();
        fragment = new DetailsFragment();
        bundle.putString("query", EnumData.queryEnum._TODAY.getValue());
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setHint("搜索");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (XUtil.notEmptyOrNull(query)) {
//                    Intent newIntent = new Intent(ctx, TkDetailsActivity.class);
//                    newIntent.putExtra("query", query);
//                    startActivity(newIntent);
                    fragment = new DetailsFragment();
                    bundle.putString("query", query);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                    toolbar.setTitle(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search;
    }
}
