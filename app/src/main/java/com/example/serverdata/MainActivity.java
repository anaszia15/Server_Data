package com.example.serverdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {
    private static final String URL_DATA = "https://jsonplaceholder.typicode.com/todos";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Items_List> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //phle yha tha ..to yha adapter ==null tha ..u cant use
        listItems = new ArrayList<>();
        adapter = new MyAdapter(listItems,getApplicationContext());//yha adapter initialize the n set kr skte
        recyclerView.setAdapter(adapter);

       /* loadRecyclerViewData();*/
        onLoadingSwipeRefresh("");

    }


    private void loadRecyclerViewData(final String keyword){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        swipeRefreshLayout.setRefreshing(true);

         JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_DATA,
                                                                 new Response.Listener<JSONArray>() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {
                                                                progressDialog.dismiss();
                                                               for(int i=0; i<response.length();i++){
                                                                      try {
                                                                          JSONObject jsonObject = response.getJSONObject(i);
                                                                          Items_List item = new Items_List();
                                                                          item.setUserId(jsonObject.getString("userId"));
                                                                          item.setId(jsonObject.getString("id"));
                                                                          item.setTitle(jsonObject.getString("title"));
                                                                          item.setCompleted(jsonObject.getString("completed"));


                                                                          listItems.add(item);
                                                                      } catch (JSONException e) {
                                                                          e.printStackTrace();
                                                                          progressDialog.dismiss();
                                                                      }
                                                               }
                                                               adapter.notifyDataSetChanged();
                                                               progressDialog.dismiss();
                                                               swipeRefreshLayout.setRefreshing(false);
                                                                Toast.makeText(MainActivity.this,"your data has been Refreshed",Toast.LENGTH_SHORT).show();
                                                            }
                                                        },
                                                                 new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                                                                swipeRefreshLayout.setRefreshing(false);
                                                            }
                                                        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        loadRecyclerViewData("");

    }
    private void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadRecyclerViewData(keyword);

            }
        });

    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.programmingViewHolder>{
        private List<Items_List> listItems;
        private Context context;

        public MyAdapter(List<Items_List> listItems,Context context)
        {
            this.listItems = listItems;
            this.context = context;
        }


        @NonNull
        @Override
        public programmingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.list_item,parent,false);

            return new programmingViewHolder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull programmingViewHolder holder, int position) {
            Items_List listItem = listItems.get(position);
            holder.userId.setText(listItem.getUserId());
            holder.id.setText(listItem.getId());
            holder.title.setText(listItem.getTitle());
            holder.completed.setText(listItem.getCompleted());


        }


        @Override
        public int getItemCount() {
            return listItems.size();
        }




        public class programmingViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
            public TextView userId, id, title, completed;
            public programmingViewHolder(@NonNull View itemView) {
                super(itemView);
                userId = itemView.findViewById(R.id.userId);
                id = itemView.findViewById(R.id.id);
                title = itemView.findViewById(R.id.title);
                completed = itemView.findViewById(R.id.completed);

                itemView.setOnLongClickListener(this);

            }

            /**
             * Called when a view has been clicked and held.
             *
             * @param v The view that was clicked and held.
             * @return true if the callback consumed the long click, false otherwise.
             */
            @Override
            public boolean onLongClick(View v) {
                final int position = getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setTitle("Are you sure !!");
                builder.setMessage("Do you want to delete this item ?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                Toast.makeText(itemView.getContext(),"Deleted Id is : " +listItems.get(position).getId(),Toast.LENGTH_SHORT).show();
                                listItems.remove(position);
                                notifyItemRemoved(position);
                            }
                        }).setNegativeButton("NO",null);

                AlertDialog builder1 = builder.create();
                builder1.show();

                return true;

            }
        }


    }
}