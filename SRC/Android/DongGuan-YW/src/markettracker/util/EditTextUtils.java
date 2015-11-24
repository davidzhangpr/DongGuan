package markettracker.util;

import android.widget.EditText;

public class EditTextUtils {

	// 判断是否为空
	public static boolean isEmpty(EditText et) {
		if (StringUtils.isEmpty(getString(et)))
			return true;
		return false;
	}

	// 获取输入内容
	public static String getString(EditText et) {
		return et.getText().toString().trim();
	}
}
