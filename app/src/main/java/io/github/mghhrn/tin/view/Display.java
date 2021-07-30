////////////////////////////////////////////////////////////////////////////////
//
//  Signal generator - An Android Signal generator written in Java.
//
//  Copyright (C) 2013	Bill Farmer
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

package io.github.mghhrn.tin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.Locale;

// Display
public class Display extends SiggenView
{
    private static final int MARGIN = 8;

    private double frequency;
    private double level;

    public Display(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    // On measure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = (parentWidth - MARGIN) /2 ;
        int h = parentHeight / 4;

        setMeasuredDimension(w, h);
    }

    // Set frequency
    public void setFrequency(double f)
    {
        frequency = f;
        invalidate();
    }

    // Set level
    protected void setLevel(double l)
    {
        level = l;
        invalidate();
    }

    public double getFrequency() {
        return frequency;
    }

    // On draw
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        String s = String.format(Locale.getDefault(), "%5.2fHz", frequency);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(height / 2);
        paint.setTextScaleX(0.9f);
        paint.setColor(textColour);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(s, MARGIN, height * 2 / 3, paint);

        s = String.format(Locale.getDefault(), "%5.2fdB", level);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(s, width - MARGIN, height * 2 / 3, paint);
    }
}
