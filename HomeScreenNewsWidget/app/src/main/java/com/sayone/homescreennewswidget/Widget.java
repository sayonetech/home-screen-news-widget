package com.sayone.homescreennewswidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sayone.homescreennewswidget.model.Articles;
import com.sayone.homescreennewswidget.model.BaseModel;
import com.sayone.homescreennewswidget.rest.ApiClient;
import com.sayone.homescreennewswidget.rest.ApiInterface;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Widget extends AppWidgetProvider {

    private static String ACTION_WIDGET_RECEIVER = "ClickActionReceiverWidget";
    private static final String SOURCE = "the-next-web";
    private static final String SORT_BY = "latest";
    private static final String API_KEY = "6e00955b6f3f45fba169a52591ad9c7e";

    private AppWidgetManager mAppWidgetManager;
    private List<Articles> mArticles;
    private RemoteViews mViews;
    private int mCurrentWidgetId;
    private ComponentName appWidget;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);
        mContext = context;

        if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
            mAppWidgetManager = AppWidgetManager.getInstance(context);
            appWidget = new ComponentName(context.getPackageName(), Widget.class.getName());
            onUpdate(context, mAppWidgetManager, mAppWidgetManager.getAppWidgetIds(appWidget));
        } else if (mAppWidgetManager != null) {
            return;
        }

        super.onReceive(context, intent);
    }


    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        mAppWidgetManager = appWidgetManager;

        for (int appWidgetId : appWidgetIds) {
            mCurrentWidgetId = appWidgetId;

            Intent action = new Intent(context, Widget.class);
            action.setAction(ACTION_WIDGET_RECEIVER);
            PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, action, 0);

            mViews.setOnClickPendingIntent(R.id.reload, actionPendingIntent);

            makeCall();
            Toast.makeText(mContext, "Loading...", Toast.LENGTH_LONG).show();
        }
    }

    public void makeCall() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<BaseModel> call = apiService.getData(SOURCE, SORT_BY, API_KEY);
        call.enqueue(new Callback<BaseModel>() {

            @Override
            public void onResponse(Call<BaseModel> call, final Response<BaseModel> response) {
                mArticles = response.body().getArticles();
                runUiThread();
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                try {
                    Thread.sleep(3000);
                    mAppWidgetManager = AppWidgetManager.getInstance(mContext);
                    appWidget = new ComponentName(mContext.getPackageName(), Widget.class.getName());
                    onUpdate(mContext, mAppWidgetManager, mAppWidgetManager.getAppWidgetIds(appWidget));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void runUiThread() {
        new Thread() {

            @Override
            public void run() {
                int number = 0;

                try {
                    for (Articles article : mArticles) {
                        Bitmap bmp = null;
                        mViews.setTextViewText(R.id.textVHeading, article.getTitle());
                        mViews.setTextViewText(R.id.textVContent, article.getDescription());

                        try {
                            bmp = BitmapFactory.decodeStream(new URL(article.getUrlToImage()).openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (bmp != null)
                            mViews.setImageViewBitmap(R.id.image, Bitmap.createScaledBitmap(bmp, 500, 250, false));

                        setData(mCurrentWidgetId, mViews);
                        number++;

                        if (number == mArticles.size())
                            makeCall();

                        Thread.sleep(6000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isInterrupted() {
                makeCall();
                return false;
            }
        }.start();
    }

    public void setData(int currentWidgetId, RemoteViews views) {
        if (mAppWidgetManager != null)
            mAppWidgetManager.updateAppWidget(currentWidgetId, views);
    }
}
