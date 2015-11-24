package markettracker.data;

import java.util.*;

public class TemGroupList {

	private List<TemplateGroup> templateList;

	public List<TemplateGroup> getTempGroupList() {
		return templateList;
	}

	public void setTempGroupList(List<TemplateGroup> tempGroupList) {
		this.templateList = tempGroupList;
	}

	public void setTempGroup(TemplateGroup tempGroup) {
		if (templateList == null)
			templateList = new ArrayList<TemplateGroup>();
		this.templateList.add(tempGroup);
	}

	public TemplateGroup getTempGroup(int index) {
		if (index >= 0 && templateList != null && index < templateList.size())
			return templateList.get(index);
		return null;
	}

	public int getCount() {
		if (templateList != null)
			return templateList.size();
		return 0;
	}

}
