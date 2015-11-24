package markettracker.util;

import markettracker.util.Constants.ControlType;
import orient.champion.business.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class NumericKeyboard {

	public static interface OnNumberClickListener {
		public void OnNumberClick(String numberStr);
	}

	private LinearLayout mNumericLayout;
	private Button[] mNumericBtns;

	/**
	 * 缓存输入的整型数�?
	 */
	private StringBuilder mNumericString = new StringBuilder();

	/**
	 * 针对输入小数的时�?
	 */
	private boolean mInputDecimal = false;
	private StringBuilder mDecimalString = new StringBuilder();

	/**
	 * 当前输入的类�?
	 */
	private ControlType mCurrInputType = ControlType.INTEGER;

	private OnNumberClickListener mOnNumberClickListener;

	private OnClickListener mOnCLickListener = new OnClickListener() {
		// @Override
		public void onClick(View v) {
			// String sss = mNumericString.toString();
			switch (v.getId()) {

			case R.id.Numeric_Keyboard_Zero:
				if (mCurrInputType == ControlType.INTEGER) {
					// if (mNumericString.length() != 0)
					mNumericString.append(0);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(0);
					} else {
						// if (mNumericString.length() != 0)
						mNumericString.append(0);
					}
				}
				break;
			case R.id.Numeric_Keyboard_One:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(1);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(1);
					} else {
						mNumericString.append(1);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Two:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(2);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(2);
					} else {
						mNumericString.append(2);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Three:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(3);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(3);
					} else {
						mNumericString.append(3);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Four:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(4);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(4);
					} else {
						mNumericString.append(4);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Five:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(5);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(5);
					} else {
						mNumericString.append(5);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Six:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(6);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(6);
					} else {
						mNumericString.append(6);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Seven:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(7);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(7);
					} else {
						mNumericString.append(7);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Eight:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(8);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(8);
					} else {
						mNumericString.append(8);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Nine:
				if (mCurrInputType == ControlType.INTEGER) {
					mNumericString.append(9);
				} else {
					if (mInputDecimal) {
						mDecimalString.append(9);
					} else {
						mNumericString.append(9);
					}
				}
				break;
			case R.id.Numeric_Keyboard_Dot:
				if (mCurrInputType == ControlType.INTEGER) {
					return;
				} else {
					mInputDecimal = true;
					return;
				}
			case R.id.Numeric_Keyboard_Del:
				if (mCurrInputType == ControlType.INTEGER) {
					System.out.println(mNumericString.toString());
					System.out.println(mNumericString.length());
					if (mNumericString.length() > 0) {
						mNumericString
								.deleteCharAt(mNumericString.length() - 1);
					} else
						mNumericString = new StringBuilder();
					// return;
				} else {
					if (mInputDecimal) {
						if (mDecimalString.length() > 0)
							mDecimalString
									.deleteCharAt(mDecimalString.length() - 1);
						if (mDecimalString.length() == 0)
							mInputDecimal = false;
					} else {
						if (mNumericString.length() > 0)
							mNumericString
									.deleteCharAt(mNumericString.length() - 1);
					}
				}
				break;
			}

			if (mCurrInputType == ControlType.INTEGER && !validateInteger()) {
				if (mNumericString.length() > 0)
					mNumericString.deleteCharAt(mNumericString.length() - 1);
				return;
			}

			if (null != mOnNumberClickListener) {
				if (mCurrInputType == ControlType.INTEGER) {
					mOnNumberClickListener.OnNumberClick(getNumericString());
				} else {
					mOnNumberClickListener.OnNumberClick(getDecimalString());
				}
			}
		}
	};

	public NumericKeyboard(View view) {
		// View view = LayoutInflater.from(activity).inflate(
		// R.layout.keyboard, null);
		mNumericLayout = (LinearLayout) view
				.findViewById(R.id.LinearLayout_Numeric_Keyboard);
		mNumericBtns = new Button[12];
		mNumericBtns[0] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Zero);
		mNumericBtns[1] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_One);
		mNumericBtns[2] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Two);
		mNumericBtns[3] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Three);
		mNumericBtns[4] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Four);
		mNumericBtns[5] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Five);
		mNumericBtns[6] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Six);
		mNumericBtns[7] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Seven);
		mNumericBtns[8] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Eight);
		mNumericBtns[9] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Nine);
		mNumericBtns[10] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Dot);
		mNumericBtns[11] = (Button) mNumericLayout
				.findViewById(R.id.Numeric_Keyboard_Del);
		for (int i = 0; i < mNumericBtns.length; i++) {
			mNumericBtns[i].setOnClickListener(mOnCLickListener);
		}
	}

	/**
	 * 验证输入的数字是否在合法的范围之�?
	 */
	private boolean validateInteger() {
		if (mNumericString.length() < 10)
			return true;
		try {
			Integer.valueOf(mNumericString.toString());
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public String getNumericString() {
		return mNumericString.toString();
	}

	public String getDecimalString() {
		StringBuilder sb = new StringBuilder();
		if (mNumericString.length() > 0) {
			sb.append(mNumericString.toString());
			sb.append(".");
			if (mDecimalString.length() > 0)
				sb.append(mDecimalString.toString());
			else
				sb.append("00");
		} else {
			sb.append("0.");
			if (mDecimalString.length() > 0)
				sb.append(mDecimalString.toString());
			else
				sb.append("00");
		}
		return sb.toString();
	}

	/**
	 * 设置数字键盘的可见与不可�?
	 * 
	 * @param visibility
	 */
	public void setVisibility(int visibility, ControlType inputType) {
		mNumericLayout.setVisibility(visibility);
		if (null != inputType)
			mCurrInputType = inputType;
		mNumericString.delete(0, mNumericString.length());
		mDecimalString.delete(0, mDecimalString.length());
		mInputDecimal = false;
	}

	public void setOnNumberClickListener(
			OnNumberClickListener onNumberClickListener) {
		mOnNumberClickListener = onNumberClickListener;
	}

}