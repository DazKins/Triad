package com.dazkins.triad.networking;

import java.util.ArrayList;

import com.dazkins.triad.game.world.Chunk;

@SuppressWarnings("unchecked")
public class UpdateList
{
	private ArrayList<ArrayList> updateLists = new ArrayList<ArrayList>();
	
	public void purge()
	{
		updateLists.clear();
	}
	
	public UpdateList clone()
	{
		UpdateList newList = new UpdateList();
		for (ArrayList list : updateLists)
		{
			 newList.updateLists.add((ArrayList) list.clone());
		}
		return newList;
	}
	
	public ArrayList getUpdateListOfType(Class type)
	{
		for (ArrayList list : updateLists)
		{
			Object firstObj = list.get(0);
			if (firstObj != null)
			{
				if (type.isInstance(firstObj))
				{
					return list;
				}
			}
		}
		return new ArrayList();
	}
	
	public void addUpdate(Object u)
	{
		ArrayList list = getUpdateListOfType(u.getClass());
		if (!list.isEmpty())
		{
			list.add(u);	
		} else 
		{
			ArrayList n = new ArrayList();
			n.add(u);
			updateLists.add(n);
		}
	}
}