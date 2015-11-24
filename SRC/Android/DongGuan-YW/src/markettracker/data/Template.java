package markettracker.data;

import java.util.*;

import markettracker.util.Constants.PanalType;

public class Template
{
	
	private String type;
	private String version;
	private String name;
	
	private String photodict;
	
	private String value;
	
	private String description;
	private String pltype;
	private int inputType = 0;
	
	private boolean isPhoto = false;
	
	private boolean isMustComplete = false;
	private boolean isComplete = false;
	
	private boolean isSubmit = false;
	
	private int onlyType;
	private int imgId = -1;
	
	private List<Panal> panalList;
	
	private List<ButtonConfig> buttonList;
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getVersion()
	{
		return version;
	}
	
	public void setVersion(String version)
	{
		this.version = version;
	}
	
	public List<Panal> getAllPanalList()
	{
		return panalList;
	}
	
	public void setPanalList(List<Panal> panalList)
	{
		this.panalList = panalList;
	}
	
	public void setPanal(Panal panal)
	{
		if (panalList == null)
			panalList = new ArrayList<Panal>();
		this.panalList.add(panal);
	}
	
	public void setUIItem(UIItem item)
	{
		if (panalList != null)
		{
			for (Panal panal : panalList)
			{
				if (panal.getId().equals(item.getContainerId()))
				{
					panal.setItem(item);
					break;
				}
			}
		}
	}
	
	public Panal getPanal(int index)
	{
		if (index >= 0 && panalList != null && index < panalList.size())
			return panalList.get(index);
		return null;
	}
	
	// public List<UIItem> getTableItem(String id) {
	// for (Panal panal : panalList) {
	// if (panal.getId().equals(id))
	// return panal.getItemList();
	// }
	// return null;
	// }
	
	public List<UIItem> getTableItem()
	{
		for (Panal panal : getAllPanalList())
		{
			if (panal.getType().equals("table"))
			{
				return panal.getItemList();
			}
		}
		return null;
	}
	
	public List<Panal> getPanalList()
	{
		List<Panal> list = new ArrayList<Panal>();
		if (getAllPanalList() != null)
		{
			for (Panal panal : getAllPanalList())
			{
				if (panal.getType().equals("panel") || panal.getType().equals("detail"))
				{
					list.add(panal);
				}
			}
		}
		return list;
	}

	public List<Panal> getDetailList()
	{
		List<Panal> list = new ArrayList<Panal>();
		if (getAllPanalList() != null)
		{
			for (Panal panal : getAllPanalList())
			{
				if (panal.getType().equals("detail"))
				{
					list.add(panal);
				}
			}
		}
		return list;
	}
	
	public boolean havePanal()
	{
		if (getCount() > 0)
		{
			for (Panal panal : panalList)
			{
				if (panal.getType().equals("panel"))
					return true;
			}
		}
		return false;
	}
	
	public boolean haveDetail()
	{
		if (getCount() > 0)
		{
			for (Panal panal : panalList)
			{
				if (panal.getType().equals("detail"))
					return true;
			}
		}
		return false;
	}
	
	public boolean haveTable()
	{
		if (getCount() > 0)
		{
			for (Panal panal : panalList)
			{
				if (panal.getType().equals("table"))
					return true;
			}
		}
		return false;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getCount()
	{
		if (panalList != null)
			return panalList.size();
		return 0;
	}
	
	public String getPltype()
	{
		return pltype;
	}
	
	public void setPltype(String pltype)
	{
		this.pltype = pltype;
	}
	
	public int getInputType()
	{
		return inputType;
	}
	
	public void setInputType(int inputType)
	{
		this.inputType = inputType;
	}
	
	public boolean isPhoto()
	{
		return isPhoto;
	}
	
	public void setPhoto(boolean isPhoto)
	{
		this.isPhoto = isPhoto;
	}
	
	public boolean isMustComplete()
	{
		return isMustComplete;
	}
	
	public void setMustComplete(boolean isMustComplete)
	{
		this.isMustComplete = isMustComplete;
	}
	
	public boolean isComplete()
	{
		return isComplete;
	}
	
	public void setComplete(boolean isComplete)
	{
		this.isComplete = isComplete;
	}
	
	public int getOnlyType()
	{
		return onlyType;
	}
	
	public void setOnlyType(int onlyType)
	{
		this.onlyType = onlyType;
	}
	
	/**
	 * 返回 description 的值
	 * 
	 * @return description
	 */
	
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * 设置 description 的值
	 * 
	 * @param description
	 */
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * 返回 value 的值
	 * 
	 * @return value
	 */
	
	public String getValue()
	{
		return value;
	}
	
	/**
	 * 设置 value 的值
	 * 
	 * @param value
	 */
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	/**
	 * 返回 isSubmit 的值
	 * 
	 * @return isSubmit
	 */
	
	public boolean isSubmit()
	{
		return isSubmit;
	}
	
	/**
	 * 设置 isSubmit 的值
	 * 
	 * @param isSubmit
	 */
	
	public void setSubmit(boolean isSubmit)
	{
		this.isSubmit = isSubmit;
	}
	
	/**
	 * 返回 photodict 的值
	 * 
	 * @return photodict
	 */
	
	public String getPhotodict()
	{
		return photodict;
	}
	
	/**
	 * 设置 photodict 的值
	 * 
	 * @param photodict
	 */
	
	public void setPhotodict(String photodict)
	{
		this.photodict = photodict;
	}
	
	
	/**
	 * 返回 imgId 的值
	 * 
	 * @return imgId
	 */
	
	public int getImgId()
	{
		return imgId;
	}
	
	/**
	 * 设置 imgId 的值
	 * 
	 * @param imgId
	 */
	
	public void setImgId(int imgId)
	{
		this.imgId = imgId;
	}
	
	public void setButton(ButtonConfig button)
	{
		if (this.buttonList == null)
			this.buttonList = new ArrayList<ButtonConfig>();
		this.buttonList.add(button);
	}
	
	/**
	 * 返回 buttonList 的值
	 * 
	 * @return buttonList
	 */
	
	public List<ButtonConfig> getButtonList()
	{
		return buttonList;
	}
	
	/**
	 * 设置 buttonList 的值
	 * 
	 * @param buttonList
	 */
	
	public void setButtonList(List<ButtonConfig> buttonList)
	{
		this.buttonList = buttonList;
	}
	
	public boolean havePhoto()
	{
		if (getCount() > 0)
		{
			for (Panal panal : panalList)
			{
				if (panal.getType().equals(PanalType.PHOTO))
					return true;
			}
		}
		return false;
	}
	
	public Panal getPhotoPanal()
	{
		// Panal list = new Panal();
		for (Panal panal : getAllPanalList())
		{
			if (panal.getType().equals(PanalType.PHOTO))
			{
				return panal;
			}
		}
		return null;
	}
	
}
