package com.silence.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.silence.activity.MainActivity;
import com.silence.signcalendar.SignCalendar;
import com.silence.signcalendar.SignCalendarReq;
import com.silence.utils.Const;
import com.silence.word.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {

    private View.OnClickListener listener;

    private SignCalendar calendar;
    private String date;
    private TextView btn_sign;
    private TextView tv_sign_year_month;
    private SignCalendarReq signCalendarReq;
    private SignCalendarReq.DataBean dataBean;
    List<String> list = new ArrayList<>();//list中存储的格式为2019-06-02
    private int month;
    private int year;
    private RelativeLayout rlGetGiftData;
    private TextView tvGetSunValue;
    private ImageView ivSun;
    private ImageView ivSunBg;
    private RelativeLayout rlQuedingBtn;
    private RelativeLayout rlBtnSign;
    private ImageView signBack;
    private boolean isSign;

    public static CalendarFragment newInstance(){
        Bundle arguments = new Bundle();
        CalendarFragment tabContentFragment = new CalendarFragment();
        tabContentFragment.setArguments(arguments);

        return tabContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View contentView = inflater.inflate(R.layout.fragment_sign_calendar, null);
        /*ImageView i8show_attention_back = (ImageView) contentView.findViewById(R.id.i8show_attention_back);
        TextView i8show_attention_tittle = (TextView) contentView.findViewById(R.id.i8show_attention_tittle);
        TextView tv_sign_year_month = (TextView) contentView.findViewById(R.id.tv_sign_year_month);
        TextView btn_sign = (TextView) contentView.findViewById(R.id.btn_sign);
        ImageView iv_huode = (ImageView) contentView.findViewById(R.id.iv_huode);
        ImageView iv_sun_bg = (ImageView) contentView.findViewById(R.id.iv_sun_bg);
        ImageView iv_sun = (ImageView) contentView.findViewById(R.id.iv_sun);
        TextView tv_text_one = (TextView) contentView.findViewById(R.id.tv_text_one);*/

        initData();
        //savedInstanceState.putSerializable("userInfos", (Serializable) signCalendarReq);
        //模拟传值

        //接收传递过来的数据
        final SignCalendarReq signCalendarReq = (SignCalendarReq) getActivity().getIntent().getSerializableExtra("userInfos");

        //获取当前的月份
        month = Calendar.getInstance().get(Calendar.MONTH);
        //获取当前的年份
        year = Calendar.getInstance().get(Calendar.YEAR);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前时间
        Date curDate = new Date(System.currentTimeMillis());
        date = formatter.format(curDate);

        calendar = (SignCalendar) contentView.findViewById(R.id.sc_main);  //com.silence.signcalendar.SignCalendar
        btn_sign = (TextView) contentView.findViewById(R.id.btn_sign);
        tv_sign_year_month = (TextView) contentView.findViewById(R.id.tv_sign_year_month);
        rlGetGiftData = (RelativeLayout) contentView.findViewById(R.id.rl_get_gift_view);
        tvGetSunValue = (TextView) contentView.findViewById(R.id.tv_text_one);
        ivSun = (ImageView) contentView.findViewById(R.id.iv_sun);
        ivSunBg = (ImageView) contentView.findViewById(R.id.iv_sun_bg);
        signBack = (ImageView) contentView.findViewById(R.id.i8show_attention_back);
        rlQuedingBtn = (RelativeLayout) contentView.findViewById(R.id.rl_queding_btn);
        rlBtnSign = (RelativeLayout) contentView.findViewById(R.id.rl_btn_sign);

        //设置当前日期
        tv_sign_year_month.setText(year + "年" + (month + 1) + "月");

        if (signCalendarReq != null) {
            if (signCalendarReq.getState().getCode() == 1) {//1成功，0失败
                dataBean = signCalendarReq.getData();
                //获取当月已签到的日期
                String signDay = dataBean.getSignDay();
                String[] splitDay = signDay.split(",");

                //list中存储的格式为2019-06-02
                for (int i = 0; i < splitDay.length; i++) {
                    if (Integer.parseInt(splitDay[i]) < 10) {
                        if (month < 10) {
                            list.add(year + "-0" + (month + 1) + "-0" + splitDay[i]);
                        } else {
                            list.add(year + "-" + (month + 1) + "-0" + splitDay[i]);
                        }

                    } else {
                        if (month < 10) {
                            list.add(year + "-0" + (month + 1) + "-" + splitDay[i]);
                        } else {
                            list.add(year + "-" + (month + 1) + "-" + splitDay[i]);
                        }
                    }
                }


                calendar.addMarks(list, 0);

                if (dataBean.getIsSign() == 1) {//1是已签到，0是未签到
                    rlBtnSign.setBackgroundResource(R.drawable.btn_sign_calendar_no);
                    btn_sign.setText("已签到");
                    rlBtnSign.setClickable(false);
                } else {
                    rlBtnSign.setBackgroundResource(R.drawable.btn_sign_calendar);
                    btn_sign.setText("签 到");
                }
            }
        }

        btn_sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isSign) {
                    signCalendarData();
                }

            }
        });

        rlQuedingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlGetGiftData.setVisibility(View.GONE);
            }
        });

        /*signBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });*/

        listener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (v.getId()) {
                    case R.id.i8show_attention_back:
                        //finish();
                        intent.setClass(getActivity(), MainActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };
        signBack.setOnClickListener(listener);
        return contentView;
    }

    private void initData() {
        //模拟请求后台返回初始化数据
        signCalendarReq = new SignCalendarReq();

        SignCalendarReq.StateBean state = new SignCalendarReq.StateBean();
        state.setCode(1);
        state.setMsg("成功");
        signCalendarReq.setState(state);

        SignCalendarReq.DataBean data = new SignCalendarReq.DataBean();
        data.setConSign(1);
        data.setIsSign(0);
        data.setSignDay("1,2");
        data.setUid("3347922");
        signCalendarReq.setData(data);
    }


    private void signCalendarData() {
        //模拟请求后台数据签到已成功

        rlGetGiftData.setVisibility(View.VISIBLE);
        rlBtnSign.setBackgroundResource(R.drawable.btn_sign_calendar_no);
        btn_sign.setText("已签到");
        isSign = true;//模拟当天已签到


        ivSun.setImageResource(R.drawable.i8live_sun);
        tvGetSunValue.setText("恭喜获得10个阳光值");


        Animation operatingAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_anim_online_gift);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        ivSunBg.startAnimation(operatingAnim);

        //list.add("2017-11-18");
        list.add(date);
        calendar.addMarks(list, 0);

    }

}
