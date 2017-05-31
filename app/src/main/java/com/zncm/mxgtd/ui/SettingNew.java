package com.zncm.mxgtd.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.DetailsData;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.data.SpConstant;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ft.DetailsFragment;
import com.zncm.mxgtd.ft.RefreshEvent;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MyPath;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by dminter on 2016/11/1.
 */

public class SettingNew extends MaterialSettings {

    Activity ctx;
    private static SettingNew instance;

    public static SettingNew getInstance() {
        return instance;
    }


    private File[] files = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ctx = this;
        instance = this;
        XUtil.verifyStoragePermissions(this);


        addItem(new HeaderItem(this).setTitle("通用"));

        addItem(new TextItem(ctx, "").setTitle("笔记本整理").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {

                startActivity(new Intent(ctx, PjActivity.class));
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("监听剪切板").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                MySp.setClipboardListen(isChecked);
            }
        }).setDefaultValue(MySp.getClipboardListen()));
        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("简要显示").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                MySp.setShowSimple(isChecked);
            }
        }).setDefaultValue(MySp.getShowSimple()));
        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("回车保存").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                MySp.put(SpConstant.isEnterSave, isChecked);
            }
        }).setDefaultValue(MySp.get(SpConstant.isEnterSave, Boolean.class, false)));


        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("网格大小-列数").setSubtitle(MySp.getSpanCount() + "").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(final TextItem textItem) {
                final ArrayList<String> items = new ArrayList<>();
                int pos = 0;
                for (int i = 1; i <= 4; i++) {
                    if (MySp.getSpanCount() == i) {
                        pos = i - 1;
                    }
                    items.add(i + "");
                }
                XUtil.themeMaterialDialog(ctx)
                        .title("网格大小-列数")
                        .items(items)
                        .itemsCallbackSingleChoice(pos, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                try {
                                    MySp.setSpanCount(Integer.parseInt(text.toString()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                textItem.updateSubTitle(text.toString());
                                return false;
                            }
                        })
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                            }
                        })
                        .show();
            }
        }));

        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("强提醒").setSubtitle("使用默认闹铃声音做提醒音乐\n注：需开启悬浮窗，操作后方可关闭").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                MySp.setIsBigRing(isChecked);
            }
        }).setDefaultValue(MySp.getIsBigRing()));





        addItem(new DividerItem(ctx));
        addItem(new CheckboxItem(this, "").setTitle("自动开启夜间模式").setSubtitle("开始时间18:00，结束时间6:00").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {

            @Override
            public void onCheckedChange(CheckboxItem checkboxItem, boolean b) {
                MySp.setIsAutoNight(b);
            }
        }).setDefaultValue(MySp.getIsAutoNight()));







        String taskTitle ="";
        final TaskData taskData = DbUtils.getTkById(MySp.getDefaultTk());
        if (taskData!=null&&XUtil.notEmptyOrNull(taskData.getTitle())){
            taskTitle =taskData.getTitle();
        }
        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("默认笔记本").setSubtitle(taskTitle).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {

                Intent newIntent = new Intent(ctx, TkDetailsActivity.class);
                newIntent.putExtra(Constant.KEY_PARAM_DATA, taskData);
                startActivity(newIntent);
            }
        }));


        String pjTitle ="";
        final ProjectData projectData = DbUtils.getPjById(MySp.getDefaultPj());
        if (projectData!=null&&XUtil.notEmptyOrNull(projectData.getTitle())){
            pjTitle =projectData.getTitle();
        }
        addItem(new DividerItem(ctx));
        addItem(new TextItem(this, "").setTitle("默认笔记本组").setSubtitle(pjTitle).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                Intent    newIntent = new Intent(ctx, ProjectDetailsActivity.class);
                newIntent.putExtra(Constant.KEY_PARAM_DATA, projectData);
                startActivity(newIntent);
            }
        }));










        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("数据备份").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                backUpDbDo();
            }
        }));
        addItem(new TextItem(ctx, "").setTitle("数据恢复").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                recoverDbDo();
            }
        }));

        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("特别感谢").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                thank();
            }
        }));
        addItem(new DividerItem(ctx));
        addItem(new TextItem(ctx, "").setTitle("浙水之南").setSubtitle("微信:xm0ff255").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                XUtil.copyText(SettingNew.this, "xm0ff255");
            }
        }));
        addItem(new DividerItem(ctx));
        String buildDate  = "2017-04-09 07:24:10";
        addItem(new TextItem(ctx, "").setTitle("检查更新").setSubtitle("当前版本:" + getVersionName()+" @"+buildDate).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                XUtil.openUrl(Constant.update_url);
            }
        }));

    }


    private void thank() {
        XUtil.themeMaterialDialog(ctx)
                .title("特别感谢")
                .content("AlipayZeroSdk\nmaterial-dialogs\nmaterialtabstrip\normlite\nmaterialsearchview\n")
                .positiveText("知")
                .show();
    }


    private String getVersionName() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            return "";
        }
    }


    @Override
    public StorageInterface initStorageInterface() {
        return null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("back")) {
            backDo();
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        backDo();
    }


    private void backDo() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("back").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_arrow_back)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    private void backUpDbDo() {
        try {
            String newPath = MyPath.getPathDb() + File.separator + XUtil.getDateY_M_D() + File.separator + Constant.DB_NAME;
            boolean flag = XUtil.copyFileTo(new File(Constant.DB_PATH), new File(newPath));
            if (flag) {
                XUtil.tLong("已备份~ ");
            } else {
                XUtil.tShort("数据备份失败~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 对文件进行排序
    private void sortFilesByDirectory(File[] files) {
        try {
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    if (f1.isDirectory() && f2.isFile())
                        return -1;
                    if (f1.isFile() && f2.isDirectory())
                        return 1;
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void recoverDbDo() {

        File tmpDir = new File(MyPath.getPathDb());
        if (tmpDir.exists() && tmpDir.length() > 0) {
            files = tmpDir.listFiles();
        } else {
            XUtil.tShort("还没有备份,请先备份~");
            return;
        }

        sortFilesByDirectory(files);
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ListView listView = new ListView(ctx);
        List<Map<String, String>> nameList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < files.length; i++) {
            Map<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("name", files[i].getName());
            nameList.add(nameMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(ctx,
                nameList, R.layout.cell_pj,
                new String[]{"name"},
                new int[]{R.id.tvTitle});
        listView.setAdapter(adapter);
        view.addView(listView);


        String item[] = null;
        item = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            item[i] = files[i].getName();
        }


        MaterialDialog md =  XUtil.themeMaterialDialog(ctx)
//                .customView(view)
                .title("数据恢复")
                .items(item)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, final int pos, CharSequence charSequence) {
                        XUtil.themeMaterialDialog(ctx)
                                .title("注意!!!")
                                .content("恢复到之前版本,当前版本数据将会丢失,请先备份!")
                                .positiveText("仍然恢复")
                                .negativeText("取消")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog materialDialog) {
                                        String path = files[pos].getAbsolutePath();
                                        recoverMath(path);
                                    }
                                })
                                .show();
                        materialDialog.dismiss();
                    }
                })
                .positiveText("保留最新备份")
                .negativeText("选择文件")
                .neutralText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        for (int j = 0; j < files.length - 1; j++) {
                            String path = files[j].getPath();
                            XUtil.deleteFile(new File(path));
                        }
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                        materialDialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        showFileChooser();
                        materialDialog.dismiss();
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();

    }

    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件导入/支持txt，xml，csv，db"), 103);
        } catch (android.content.ActivityNotFoundException ex) {
            XUtil.tShort("没有找到文件管理器");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 103:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = XUtil.getPathFromUri(ctx, uri);
                    importData(path);
                }
                break;
        }
    }

    private void importData(final String path) {
        boolean canImport = false;
        File file = new File(path);
        String fileName = "";
        if (file.exists()) {
            fileName = file.getName();
            if (fileName.endsWith(".txt") || fileName.endsWith(".xml") ||
                    fileName.endsWith(".csv") || fileName.endsWith(".db")) {
                canImport = true;
            }
        }
        if (canImport) {
            if (fileName.endsWith(".db")) {
                XUtil.themeMaterialDialog(ctx)
                        .title("注意!!!")
                        .content("恢复到之前版本,当前版本数据将会丢失,请先备份!")
                        .positiveText("仍然恢复")
                        .negativeText("取消")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                recoverMath(path);
                            }
                        })
                        .show();
            } else {
                GetData getData = new GetData();
                getData.execute(path);
            }
            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.MAIN.getValue()));
        } else {
            XUtil.tShort("文件格式非法~");
        }
    }

    class GetData extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {
            try {
                DbUtils.insertProgressForCsv(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Boolean canLoadMore) {
            super.onPostExecute(canLoadMore);
            XUtil.tShort("导入成功~");
        }
    }

    private void recoverMath(final String path) {
        int x = new Random().nextInt(50);
        int y = new Random().nextInt(50);
        final int result = x + y;
        final EditText editText = new EditText(ctx);
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setHint("输入结果...");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);
        XUtil.themeMaterialDialog(ctx)
                .customView(view, true)
                .title("恢复确认(" + x + "+" + y + " =?)")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        String res = editText.getText().toString();
                        if (XUtil.notEmptyOrNull(res) && res.equals(String.valueOf(result))) {

                            try {
                                String filePath = path;
                                if (!path.contains(Constant.DB_NAME)) {
                                    filePath = path + File.separator + Constant.DB_NAME;
                                }
                                boolean flag = XUtil.copyFileTo(new File(filePath), new File(Constant.DB_PATH));
                                if (flag) {
                                    XUtil.tShort("数据恢复成功~");
                                } else {
                                    XUtil.tShort("数据恢复失败~");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            XUtil.tShort("回答错误~");
                        }
                    }
                })
                .show();


    }


}
