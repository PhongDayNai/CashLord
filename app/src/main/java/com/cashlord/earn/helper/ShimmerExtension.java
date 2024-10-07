package com.cashlord.earn.helper;

import android.view.View;
import com.facebook.shimmer.ShimmerFrameLayout;

public class ShimmerExtension {
    public static void safelyHide(ShimmerFrameLayout shimmerFrameLayout) {
        if (shimmerFrameLayout != null) {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.hideShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }
}
