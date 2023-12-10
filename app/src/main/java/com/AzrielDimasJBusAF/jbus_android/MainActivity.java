package com.AzrielDimasJBusAF.jbus_android;

//import statement
import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionBarPolicy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import com.AzrielDimasJBusAF.jbus_android.model.Bus;
import com.AzrielDimasJBusAF.jbus_android.request.BaseApiService;
import com.AzrielDimasJBusAF.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The MainActivity class manages the primary functionality of the application's main screen.
 */
public class MainActivity extends AppCompatActivity {
    // Variable declarations
    Context mContext;
    BaseApiService mApiService;
    private ListView listView;
    public static int busIndex;
    private ListView list;
    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 12; // kalian dapat bereksperimen dengan field ini
    private int listSize;
    private int noOfPages;
    public static List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;

    // Initialization of variables and UI elements
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        listView = findViewById(R.id.listView);
        // Fetch the list of buses and set up pagination
        getBusList();

        // Set listeners for pagination buttons
        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);


        paginationFooter();
        goToPage(currentPage);

        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0 ? currentPage - 1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages - 1 ? currentPage + 1 : currentPage;
            goToPage(currentPage);
        });
    }

    // Override the onCreateOptionsMenu method to create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating the action bar menu layout
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    // Override onOptionsItemSelected method to handle menu item clicks
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks

        int id = item.getItemId();

        if (id == R.id.search_button) {
            Toast.makeText(MainActivity.this, "button", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.profile_button) {
            Intent intent = new Intent(this, AboutMeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.payment_button) {
            Toast.makeText(MainActivity.this, "payment", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Generates the pagination footer based on the number of items.
     */
    private void paginationFooter() {
        // Pagination logic
        int val = listSize % pageSize;
        val = val == 0 ? 0 : 1;
        noOfPages = listSize / pageSize + val;

        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity = Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i] = new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));

            btns[i].setTextColor(getResources().getColor(R.color.blue));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }

    /**
     * Navigates to the selected page.
     *
     * @param index The index of the page to navigate to.
     */
    private void goToPage(int index) {
        // Logic to navigate to the selected page
        for (int i = 0; i < noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listBus, currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }

    /**
     * Scrolls to the selected item within the pagination footer.
     *
     * @param item The item to be scrolled to.
     */
    private void scrollToItem(Button item) {
        // Logic to scroll to the selected item
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) /
                2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    /**
     * Displays the paginated list of buses on the screen.
     *
     * @param listBus The list of buses to be displayed.
     * @param page    The current page index.
     */
    private void viewPaginatedList(List<Bus> listBus, int page) {
        // Logic to display the paginated list of buses
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        BusArrayAdapter paginatedAdapter = new BusArrayAdapter(this, R.layout.bus_view, paginatedList);
        list.setAdapter(paginatedAdapter);
    }

    /**
     * Fetches the list of buses from the API.
     */
    private void getBusList() {
        // Making an API call to retrieve the list of buses
        mApiService.getAllBus().enqueue(new Callback<List<Bus>>() {
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Bus> busList = response.body();
                listBus = busList;
                busArrayAdapter arrayAdapter = new busArrayAdapter(mContext,busList);
                listView.setAdapter(arrayAdapter);
                listSize = busList.size();


            }

            public void onFailure(Call<List<Bus>> call, Throwable t) {

            }


        });


    }

    /**
     * Custom ArrayAdapter to populate the ListView with Bus objects.
     */
    private class busArrayAdapter extends ArrayAdapter<Bus> {
        // Constructor and overridden getView method

        public busArrayAdapter(@NonNull Context context, @NonNull List<Bus> busList) {
            super(context, 0, busList);
            mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItemView = convertView;


            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
            }
            Bus currentBus = getItem(position);

            TextView busName = listItemView.findViewById(R.id.bistxt);
            busName.setText(currentBus.name);


            ImageView addSched = listItemView.findViewById(R.id.bisview);
            addSched.setOnClickListener(v -> {
                Intent i = new Intent(mContext, BusDetailActivity.class);
                busIndex = currentBus.id;
                i.putExtra("busId", currentBus.id);
                mContext.startActivity(i);
            });

            return listItemView;


        }

    }

    /**
     * Starts a new activity.
     *
     * @param ctx The current context.
     * @param cls The class of the activity to be started.
     */
    private void moveActivity(Context ctx, Class<?> cls) {
        // Method to start a new activity
        Intent intent = new Intent(ctx, cls);
        mContext.startActivity(intent);
    }

}