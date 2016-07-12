package com.dazkins.triad.networking;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class UpdateList
{
	private ArrayList<ArrayList> updateLists = new ArrayList<ArrayList>();
	
	public UpdateList clone()
	{
		UpdateList newList = new UpdateList();
		for (int i = 0; i < updateLists.size(); i++)
		{
			 newList.updateLists.add((ArrayList) updateLists.get(i).clone());
		}
		return newList;
	}
	
	public synchronized ArrayList getAndPurgeUpdateListOfType(Class type)
	{
		ArrayList original = getUpdateListOfType(type);
		ArrayList list = (ArrayList) original.clone();
		updateLists.remove(original);
		return list;
	}
	
	private ArrayList getUpdateListOfType(Class type)
	{
		for (int i = 0; i < updateLists.size(); i++)
		{
			ArrayList list = updateLists.get(i);
			if (list != null)
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
		}
		return new ArrayList();
	}
	
	public synchronized void addUpdate(Object u)
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