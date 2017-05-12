package com.zncm.mxgtd.ui;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.SpConstant;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ft.RemindFragment;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.utils.statusbar.StatusBarCompat;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dminter on 2016/11/21.
 */

public class QuickAddActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TaskData defaultTask;
    private String action = Constant.ADD_RD;
    private Calendar calendar = Calendar.getInstance();
    private Calendar remindCalendar = Calendar.getInstance();
    private TextView tvTime;
    private Spinner spTime;
    private Spinner spTimeRepeat;
    private int spChoose = 0;
    private int spRepeatChoose = 0;
    private Long remind_time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = bundle.getString("action");
        }
        defaultTask = DbUtils.getDefaultTk();
        if (defaultTask == null) {
            finish();
            return;
        }
        if (XUtil.notEmptyOrNull(action)) {
            if (action.equals(Constant.ADD_RD)) {
                newRd();
            } else if (action.equals(Constant.ADD_PROGRESS)) {
                add1Dlg();
            }
        }
        StatusBarCompat.translucentStatusBar(this);
    }

    void add1Dlg() {
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final EditText editText = new EditText(ctx);
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setHint("输入进展...");
        editText.setLines(5);
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
        editText.setHintTextColor(getResources().getColor(R.color.gray));
        editText.setBackgroundDrawable(new BitmapDrawable());
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    if (!MySp.get(SpConstant.isEnterSave, Boolean.class, false)) {
                        return false;
                    }
                    String content = editText.getText().toString().trim();
                    if (XUtil.notEmptyOrNull(content)) {
                        DbUtils.insertProgress(content);
                        editText.setText("");
                    } else {
                        XUtil.tShort("输入内容~");
                    }
                    finish();
                }
                return false;
            }

        });
        view.addView(editText);
        MaterialDialog md = new MaterialDialog.Builder(ctx)
                .customView(view, true)
                .positiveText("保存继续")
                .negativeText("添加")
                .neutralText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(materialDialog, false);
                                return;
                            }
                            DbUtils.insertProgress(content);
                            editText.setText("");
                            XUtil.dismissShowDialog(materialDialog, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {
                        XUtil.dismissShowDialog(materialDialog, true);
                        finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(dialog, false);
                                return;
                            }
                            DbUtils.insertProgress(content);
                            XUtil.dismissShowDialog(dialog, true);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();

    }

    private void newRd() {
        final String items[] = new String[]{"10秒", "30秒", "1分钟", "2分钟", "3分钟", "5分钟", "10分钟", "15分钟", "20分钟", "25分钟", "30分钟",
                "40分钟", "45分钟", "50分钟", "1小时", "2小时", "3小时", "4小时", "1天", "2天", "3天", "4天", "5天", "6天", "7天", "15天", "30天", "45天", "60天", "90天", "一年"};
        new MaterialDialog.Builder(ctx)
                .title("倒计时")
                .items(items)
                .neutralText("取消")
                .negativeText("自定义")
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                Long between = 0l;
                                between = RemindFragment.getRdTime(which, between);
                                Long remind_time = System.currentTimeMillis() + between;
                                String endTime = XUtil.getTimeYMDHM(remind_time);
                                String content = endTime + "(" + items[which] + ")";
                                RemindData remindData = new RemindData(content, EnumData.StatusEnum.ON.getValue(), defaultTask.getId(),
                                        remind_time, 0
                                );
                                DbUtils.addRd(remindData);
                                DbUtils.initRemind(ctx);
                                XUtil.tLong(XUtil.diffTime(remindData.getRemind_time()) + "后提醒");
                                finish();
                                return true;
                            }

                        }

                )
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        if (which == DialogAction.NEUTRAL) {
                            finish();
                        } else if (which == DialogAction.NEGATIVE) {
                            add2Dlg();
                        }

                    }
                })
                .cancelable(false)
                .show();
    }


    void add2Dlg() {
        calendar = Calendar.getInstance();
        LayoutInflater mInflater = LayoutInflater.from(ctx);
        View view = mInflater.inflate(R.layout.view_remind_dlg, null);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        XUtil.autoKeyBoardShow(editText);
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
        editText.setLines(2);
        editText.setHint("提醒内容...");
        editText.setBackgroundDrawable(new BitmapDrawable());
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        spTime = (Spinner) view.findViewById(R.id.spTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
                R.array.reminds_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTime.setAdapter(adapter);
        spTime.setVisibility(View.VISIBLE);
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spChoose = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTimeRepeat = (Spinner) view.findViewById(R.id.spTimeRepeat);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(ctx,
                R.array.reminds_repeat_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTimeRepeat.setAdapter(adapter2);
        spTimeRepeat.setVisibility(View.VISIBLE);
        spTimeRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spRepeatChoose = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        MaterialDialog md = new MaterialDialog.Builder(ctx)
                .customView(view, true)
                .positiveText("添加")
                .negativeText("提醒时间")
                .neutralText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            String time = tvTime.getText().toString().trim();

                            Long between = 0l;
                            if (!XUtil.notEmptyOrNull(time)) {

                                switch (spChoose) {
                                    case 0:
                                        between = 10 * Constant.SEC;
                                        break;
                                    case 1:
                                        between = 30 * Constant.SEC;
                                        break;
                                    case 2:
                                        between = 1 * Constant.MIN;
                                        break;
                                    case 3:
                                        between = 2 * Constant.MIN;
                                        break;
                                    case 4:
                                        between = 3 * Constant.MIN;
                                        break;
                                    case 5:
                                        between = 5 * Constant.MIN;
                                        break;
                                    case 6:
                                        between = 10 * Constant.MIN;
                                        break;
                                    case 7:
                                        between = 15 * Constant.MIN;
                                        break;
                                    case 8:
                                        between = 20 * Constant.MIN;
                                        break;
                                    case 9:
                                        between = 25 * Constant.MIN;
                                        break;
                                    case 10:
                                        between = 30 * Constant.MIN;
                                        break;
                                    case 11:
                                        between = 40 * Constant.MIN;
                                        break;
                                    case 12:
                                        between = 45 * Constant.MIN;
                                        break;
                                    case 13:
                                        between = 50 * Constant.MIN;
                                        break;
                                    case 14:
                                        between = 1 * Constant.HOUR;
                                        break;
                                    case 15:
                                        between = 2 * Constant.HOUR;
                                        break;
                                    case 16:
                                        between = 3 * Constant.HOUR;
                                        break;
                                    case 17:
                                        between = 4 * Constant.HOUR;
                                        break;
                                    case 18:
                                        between = 1 * Constant.DAY;
                                        break;
                                    case 19:
                                        between = 2 * Constant.DAY;
                                        break;
                                    case 20:
                                        between = 3 * Constant.DAY;
                                        break;
                                    case 21:
                                        between = 4 * Constant.DAY;
                                        break;
                                    case 22:
                                        between = 5 * Constant.DAY;
                                        break;
                                    case 23:
                                        between = 6 * Constant.DAY;
                                        break;
                                    case 24:
                                        between = 7 * Constant.DAY;
                                        break;
                                    case 25:
                                        between = 15 * Constant.DAY;
                                        break;
                                    case 26:
                                        between = 30 * Constant.DAY;
                                        break;
                                    case 27:
                                        between = 45 * Constant.DAY;
                                        break;
                                    case 28:
                                        between = 60 * Constant.DAY;
                                        break;
                                    case 29:
                                        between = 90 * Constant.DAY;
                                        break;
                                    case 30:
                                        between = 365 * Constant.DAY;
                                        break;
                                }
                                remind_time = System.currentTimeMillis() + between;
                            }
                            if (remind_time == null) {
                                XUtil.tShort("选择提醒时间~");
                                XUtil.dismissShowDialog(materialDialog, false);
                                return;
                            }
                            if (!XUtil.notEmptyOrNull(content)) {
                                content = XUtil.getTimeYMDHM(remind_time);
                            }
                            int tk_id = defaultTask.getId();
                            RemindData remindData = new RemindData(content, EnumData.StatusEnum.ON.getValue(), tk_id,
                                    remind_time, spRepeatChoose
                            );
                            DbUtils.addRd(remindData);
                            DbUtils.initRemind(ctx);
                            XUtil.tLong(XUtil.diffTime(remindData.getRemind_time()) + "后提醒");
                            finish();
                            XUtil.dismissShowDialog(materialDialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {
                        finish();
                        XUtil.dismissShowDialog(materialDialog, true);
                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        initDatePicker();
                        spTime.setVisibility(View.GONE);
                        XUtil.dismissShowDialog(materialDialog, false);
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();
    }

    private void initDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(2000, 2025);
        datePickerDialog.show(getSupportFragmentManager(), "datepicker");
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_quickadd;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        remindCalendar.set(year, month, day);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
        timePickerDialog.show(getSupportFragmentManager(), "timepicker");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        remindCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        remindCalendar.set(Calendar.MINUTE, minute);
        remind_time = remindCalendar.getTimeInMillis();
        if (tvTime != null) {
            tvTime.setText(XUtil.getTimeYMDHM(new Date(remindCalendar.getTimeInMillis())));
        }
    }
}
