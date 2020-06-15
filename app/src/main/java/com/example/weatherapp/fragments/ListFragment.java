package com.example.weatherapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.adapters.CitiesAdapter;
import com.example.weatherapp.entities.City;
import com.example.weatherapp.entities.Coordinate;
import com.example.weatherapp.utils.LinearLayoutCustomManager;
import com.example.weatherapp.utils.LocationSrv;
import com.example.weatherapp.utils.RecyclerItemClickListener;
import com.example.weatherapp.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private static final String TAG = "ListFragment";
    private List<City> mCities;
    private LocationSrv mLocSrv;
    private CitiesAdapter adapter = new CitiesAdapter();
    private SearchView searchView = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar_list_fragment);
        toolbar.inflateMenu(R.menu.top_bar);
        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    search(query);
                    return true;
                }
            });
        }


        Button btn = view.findViewById(R.id.btn_getWeather);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocSrv.getLastLocation();
                setDetailFragment(mLocSrv.getmCoordinate());
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutCustomManager(this.getActivity()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ListFragment.this.getActivity(),
                        recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        City city = mCities.get(position);
                        setDetailFragment(city.getCoord());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        if (mCities == null) {
            mCities = new ArrayList<>();
            new GetCitiesFromJson(this).execute("city.list.json");
        }
        adapter.setCities(mCities);
        recyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment_layout, container, false);
        mLocSrv = new LocationSrv(this.getActivity());
        setHasOptionsMenu(true);
        return view;
    }

    private void setDetailFragment(Coordinate coordinate) {
        Fragment detailFrag = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("LOC_COORD", coordinate);
        detailFrag.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            View container_v = getActivity().findViewById(R.id.detail_fragment_container);
            if (container_v != null) {
                fragmentTransaction.replace(container_v.getId(), detailFrag);
            }
        } else {
            // In portrait
            fragmentTransaction.replace(R.id.fragment_container, detailFrag);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //<editor-fold desc="Location methods">


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mLocSrv.getPERMISSION_ID()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocSrv.getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocSrv.checkPermissions()) {
            mLocSrv.getLastLocation();
        }
    }
    //</editor-fold>

    private static class GetCitiesFromJson extends AsyncTask<String, Void, List<City>> {
        private final WeakReference<Fragment> weakReference;
        private Fragment mFragment;

        @SuppressLint("StaticFieldLeak")

        public GetCitiesFromJson(Fragment fragment) {
            weakReference = new WeakReference<Fragment>(fragment);
            mFragment = fragment;
        }

        @Override
        protected List<City> doInBackground(String... strings) {
            List<City> res = Utils.getJsonFromAssets(mFragment.getActivity(), strings[0]);
            return res;
        }

        @Override
        protected void onPostExecute(List<City> cities) {
            super.onPostExecute(cities);

            Fragment activity = weakReference.get();
            if (activity == null || activity.isDetached()) {
                return;
            }
            ((ListFragment) mFragment).mCities = cities;
            ((ListFragment) mFragment).adapter.setCities(cities);
        }
    }

    private void search(String query) {
        adapter.getFilter().filter(query);
    }
}
