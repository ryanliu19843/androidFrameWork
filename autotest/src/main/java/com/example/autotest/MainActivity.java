package com.example.autotest;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.example.autotest.act.ActColtest;
import com.example.autotest.dataformat.DfSimple;
import com.example.autotest.frg.FrgComn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdx.framework.activity.IndexAct;
import com.mdx.framework.activity.LoadingAct;
import com.mdx.framework.activity.MActivity;
import com.mdx.framework.activity.NoTitleAct;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.activity.TitleTransStatusAct;
import com.mdx.framework.activity.TitleTransparentAct;
import com.mdx.framework.autofit.AutoFitFragment;
import com.mdx.framework.autofit.AutoFitUtil;
import com.mdx.framework.commons.ParamsManager;
import com.mdx.framework.config.ApiConfig;
import com.mdx.framework.dialog.PhotoShow;
import com.mdx.framework.server.api.OnApiInitListener;
import com.mdx.framework.utility.Device;
import com.mdx.framework.utility.Helper;
import com.mdx.framework.utility.permissions.PermissionRequest;
import com.mdx.framework.widget.getphoto.PopUpdataPhoto;
import com.mdx.framework.widget.pagerecycleview.ada.CardAdapter;
import com.mdx.framework.widget.selectphotos.SelectPhoto;
import com.udows.canyin.proto.MEmpAccount;

public class MainActivity extends MActivity {

    public Toolbar toolbar;
    public LinearLayout content_main;
    public FloatingActionButton fab;
    public com.mdx.framework.widget.MImageView image;
    public com.mdx.framework.widget.banner.CirleCurr ciclecurr;
    public Button stop;
    public Button start;
    public Button pause;
    public Button setloop;
    public Button photoslec;
    public Button autofittest;
    public Button indexact;
    public Button loadingact;
    public Button titleAct;
    public Button titleTransparentAct;
    public Button noTitleAct;
    public Button titleTransStatusAct;
    public Button startmain;

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        findVMethod();

        Helper.requestPermissions(new PermissionRequest() {
            @Override
            public void onGrant(String[] permissions, int[] grantResults) {
                super.onGrant(permissions, grantResults);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }


    private void findVMethod() {
        toolbar = findViewById(R.id.toolbar);
        content_main = findViewById(R.id.content_main);
        fab = findViewById(R.id.fab);
        image = findViewById(R.id.image);
        ciclecurr = findViewById(R.id.ciclecurr);
        stop = findViewById(R.id.stop);
        start = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        setloop = findViewById(R.id.setloop);
        photoslec = findViewById(R.id.photoslec);
        autofittest = findViewById(R.id.autofittest);
        titleAct = findViewById(R.id.titleAct);
        titleTransparentAct = findViewById(R.id.titleTransparentAct);
        noTitleAct = findViewById(R.id.noTitleAct);
        titleTransStatusAct = findViewById(R.id.titleTransStatusAct);
        indexact = findViewById(R.id.indexact);
        loadingact = findViewById(R.id.loadingact);
        startmain = findViewById(R.id.startmain);

        DfSimple df = new DfSimple();
        CardAdapter cardAdapter = df.getCardAdapter(this, null, 0);


        ApiConfig.setAutoApiInitParams(new OnApiInitListener() {
            @Override
            public String[][] onApiInitListener(Object... objs) {
                Object obj = ParamsManager.getValue("user");
                String user = "", code = "", verify = "";
                if (obj != null) {
                    MEmpAccount account = (MEmpAccount) obj;
                    user = account.id;
                    verify = account.verify;
                    code = account.storeId;
                }
//                code="1da627b984594cd28b184350d532a7df";
//                user="548d3a956d604d66ab343827e503bc2b";
//                verify="73264882-3e9f-40ba-830a-fcaa81b8b192";
                return new String[][]{{"appid", code},
                        {"deviceid", Device.getId() + "@emp"}, {"userid", user},
                        {"areaCode", ""}, {"verify", verify}};
            }
        });


        com.udows.wgh.proto.ApisFactory.getApiindex().set().setHasPage(true);

//        ciclecurr.setAdapter(cardAdapter);

//        autofittest.setOnClickListener((v)->{AutoFitUtil.startActivity(getContext(),0,AutoFitFragment.class,NoTitleAct.class,R.layout.frg_autofitlogin);});
        autofittest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = ParamsManager.getValue("user");
//                if(obj==null){
//                    AutoFitUtil.startActivity(getContext(),0,AutoFitFragment.class,NoTitleAct.class,R.layout.frg_autofitlogin);
//                }else{
//                    AutoFitUtil.startActivity(getContext(),0,AutoFitFragment.class,NoTitleAct.class,R.layout.frg_autofittest);
//                }
//                AutoFitUtil.openpop(v,R.layout.frg_autofittest,new HashMap<String, Object>());
                AutoFitUtil.startActivity(getContext(), 0, AutoFitFragment.class, NoTitleAct.class, R.layout.pop_select_test);

            }
        });

        startmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(MainActivity.this, 0, R.layout.frg_main, IndexAct.class);
            }
        });
        titleAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(MainActivity.this, FrgComn.class, TitleAct.class);
            }
        });

        noTitleAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(MainActivity.this, FrgComn.class, NoTitleAct.class);
            }
        });

        titleTransparentAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(MainActivity.this, FrgComn.class, TitleTransparentAct.class);
            }
        });
        titleTransStatusAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(MainActivity.this, FrgComn.class, TitleTransStatusAct.class);
            }
        });

        indexact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(MainActivity.this, FrgComn.class, IndexAct.class);
            }
        });

        loadingact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.startActivity(MainActivity.this, FrgComn.class, LoadingAct.class);
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.stop(false);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image.isRunning()) {
                    image.pause();
                } else {
                    image.start();
                }
            }
        });
        photoslec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.getPhoto(getContext(), new PopUpdataPhoto.OnReceiverPhoto() {
                    @Override
                    public void onReceiverPhoto(String photoPath, int width, int height) {
                        Log.d("test", photoPath);
                    }
                }, 1, 1, 1, 1);

//                Helper.getPhotos(getContext(), new PopUpdataPhoto.OnReceiverPhotos() {
//                    @Override
//                    public void onReceiverPhoto(ArrayList<String> photos) {
//                        Log.d("test",photos.toString());
//                    }
//                },5);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.pause();
                Log.d("test", "" + image.getNumberOfFrames());
            }
        });

        setloop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setLoopCount(0);
            }
        });

//        image.setObj(R.drawable.tylt_bt_feilei1_h);
        image.setClickColor(0x55555555, -1);
        image.stop(false);
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                image.stop(false);
//                PhotoShow photoShow = new PhotoShow(getContext(), "ASSETS:tylt_bg_youdoushi_n.png");
//                photoShow.show();
//            }
//        });
    }
}
