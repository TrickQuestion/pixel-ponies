package com.annatala.pixelponies.sprites;

import com.annatala.noosa.Visual;
import com.annatala.noosa.tweeners.Tweener;
import com.annatala.utils.PointF;

/**
 * Created by mike on 16.04.2016.
 */
class JumpTweener extends Tweener {

	public Visual visual;

	public PointF start;
	public PointF end;

	public float height;

	public JumpTweener(Visual visual, PointF pos, float height, float time) {
		super(visual, time);

		this.visual = visual;
		start = visual.point();
		end = pos;

		this.height = height;
	}

	@Override
	protected void updateValues(float progress) {
		visual.point(PointF.inter(start, end, progress).offset(0,
				-height * 4 * progress * (1 - progress)));
	}
}
