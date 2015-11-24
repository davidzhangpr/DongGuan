package markettracker.data;

import java.util.*;

public class TemplateGroup {

	private String id;
	private String name;
	private List<Template> templateList;
	private int imgId = -1;
	private boolean showTitle = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Template> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<Template> templateList) {
		this.templateList = templateList;
	}

	public void setTemplate(Template temp) {
		if (templateList == null)
			templateList = new ArrayList<Template>();
		this.templateList.add(temp);
	}

	public Template getTemplate(int index) {
		if (index >= 0 && templateList != null && index < templateList.size())
			return templateList.get(index);
		return null;
	}

	/**
	 * 返回 imgId 的值
	 * 
	 * @return imgId
	 */

	public int getImgId() {
		return imgId;
	}

	/**
	 * 设置 imgId 的值
	 * 
	 * @param imgId
	 */

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

}
