package com.example.memeapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class My_Posts extends AppCompatActivity {
    FirebaseUser user;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    RecyclerView post_list;
    private long pressedTime;
    String username,caption;
    Blog_Data blog_data;
    FirebaseRecyclerAdapter<Blog_Data, PostHolder> firebaseRecyclerAdapter;
    private DatabaseReference databaseReference;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private DatabaseReference dr;
    static String get_uid;
    String uid="Varshith V hegde";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("blog_images");
        databaseReference.orderByChild("timestamp");

        post_list = (RecyclerView) findViewById(R.id.postlist);
        post_list.setHasFixedSize(true);
        post_list.setLayoutManager(new LinearLayoutManager(this));

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        firebaseAuth = FirebaseAuth.getInstance();
//        get_uid = user.getUid();
//        dr=FirebaseDatabase.getInstance().getReference().child("user info");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                uid = dataSnapshot.child(get_uid).child("name").getValue(String.class);
//
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                Toast.makeText(My_Posts.this,"something went wrong..",Toast.LENGTH_SHORT).show();
//            }
//        });
        fetch();
    }
    private void fetch() {
        // DatabaseReference query = FirebaseDatabase.getInstance().getReference().child("blog_images");
//        FirebaseRecyclerOptions<Blog_data> options = new FirebaseRecyclerOptions.Builder<Blog_data>().setQuery(query, new SnapshotParser<Blog_data>() {
//            @NonNull
//            @Override
//            public Blog_data parseSnapshot(@NonNull DataSnapshot snapshot) {
//                Blog_data blog_data = new Blog_data();
//                blog_data.setUsername(snapshot.child("username").getValue().toString());
//                blog_data.setCaption(snapshot.child("caption").getValue().toString());
//                blog_data.setImage(snapshot.child("image").getValue().toString());
////                return new Blog_data(snapshot.child("username").getValue().toString(),
////                        snapshot.child("caption").getValue().toString(),
////                        snapshot.child("image").getValue().toString()
////                        );
//                return blog_data;
//
//            }
//
//        }).build();
        FirebaseRecyclerOptions<Blog_Data> options = new FirebaseRecyclerOptions.Builder<Blog_Data>().setQuery(databaseReference,Blog_Data.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog_Data, My_Posts.PostHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull My_Posts.PostHolder holder, int position, @NonNull Blog_Data model) {
             //   Toast.makeText(getApplicationContext(), model.getEmail(), Toast.LENGTH_SHORT).show();
                    holder.setUsername(model.getUsername());
                    holder.setCaption(model.getCaption());
                    holder.setImage(getApplicationContext(), model.getImage());


            }

            @NonNull
            @Override
            public My_Posts.PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list,viewGroup,false);
                return new My_Posts.PostHolder(view);
            }
        };


        post_list.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        post_list.setLayoutManager(linearLayoutManager);

    }


    public static class PostHolder extends RecyclerView.ViewHolder{

        View mView;
        //
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
//
        }
        public void setUsername(String username){

            TextView post_caption = (TextView) mView.findViewById(R.id.field_post_username);
            post_caption.setText(username);

        }
        public void setCaption(String caption){

            TextView post_caption = (TextView) mView.findViewById(R.id.field_post_caption);
            post_caption.setText(caption);

        }

        public void setImage(Context context, String image) {
            ImageView imageView = (ImageView)mView.findViewById(R.id.field_post_image);
            Picasso.get().load(image).into(imageView);
        }
    }


    @Override
    protected void onStart() {

        super.onStart();
        firebaseRecyclerAdapter.startListening();
//        firebaseRecyclerAdapter.startListening();
//        FirebaseRecyclerOptions<Blog_data> options = new FirebaseRecyclerOptions.Builder<Blog_data>().setQuery(databaseReference,Blog_data.class).build();
//        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog_data, PostHolder>(
//                options
//        ) {
//
//            @Override
//            protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull AddPost model) {
//
//               // holder.setUsername(model.username);
//              //  holder.setCaption(model.caption_val);
//            }
//
//            @NonNull
//            @Override
//            public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                View view = LayoutInflater.from(Feed.this).inflate(R.layout.post_list,null,false);
//                return new PostHolder(view);
//            }
//        };
//
//
//        post_list.setAdapter(firebaseRecyclerAdapter);
//

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.stopListening();
        }
    }
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
