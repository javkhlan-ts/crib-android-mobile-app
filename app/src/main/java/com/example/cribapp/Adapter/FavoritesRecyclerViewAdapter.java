package com.example.cribapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cribapp.R;
import com.example.cribapp.RentListingDetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavoritesRecyclerViewAdapter extends RecyclerView.Adapter<FavoritesRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "FavoritesRecyclerViewAd";

    private ArrayList<String> mListingImagesArrayList = new ArrayList<>();
    private ArrayList<String> mBedBathSqrtArrayList = new ArrayList<>();
    private ArrayList<String> mPriceArrayList = new ArrayList<>();
    private ArrayList<String> mAddressArrayList = new ArrayList<>();
    private ArrayList<String> mTypeArrayList = new ArrayList<>();

    private Context mContext;

    //Command+N to create constructor
    public FavoritesRecyclerViewAdapter(Context mContext, ArrayList<String> mListingImagesArrayList,
                                        ArrayList<String> mBedBathSqrtArrayList, ArrayList<String> mPriceArrayList,
                                        ArrayList<String> mAddressArrayList, ArrayList<String> mTypeArrayList) {
        this.mContext = mContext;
        this.mListingImagesArrayList = mListingImagesArrayList;
        this.mBedBathSqrtArrayList = mBedBathSqrtArrayList;
        this.mPriceArrayList = mPriceArrayList;
        this.mAddressArrayList = mAddressArrayList;
        this.mTypeArrayList = mTypeArrayList;
    }


    //responsible for inflating the view, recycling the view holder and putting them into where they should be 
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favorites, parent, false);
        ViewHolder holder = new ViewHolder(view);
         return holder;
    }

    //this method will be called everytime new item is added to the lis. if 10 items added to recycler view, the log will be called 10 times too
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called. ");

        Glide.with(mContext)
                .asBitmap()
                .load(mListingImagesArrayList.get(position))
                .into(holder.mListingImage);
        holder.mPrice.setText(mPriceArrayList.get(position));
        holder.mAddress.setText(mAddressArrayList.get(position));
        holder.mBedBathSqft.setText(mBedBathSqrtArrayList.get(position));
        holder.mType.setText(mTypeArrayList.get(position));

        holder.mFavoritesParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on "+mAddressArrayList.get(position ));
                //Toast.makeText(mContext, mAddressArrayList.get(position), Toast.LENGTH_SHORT).show();

                FirebaseFirestore.getInstance().collection("listing")
                        .whereEqualTo("address1", mAddressArrayList.get(position))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());

                                        String listingId = document.getId();
                                        String fragmentId = "1";
                                        Intent intent = new Intent(mContext, RentListingDetailsActivity.class);
                                        intent.putExtra(RentListingDetailsActivity.LISTING_ID, listingId);
                                        intent.putExtra(RentListingDetailsActivity.FRAGMENT_ID, fragmentId);
                                        mContext.startActivity(intent);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: "+e.getMessage());
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddressArrayList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mListingImage;
        TextView mBedBathSqft;
        TextView mPrice;
        TextView mAddress;
        TextView mType;
        LinearLayout mFavoritesParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mListingImage = itemView.findViewById(R.id.listing_images);
            mBedBathSqft = itemView.findViewById(R.id.listing_bed_bath_sqft);
            mPrice = itemView.findViewById(R.id.listing_price);
            mAddress = itemView.findViewById(R.id.listing_address);
            mType = itemView.findViewById(R.id.listing_type);
            mFavoritesParentLayout = itemView.findViewById(R.id.favorites_parent_layout);
        }
    }
}
