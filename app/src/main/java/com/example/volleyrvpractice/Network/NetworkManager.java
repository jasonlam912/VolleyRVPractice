package com.example.volleyrvpractice.Network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NetworkManager {
    private static NetworkManager instance ;
    private RequestQueue requestQueue ;
    private ImageLoader imageLoader ;
    private static Context ctx ;

    private static final String randomRecipeUrl = "https://api.spoonacular.com/recipes/random?number=10&apiKey=";
    private static final String searchRecipeUrl1 = "https://api.spoonacular.com/recipes/complexSearch?number=10&apiKey=";
    private static final String api = "22490cb12d2d45c7aafe40f28b4aa804";
    private static final String searchRecipeUrl2 = "&query=";
    private static final String searchRecipeUrl3 = "&offset=";


    private NetworkManager(Context context ) {
        ctx = context ;
        requestQueue = getRequestQueue ();

        imageLoader = new ImageLoader ( requestQueue ,
                new ImageLoader . ImageCache () {
                    private final LruCache< String , Bitmap>
                            cache = new LruCache < String , Bitmap >( 20 );

                    @Override
                    public Bitmap getBitmap ( String url ) {
                        return cache . get ( url );
                    }

                    @Override
                    public void putBitmap ( String url , Bitmap bitmap ) {
                        cache . put ( url , bitmap );
                    }
                });
    }

    public static synchronized NetworkManager getInstance (Context context ) {

        if ( instance == null ) {
            instance = new NetworkManager( context );
            //Log.d("getInstance",instance.toString());
        }
        return instance ;
    }

    public RequestQueue getRequestQueue () {
        if ( requestQueue == null ) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue ( ctx . getApplicationContext ());
        }
        return requestQueue ;
    }

    public < T > void addToRequestQueue ( Request< T > req ) {
        getRequestQueue (). add ( req );
    }

    public ImageLoader getImageLoader () {
        return imageLoader ;
    }

    public void getRandomRecipe(final CallbackListener listener){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, randomRecipeUrl + api, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.getResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    public void getSearchRecipe( String query, int offset, final CallbackListener listener){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchRecipeUrl1 + api + searchRecipeUrl2 + query + searchRecipeUrl3 + offset,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.getResult(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

}
