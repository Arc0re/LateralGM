/*
 * Copyright (C) 2007 Clam <clamisgood@gmail.com>
 *
 * This file is part of LateralGM.
 * LateralGM is free software and comes with ABSOLUTELY NO WARRANTY.
 * See LICENSE for details.
 */

package org.lateralgm.components.impl;

import static org.lateralgm.main.Util.deRef;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.tree.DefaultMutableTreeNode;

import org.lateralgm.messages.Messages;
import org.lateralgm.resources.GmObject;
import org.lateralgm.resources.Resource;
import org.lateralgm.resources.ResourceReference;
import org.lateralgm.resources.sub.Event;
import org.lateralgm.resources.sub.MainEvent;

public class EventNode extends DefaultMutableTreeNode implements Transferable
	{
	private static final long serialVersionUID = 1L;
	public static final DataFlavor EVENTNODE_FLAVOR = new DataFlavor(
			DataFlavor.javaJVMLocalObjectMimeType,"Event Node");
	private DataFlavor[] flavors = { EVENTNODE_FLAVOR };
	public int mainId;
	public int eventId;
	public ResourceReference<GmObject> other;

	public EventNode(String text)
		{
		this(text,-1,0);
		}

	public EventNode(int mainId)
		{
		this(Messages.getString("MainEvent.EVENT" + mainId),mainId,0); //$NON-NLS-1$
		}

	public EventNode(String text, int mainId, int eventId)
		{
		setUserObject(text);
		this.mainId = mainId;
		this.eventId = eventId;
		}

	public EventNode(int mainId, ResourceReference<GmObject> other)
		{
		Resource<GmObject,?> r = deRef(other);
		setUserObject(r == null ? "" : r.getName());
		this.mainId = mainId;
		this.other = other;
		}

	public void add(int mainId, int eventId)
		{
		add(new EventNode(Event.eventName(mainId,eventId),mainId,eventId));
		}

	public void add(int mainId, ResourceReference<GmObject> other)
		{
		add(new EventNode(mainId,other));
		}

	public void add(int mainId)
		{
		add(new EventNode(Messages.getString("MainEvent.EVENT" + mainId),mainId,0)); //$NON-NLS-1$
		}

	public boolean isValid()
		{
		switch (mainId)
			{
			case MainEvent.EV_KEYBOARD:
			case MainEvent.EV_KEYPRESS:
			case MainEvent.EV_KEYRELEASE:
				return Event.KEYS.containsKey(eventId);
			case MainEvent.EV_COLLISION:
				return other != null;
			default:
				return true;
			}
		}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
		{
		if (flavor != EVENTNODE_FLAVOR) throw new UnsupportedFlavorException(flavor);
		return this;
		}

	public DataFlavor[] getTransferDataFlavors()
		{
		return flavors;
		}

	public boolean isDataFlavorSupported(DataFlavor flavor)
		{
		return flavor.equals(EVENTNODE_FLAVOR);
		}

	/*public void setIcon(Icon ico)
		{
		if (ico == null) return;
		super.setIcon(ico);
		}*/
	}
