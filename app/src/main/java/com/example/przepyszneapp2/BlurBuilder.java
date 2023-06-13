package com.example.przepyszneapp2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.renderscript.ScriptIntrinsicConvolve5x5;

public class BlurBuilder {

    private static final float BLUR_RADIUS = 25f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * 0.4f);
        int height = Math.round(image.getHeight() * 0.4f);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);

        tmpOut.copyTo(outputBitmap);

        renderScript.destroy();

        return outputBitmap;
    }
}