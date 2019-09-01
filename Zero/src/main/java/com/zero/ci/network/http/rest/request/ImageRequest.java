package com.zero.ci.network.http.rest.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.zero.ci.widget.logger.Logger;
import com.zero.ci.network.http.rest.headers.Headers;
import com.zero.ci.network.http.RequestMethod;

import java.util.Locale;

/**
 * Image handle parameter.
 */
public class ImageRequest extends Request<Bitmap> {

    private final int mMaxWidth;
    private final int mMaxHeight;
    private final Bitmap.Config mDecodeConfig;
    private ImageView.ScaleType mScaleType;

    /**
     * Decoding lock so that we don't decode more than one image at a time (to avoid OOM's).
     */
    private static final Object DECODE_LOCK = new Object();

    public ImageRequest(String url, RequestMethod requestMethod, int maxWidth, int maxHeight, Bitmap.Config
            decodeConfig, ImageView.ScaleType scaleType) {
        super(url, requestMethod);
        this.mMaxWidth = maxWidth;
        this.mMaxHeight = maxHeight;
        this.mDecodeConfig = decodeConfig;
        this.mScaleType = scaleType;
        setAccept("image/*");
    }

    @Override
    public Bitmap parseResponse(Headers responseHeaders, byte[] responseBody) throws Exception {
        synchronized (DECODE_LOCK) {
            Bitmap bitmap = null;
            if (responseBody != null) {
                try {
                    bitmap = doResponse(responseBody);
                } catch (OutOfMemoryError e) {
                    String errorMessage = String.format(Locale.US, "Caught OOM for %d byte image, url=%s",
                            responseBody.length, url());
                    Logger.e(e + " : " + errorMessage);
                }
            }
            return bitmap;
        }
    }

    /**
     * The real guts of AnalyzeResponse. Broken out for readability.
     */
    private Bitmap doResponse(byte[] byteArray) throws OutOfMemoryError {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        Bitmap bitmap;
        if (mMaxWidth == 0 && mMaxHeight == 0) {
            decodeOptions.inPreferredConfig = mDecodeConfig;
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
        } else {
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;

            int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight, actualWidth, actualHeight, mScaleType);
            int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth, actualHeight, actualWidth, mScaleType);

            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);

            if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth || tempBitmap.getHeight() >
                    desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        }
        return bitmap;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary,
                                           ImageView.ScaleType scaleType) {

        // If no dominant value at all, just myreturn the actual.
        if ((maxPrimary == 0) && (maxSecondary == 0)) {
            return actualPrimary;
        }

        // If ScaleType.FIT_XY fill the whole rectangle, ignore ratio.
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            if (maxPrimary == 0) {
                return actualPrimary;
            }
            return maxPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;

        // If ScaleType.CENTER_CROP fill the whole rectangle, preserve aspect ratio.
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            if ((resized * ratio) < maxSecondary) {
                resized = (int) (maxSecondary / ratio);
            }
            return resized;
        }

        if ((resized * ratio) > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    // Visible for testing.
    public static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }

}