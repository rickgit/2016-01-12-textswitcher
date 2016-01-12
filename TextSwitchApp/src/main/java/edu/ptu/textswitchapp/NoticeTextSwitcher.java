package edu.ptu.textswitchapp;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

/**
 * 滚动效果，每条停留5秒自动向下一条切换，
 * <p/>
 * Created by WangAnshu on 2016/1/12.
 */
public class NoticeTextSwitcher extends TextSwitcher {
    private String[] noticesArr = new String[]{"99999", "000", "123214"};
    private int noticePosition = 0;
    private Switcher switcher;
    private ItemClickListener itemClickListener;

    public NoticeTextSwitcher(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setFactory(createFactory());
        setOutAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.notice_scroll_top_to_down_current_text));
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.notice_scroll_top_to_down_next_text));
        switcher = new Switcher(this);
    }

    private ViewFactory createFactory() {
        return new ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getContext());
                textView.setTextSize(36);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener!=null)
                            itemClickListener.onItemClick(noticePosition);
                    }
                });
                return textView;
            }
        };
    }

    private void changeToNextText() {
        if (noticesArr.length > 0) {
            if (noticePosition >= 0 && noticePosition < noticesArr.length ) {
                setText(noticesArr[noticePosition]);
                noticePosition++;//show next
            } else {
                setText(noticesArr[0]);
                noticePosition = 1;//show next
            }
        }

    }

//    @Override
//    public void setOnClickListener(final OnClickListener l) {
//        super.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemClickListener!=null)
//                    itemClickListener.onItemClick(noticePosition);
//                l.onClick(v);
//            }
//        });
//    }

    public void setNoticesArr(String[] arr){
        if (arr!=null&&arr.length>0){
            this.noticesArr=arr;
            setText(arr[0]);
            //FIXME 默认开启动画
            startAnimation();
        }
    }
    public void startAnimation() {
        switcher.startAnimation();
    }

    public void stopAnimation() {
        switcher.stopAnimation();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    public static interface ItemClickListener {
        public void onItemClick(int position);
    }
    private static class Switcher {
        private final NoticeTextSwitcher noticeTextSwitcher;
        private boolean isRuning = true;
        private int interval = 5000;//5秒

        Switcher(NoticeTextSwitcher noticeTextSwitcher) {
            this.noticeTextSwitcher = noticeTextSwitcher;
        }

        private Runnable worker = new Runnable() {
            @Override
            public void run() {
                if (isRuning) {//FIXME update text
                    noticeTextSwitcher.changeToNextText();
                    startAnimation();
                }
            }
        };
        private android.os.Handler schedule = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                schedule.postDelayed(worker, interval);
            }
        };

        public void startAnimation() {
            isRuning = true;
            schedule.removeCallbacks(worker);
            schedule.postDelayed(worker, interval);
        }

        public void stopAnimation() {
            isRuning = false;
        }
    }

}
