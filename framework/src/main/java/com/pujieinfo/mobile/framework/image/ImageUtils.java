package com.pujieinfo.mobile.framework.image;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.pujieinfo.mobile.framework.R;
import com.pujieinfo.mobile.framework.utils.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Observable;

/**
 * Fresco Utils
 */
public class ImageUtils {

    public static void init(Context ctx) {

        File cache = new File(FileUtils.getAvailableCachePath(ctx));
        if (!cache.exists()) {
            cache.mkdirs();
        }

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(ctx)
                .setBaseDirectoryPath(cache)
                .build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(ctx)
                .setDownsampleEnabled(true)
                .setResizeAndRotateEnabledForNetwork(true)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .setMainDiskCacheConfig(diskCacheConfig)
                .build();

        Fresco.initialize(ctx, config);

    }

    public static void setImageUrl(SimpleDraweeView imageView, String url) {
        if (imageView == null || TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        setImageUri(imageView, uri);
    }

    public static void setImageUri(SimpleDraweeView imageView, Uri uri) {
        if (imageView == null || uri == null) {
            return;
        }
        imageView.setImageURI(uri);
    }

    public static void setImageResource(SimpleDraweeView imageView, int resId) {
        if (imageView == null) {
            return;
        }

        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
        hierarchy.setPlaceholderImage(resId);
        imageView.setHierarchy(hierarchy);

    }

    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        if (uri == null) {
            return null;
        }

        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return path;
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return path;
        } else {

        }
        return null;
    }

    public static Uri saveBitmapToLocal(Bitmap bm, String local) {

        File img = new File(local);
        if (!img.getParentFile().exists()) {
            boolean success = img.getParentFile().mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor == null) {
                return "";
            }
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // 两种压缩方式
    public static Observable<String> compressImage(String sourcePath, String targetPath) {

        if (TextUtils.isEmpty(sourcePath) || TextUtils.isEmpty(targetPath)) {
            return null;
        }

        File source = new File(sourcePath);
        File tag = new File(targetPath);
        if (!tag.getParentFile().exists()) {
            tag.getParentFile().mkdirs();
        }
        File temp = new File(tag.getParentFile(), String.valueOf(System.currentTimeMillis()));

        return compressByRatio(sourcePath, temp.getAbsolutePath())
                .flatMap(s -> {
                    if (TextUtils.isEmpty(s)) {
                        return Observable.error(new Throwable());
                    } else {
                        return compressByQuality(s, tag.getAbsolutePath());
                    }
                });

    }

    // 比例压缩
    private static Observable<String> compressByRatio(String sourcePath, String targetPath) {

        return Observable
                .create(sub -> {

                    float maxSize = 640;

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    BitmapFactory.decodeFile(sourcePath, options);

                    final float originalWidth = options.outWidth;
                    final float originalHeight = options.outHeight;

                    float convertedWidth;
                    float convertedHeight;

                    if (originalWidth > originalHeight) {
                        convertedWidth = 640;
                        convertedHeight = maxSize / originalWidth * originalHeight;
                    } else {
                        convertedHeight = maxSize;
                        convertedWidth = maxSize / originalHeight * originalWidth;
                    }

                    final float ratio = originalWidth / convertedWidth;

                    options.inSampleSize = (int) ratio;
                    options.inJustDecodeBounds = false;

                    Bitmap convertedBitmap = BitmapFactory.decodeFile(sourcePath, options);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    convertedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    FileOutputStream fileOutputStream;
                    try {
                        fileOutputStream = new FileOutputStream(new File(targetPath));
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        fileOutputStream.flush();
                        fileOutputStream.close();

                        sub.onNext(targetPath);
                        sub.onComplete();
                    } catch (Exception e) {
                        sub.onError(e);
                    }

                });
    }

    // 质量压缩
    private static Observable<String> compressByQuality(String sourceFile, String tagFile) {
        return Observable.just(new File(sourceFile))
                .flatMap(f -> {
                    if (f.exists()) {
                        return Observable.just(f);
                    } else {
                        return Observable.error(new Throwable());
                    }
                })
                .flatMap(f -> {
                    try {

                        Bitmap image = BitmapFactory.decodeFile(sourceFile);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        int options = 100;
                        while (baos.toByteArray().length / 1024 > 50) {
                            if (options == 20) {
                                break;
                            }
                            baos.reset();
                            options -= 10;
                            image.compress(Bitmap.CompressFormat.JPEG, options, baos);

                        }
                        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

                        File temp = new File(sourceFile);
                        if (temp.exists()) {
                            temp.delete();
                        }

                        saveBitmapToLocal(bitmap, tagFile);

                        return Observable.just(tagFile);

                    } catch (Exception e) {
                        return Observable.error(e);
                    }
                });
    }

    public static String compressImage(Context context, String filePath) {
//        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 1280.0f;
        float maxWidth = 960.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = FileUtils.getAvailableCachePath(context) + System.currentTimeMillis() + ".jpg";
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static void cacheImage(SimpleDraweeView imageView, String url) {

        if (TextUtils.isEmpty(url)) {
            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            hierarchy.setPlaceholderImage(R.mipmap.image_loading);
            imageView.setHierarchy(hierarchy);
            return;
        }

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(80, 80))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setImageRequest(request)
                .build();

        imageView.setController(controller);

    }

    public static String saveBitmap(Context context, Bitmap bitmap, String savePath) {

        if (bitmap == null || context == null || TextUtils.isEmpty(savePath)) {
            return "";
        }

        File path = new File(savePath);
        if (!path.exists()) {
            path.mkdirs();
        }

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(path, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static boolean saveToAlbum(Context context, String path) {

        try {
            File file = new File(path);

            String save = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);

            Log.i("ImageTag", "Save " + path + " to album " + save);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updateAlbum(Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
