package com.annatala.pixelponies.windows.elements;

import com.annatala.noosa.Group;
import com.annatala.pixelponies.windows.WndTabbed;

public class RankingTab extends LabeledTab {
	
	private Group page;
	
	public RankingTab(WndTabbed parent, String label, Group page ) {
		super( parent, label );
		this.page = page;
	}
	
	@Override
	public void select( boolean value ) {
		super.select( value );
		if (page != null) {
			page.setVisible(page.active = selected);
		}
	}
}