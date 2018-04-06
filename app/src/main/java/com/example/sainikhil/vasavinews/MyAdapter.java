package com.example.sainikhil.vasavinews;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    FirebaseFirestore db;
    private String[] mtitle,mdescription;
    private Bitmap[] mbitmap;
    TextView postednewstitle,postednewsdescription;
    ImageView postednewsimage;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }
    public MyAdapter()
    {
        db = FirebaseFirestore.getInstance();

    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset,Bitmap[] bitmap,String[] Dataset )
     {
        mtitle = myDataset;
        mbitmap=bitmap;
        mdescription=Dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mView.setText(mDataset[position]);
        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> data = document.getData();
                                for (Map.Entry<String, Object> entry : data.entrySet())
                                {
                                    System.out.println(entry.getKey() + "/" + entry.getValue());
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        final TextView mTextView = (TextView) holder.mView.findViewById(R.id.text_view);
        final ImageView mImageView = (ImageView) holder.mView.findViewById(R.id.image_view);
        mTextView.setText(mtitle[position]);
        mImageView.setImageBitmap(mbitmap[position]);
        final CardView mCardView=(CardView)holder.mView.findViewById(R.id.card_view);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE );
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                final View myView = inflater.inflate(R.layout.posted_news_layout, null);
                //Toast.makeText(view.getContext(), "OnLongClick Called at position " + holder.getAdapterPosition()+mtitle[holder.getAdapterPosition()], Toast.LENGTH_SHORT).show();
                postednewstitle=(TextView)myView.findViewById(R.id.posted_news_title);
                postednewsimage=(ImageView)myView.findViewById(R.id.posted_news_image);
                postednewsdescription=(TextView)myView.findViewById(R.id.posted_news_description);
                postednewstitle.setText(mtitle[holder.getAdapterPosition()]);
                postednewsimage.setImageBitmap(mbitmap[holder.getAdapterPosition()]);
                postednewsdescription.setText(mdescription[holder.getAdapterPosition()]);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.setView(myView);
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });

}

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mtitle.length;
    }
}
