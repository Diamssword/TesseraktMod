package com.diamssword.tesserakt.storage;

import net.minecraftforge.items.ItemStackHandler;

public class TItemStorage extends ItemStackHandler{

	private Tesserakt parent;
	public TItemStorage(int capacity,Tesserakt parent) {
		super(capacity);
		this.parent = parent;
	}
	@Override
    protected void onContentsChanged(int slot)
    {
    	Tesserakt.save(parent.world,parent);
    }
}
