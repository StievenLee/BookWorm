package com.example.bookworm.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.example.bookworm.R;

public class NavbarView extends View {

    public static final int SLOT_HOME   = 0;
    public static final int SLOT_BOOKS  = 1;
    public static final int SLOT_STORES = 2;
    public static final int SLOT_LOGOUT = 3;

    // Slot center X as a fraction of the bar width. The Figma navbar's icon centers
    // (33.12/76.12/118.07/164.12px of 188.785px) are unevenly spaced — the last gap
    // is ~10% wider than the other two — which reads as a lopsided gap on-screen.
    // Instead, split the bar into 4 equal columns (1/8, 3/8, 5/8, 7/8) so all four
    // slots — including the edge margins — are evenly and symmetrically spaced.
    private static final float[] SLOT_FRACTION = { 1f / 8f, 3f / 8f, 5f / 8f, 7f / 8f };

    private final float density;
    private int   activeSlot = SLOT_HOME;
    private float bubbleDp   = 0f;
    private float barWidthDp = 393f;

    private final Paint barPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path  barPath     = new Path();

    private final Drawable[] icons = new Drawable[4];

    public interface OnTabClickListener { void onTabClick(int slot); }
    private OnTabClickListener listener;

    public NavbarView(Context c) { this(c, null); }
    public NavbarView(Context c, AttributeSet a) { this(c, a, 0); }
    public NavbarView(Context c, AttributeSet a, int def) {
        super(c, a, def);
        density = c.getResources().getDisplayMetrics().density;
        barPaint.setColor(0xFFC08552);    // color_primary_1
        bubblePaint.setColor(0xFF7C3900); // dark bubble
        int[] res = {
            R.drawable.ic_nav_home_v2, R.drawable.ic_nav_books_v2,
            R.drawable.ic_nav_cart,    R.drawable.ic_nav_logout
        };
        for (int i = 0; i < 4; i++) {
            Drawable d = ContextCompat.getDrawable(c, res[i]);
            icons[i] = d != null ? d.mutate() : null;
        }
    }

    public void setOnTabClickListener(OnTabClickListener l) { listener = l; }

    public void setSlot(int slot) {
        activeSlot = slot;
        bubbleDp   = slotDp(slot);
        invalidate();
    }

    public void animateToSlot(int slot) {
        if (slot == activeSlot) return;
        float from = bubbleDp, to = slotDp(slot);
        activeSlot = slot;
        ValueAnimator va = ValueAnimator.ofFloat(from, to);
        va.setDuration(320);
        va.setInterpolator(new FastOutSlowInInterpolator());
        va.addUpdateListener(a -> { bubbleDp = (float) a.getAnimatedValue(); invalidate(); });
        va.start();
    }

    // Slot center X in dp, proportional to the bar's actual measured width.
    private float slotDp(int slot) { return SLOT_FRACTION[slot] * barWidthDp; }

    @Override
    protected void onMeasure(int ws, int hs) {
        setMeasuredDimension(MeasureSpec.getSize(ws), (int)(92 * density));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float newBarWidthDp = w / density;
        if (newBarWidthDp > 0) {
            barWidthDp = newBarWidthDp;
            bubbleDp   = slotDp(activeSlot);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) { canvas.drawColor(0xFFC08552); return; }
        float d  = density;
        float cx = bubbleDp * d;

        buildBarPath(cx, d);
        canvas.drawPath(barPath, barPaint);

        // Bubble circle (dark brown raised disc)
        canvas.drawCircle(cx, 24.5f * d, 24.5f * d, bubblePaint);

        // Icons: active slides with bubble, inactive at fixed positions
        for (int i = 0; i < 4; i++) {
            if (icons[i] == null) continue;
            boolean active = (i == activeSlot);
            float icx = active ? cx : slotDp(i) * d;
            // Vertical center: active icon on the bubble center (24.5dp),
            // inactive icons on the bar body (45dp).
            float icyCenter = active ? 24.5f * d : 45f * d;
            // Figma navbar (node 205:563): icon glyphs are 17.293px on a 188.785px
            // bar; on-device the bar spans the full 393dp width, a ~2.082x scale, so
            // the icons render at 17.293 * (393/188.785) ≈ 36dp (half = 18dp).
            // Logout has a larger intrinsic viewport (28.79 vs 24) than the other
            // three — shrink its drawn bounds ~15% so it reads at the same visual
            // size as the rest, matching the Figma navbar.
            float half = (i == SLOT_LOGOUT) ? 15f * d : 18f * d;
            float iconTop = icyCenter - half;
            icons[i].setTint(active ? 0xFFFFF8F0 : 0xFF553522);
            icons[i].setBounds((int)(icx - half), (int)iconTop,
                               (int)(icx + half), (int)(iconTop + half*2));
            icons[i].draw(canvas);
        }
    }

    // Draws caramel bar with a bezier scoop at bubble center cx (px).
    // Path derived from Figma bg_nav_pill_* SVG with parameterized cx.
    private void buildBarPath(float cx, float d) {
        float r   = 10 * d;
        float w   = getWidth();
        float top = 10 * d;
        float h   = 92 * d;

        barPath.reset();
        barPath.moveTo(0, 20 * d);
        // Top-left corner
        barPath.arcTo(new RectF(0, top, r*2, top+r*2), 180, 90, false);
        // Top edge → scoop left entry
        barPath.lineTo(cx - 39.5f*d, top);
        // Left scoop bezier (down into trough)
        barPath.cubicTo(cx - 27.5f*d, 15.5f*d,
                        cx - 39f*d,   55.5f*d,
                        cx - 0.5f*d,  55.5f*d);
        // Right scoop bezier (back up)
        barPath.cubicTo(cx + 38f*d,   55.5f*d,
                        cx + 25f*d,   top,
                        cx + 39.5f*d, top);
        // Top edge → top-right corner
        barPath.lineTo(w - r*2, top);
        barPath.arcTo(new RectF(w-r*2, top, w, top+r*2), 270, 90, false);
        // Right edge down
        barPath.lineTo(w, 82*d);
        // Bottom-right corner
        barPath.arcTo(new RectF(w-r*2, 82*d, w, h), 0, 90, false);
        // Bottom edge
        barPath.lineTo(r, h);
        // Bottom-left corner
        barPath.arcTo(new RectF(0, 82*d, r*2, h), 90, 90, false);
        barPath.close();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            float xDp = e.getX() / density;
            int nearest = 0;
            float minDist = Float.MAX_VALUE;
            for (int i = 0; i < 4; i++) {
                float dist = Math.abs(xDp - slotDp(i));
                if (dist < minDist) { minDist = dist; nearest = i; }
            }
            if (listener != null) { listener.onTabClick(nearest); return true; }
        }
        return super.onTouchEvent(e);
    }
}
