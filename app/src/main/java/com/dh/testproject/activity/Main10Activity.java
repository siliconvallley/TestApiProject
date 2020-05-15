package com.dh.testproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.dh.testproject.R;
import com.dh.testproject.activity.viewpager.CardFragmentActivity;
import com.dh.testproject.activity.viewpager.CardViewActivity;
import com.dh.testproject.activity.viewpager.CardViewTabLayoutActivity;
import com.dh.testproject.activity.viewpager.FakeDragActivity;
import com.dh.testproject.activity.viewpager.MutableCollectionFragmentActivity;
import com.dh.testproject.activity.viewpager.MutableCollectionViewActivity;
import com.dh.testproject.adapter.RvViewpagerAdapter;
import com.dh.testproject.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main10Activity extends AppCompatActivity {
    private static final String TAG = "Main10Activity";
    private RecyclerView rv;
    private List<Map<String, Object>> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main10);

        init();
    }

    private void init() {
        rv = findViewById(R.id.rv);

        getData();

        RvViewpagerAdapter adapter = new RvViewpagerAdapter(list);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(itemClickListener);
    }

    private RvViewpagerAdapter.OnItemClickListener itemClickListener = new RvViewpagerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Intent intent) {
            startActivity(intent);
        }
    };

    private void getData() {
        list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TITLE_KEY, "ViewPager2 with Views");
        map.put(Constants.INTENT_KEY, new Intent(this, CardViewActivity.class));
        list.add(map);

        map = new HashMap<>();
        map.put(Constants.TITLE_KEY, "ViewPager2 with Fragments");
        map.put(Constants.INTENT_KEY, new Intent(this, CardFragmentActivity.class));
        list.add(map);

        map = new HashMap<>();
        map.put(Constants.TITLE_KEY, "ViewPager2 with a Mutable Collection (Views)");
        map.put(Constants.INTENT_KEY, new Intent(this, MutableCollectionViewActivity.class));
        list.add(map);

        map = new HashMap<>();
        map.put(Constants.TITLE_KEY, "ViewPager2 with a Mutable Collection (Fragments)");
        map.put(Constants.INTENT_KEY, new Intent(this, MutableCollectionFragmentActivity.class));
        list.add(map);

        map = new HashMap<>();
        map.put(Constants.TITLE_KEY, "ViewPager2 with a TabLayout (Views)");
        map.put(Constants.INTENT_KEY, new Intent(this, CardViewTabLayoutActivity.class));
        list.add(map);

        map = new HashMap<>();
        map.put(Constants.TITLE_KEY, "ViewPager2 with Fake Dragging");
        map.put(Constants.INTENT_KEY, new Intent(this, FakeDragActivity.class));
        list.add(map);

       /*
        myData.add(mapOf("title" to "ViewPager2 with PageTransformers",
                "intent" to activityToIntent(PageTransformerActivity::class.java.name)))
        myData.add(mapOf("title" to "ViewPager2 with a Preview of Next/Prev Page",
                "intent" to activityToIntent(PreviewPagesActivity::class.java.name)))
        myData.add(mapOf("title" to "ViewPager2 with Nested RecyclerViews",
                "intent" to activityToIntent(ParallelNestedScrollingActivity::class.java.name)))*/
    }
}
