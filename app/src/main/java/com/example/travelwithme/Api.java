package com.example.travelwithme;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.travelwithme.api.AddSubscribeApi;
import com.example.travelwithme.api.DeletePostApi;
import com.example.travelwithme.api.DeleteSubscribeApi;
import com.example.travelwithme.api.ExistingSubscribeApi;
import com.example.travelwithme.api.GetFollowersApi;
import com.example.travelwithme.api.GetFollowingsApi;
import com.example.travelwithme.api.GetFollowingsPostsApi;
import com.example.travelwithme.api.GetPostsApi;
import com.example.travelwithme.api.GetUserApi;
import com.example.travelwithme.api.GetUserByIDApi;
import com.example.travelwithme.api.SearchUsersApi;
import com.example.travelwithme.pojo.Post;
import com.example.travelwithme.pojo.User;
import com.example.travelwithme.requests.PostCreateRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public void getUser(String email, Consumer<User> onUserLoaded) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        GetUserApi getUserApi = retrofit.create(GetUserApi.class);
        Call<User> call = getUserApi.getUser(email);
        call.enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.i("sucsess", "sucsess");
                    User user = response.body();
                    onUserLoaded.accept(user);
                } else {
                    Log.i("error", "error1 to load user");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("error", "error2 to load user");
                t.printStackTrace();
            }
        });
    }

    public void getUserByID(long userID, Consumer<User> onUserLoaded) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        GetUserByIDApi getUserApi = retrofit.create(GetUserByIDApi.class);
        Call<User> call = getUserApi.getUser(userID);
        call.enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.i("sucsess", "sucsess to load user by id");
                    User user = response.body();
                    onUserLoaded.accept(user);
                } else {
                    Log.i("eeeerrror", "error1");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("eeeerrror", "error2");
                t.printStackTrace();
            }
        });
    }

    public void existingSubscribe(long followingID, long followerID, Consumer<Boolean> onUserLoaded) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ExistingSubscribeApi existingSubscribeApi = retrofit.create(ExistingSubscribeApi.class);
        Call<Boolean> call = existingSubscribeApi.existingSubscribe(followingID, followerID);
        call.enqueue(new Callback<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Log.i("sucsess", "sucsess existing subscribe");
                    Boolean isFollowing = response.body();
                    onUserLoaded.accept(isFollowing);
                } else {
                    Log.i("eeeerrror", "error existing subscribe");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.i("eeeerrror", "error existing subscribe");
                t.printStackTrace();
            }
        });
    }

    public void addSubscribe(long followingID, long followerID) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        AddSubscribeApi addSubscribeApi = retrofit.create(AddSubscribeApi.class);
        Call<Void> call = addSubscribeApi.addSubscribe(followingID, followerID);
        call.enqueue(new Callback<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("sucsess", "sucsess add subscribe");
                } else {
                    Log.i("eeeerrror", "error add subscribe");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("eeeerrror", "error add subscribe");
                t.printStackTrace();
            }
        });
    }

    public void deleteSubscribe(long followingID, long followerID) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        DeleteSubscribeApi deleteSubscribeApi = retrofit.create(DeleteSubscribeApi.class);
        Call<Void> call = deleteSubscribeApi.deleteSubscribe(followingID, followerID);
        call.enqueue(new Callback<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("sucsess", "sucsess delete subscribe");
                } else {
                    Log.i("eeeerrror", "error delete subscribe");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("eeeerrror", "error delete subscribe");
                t.printStackTrace();
            }
        });
    }

    public void deletePost(long postID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .build();
        DeletePostApi deletePostApi = retrofit.create(DeletePostApi.class);
        Call<Void> call = deletePostApi.deletePost(postID);
        call.enqueue(new Callback<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("sucsess", "sucsess delete post");
                } else {
                    Log.i("eeeerrror1", "error delete post");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("eeeerrror2", "error delete post");
                t.printStackTrace();
            }
        });
    }

    //TODO: add offset and count params
    public void getPosts(long userID, Consumer<Collection<Post>> onUserLoaded) {
        Collection<Post> lst = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GetPostsApi getPostsApi = retrofit.create(GetPostsApi.class);
        Call<List<PostCreateRequest>> call = getPostsApi.getPosts(userID, 0L, 1000L);
        call.enqueue(new Callback<List<PostCreateRequest>>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<PostCreateRequest>> call, Response<List<PostCreateRequest>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (PostCreateRequest postCreateRequest : response.body()) {
                            lst.add(postCreateRequest.getPost());
                        }
                        onUserLoaded.accept(lst);
                    } else {
                        Log.i("error", "response body is null");
                    }
                } else {
                    Log.i("error", "error load posts1");
                }
            }

            @Override
            public void onFailure(Call<List<PostCreateRequest>> call, Throwable t) {
                Log.i("error", "error load posts2");
                t.printStackTrace();
            }
        });
    }

    //TODO: add offset and count params
    public void getFollowingsPosts(long userID, Consumer<Collection<Post>> onUserLoaded) {
        Collection<Post> lst = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GetFollowingsPostsApi getFollowingsPostsApi = retrofit.create(GetFollowingsPostsApi.class);
        Call<List<PostCreateRequest>> call = getFollowingsPostsApi.getFollowingsPosts(userID, 0L, 1000L);
        call.enqueue(new Callback<List<PostCreateRequest>>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<PostCreateRequest>> call, Response<List<PostCreateRequest>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (PostCreateRequest postCreateRequest : response.body()) {
                            lst.add(postCreateRequest.getPost());
                        }
                        onUserLoaded.accept(lst);
                    } else {
                        Log.i("error", "response body is null");
                    }
                } else {
                    Log.i("error", "error");
                }
            }

            @Override
            public void onFailure(Call<List<PostCreateRequest>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //TODO: add offset and count params
    public void getFollowers(long userID, Consumer<Collection<User>> onFollowersLoaded) {
        Collection<User> lst = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GetFollowersApi getFollowersApi = retrofit.create(GetFollowersApi.class);
        Call<List<User>> call = getFollowersApi.getFollowers(userID, 0L, 1000L);
        call.enqueue(new Callback<List<User>>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        lst.addAll(response.body());
                        onFollowersLoaded.accept(lst);
                    } else {
                        Log.i("error", "response body is null");
                    }
                } else {
                    Log.i("error", "error");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //TODO: add offset and count params
    public void getFollowing(long userID, Consumer<Collection<User>> onFollowingLoaded) {
        Collection<User> lst = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GetFollowingsApi getFollowingsApi = retrofit.create(GetFollowingsApi.class);
        Call<List<User>> call = getFollowingsApi.getFollowings(userID, 0L, 1000L);
        call.enqueue(new Callback<List<User>>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        lst.addAll(response.body());
                        onFollowingLoaded.accept(lst);
                    } else {
                        Log.i("error", "response body is null");
                    }
                } else {
                    Log.i("error", "error");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //TODO: add offset and count params
    public void searchUsers(long myID, String inputText, Consumer<Collection<User>> onSearchResultsLoaded) {
        Collection<User> lst = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        SearchUsersApi searchUsersApi = retrofit.create(SearchUsersApi.class);
        Call<List<User>> call = searchUsersApi.searchUsers(myID, inputText, 0L, 1000L);
        call.enqueue(new Callback<List<User>>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        lst.addAll(response.body());
                        onSearchResultsLoaded.accept(lst);
                    } else {
                        Log.i("error", "response body is null");
                    }
                } else {
                    Log.i("error", "error");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
