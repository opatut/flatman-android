package de.opatut.flatman.util;

import info.evelio.drawable.RoundedAvatarDrawable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	static Map<String, Bitmap> mCache = new HashMap<String, Bitmap>();

	ImageView mImage;

	public DownloadImageTask(ImageView bmImage) {
		this.mImage = bmImage;
	}

	protected Bitmap doInBackground(String... urls) {
		String url = urls[0];

		if (mCache.containsKey(url)) {
			return mCache.get(url);
		}

		try {
			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(true);
			connection.connect();
			InputStream input = connection.getInputStream();

			Bitmap bitmap = BitmapFactory.decodeStream(input);
			mCache.put(url, bitmap);
			return bitmap;
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			mImage.setImageDrawable(new RoundedAvatarDrawable(result));
		}
	}
}