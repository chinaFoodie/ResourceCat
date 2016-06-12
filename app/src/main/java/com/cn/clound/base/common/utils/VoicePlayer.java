package com.cn.clound.base.common.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 多媒体播放
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年6月12日 16:06:56
 */
public class VoicePlayer implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer;
    private Timer mTimer = new Timer();
    private String videoUrl;
    private boolean pause;
    private int playPosition;

    public VoicePlayer(String videoUrl) {
        this.videoUrl = videoUrl;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }

        mTimer.schedule(mTimerTask, 0, 1000);
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying()) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
        }

        ;
    };

    /**
     * 来电话了
     */
    public void callIsComing() {
        if (mediaPlayer.isPlaying()) {
            playPosition = mediaPlayer.getCurrentPosition();// 获得当前播放位置
            mediaPlayer.stop();
        }
    }

    /**
     * 通话结束
     */
    public void callIsDown() {
        if (playPosition > 0) {
            playNet(playPosition);
            playPosition = 0;
        }
    }

    /**
     * 播放
     */
    public void play() {
        playNet(0);
    }

    /**
     * 重播
     */
    public void replay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);// 从开始位置开始播放音乐
        } else {
            playNet(0);
        }
    }

    /**
     * 暂停
     */
    public boolean pause() {
        if (mediaPlayer.isPlaying()) {// 如果正在播放
            mediaPlayer.pause();// 暂停
            pause = true;
        } else {
            if (pause) {// 如果处于暂停状态
                mediaPlayer.start();// 继续播放
                pause = false;
            }
        }
        return pause;
    }

    /**
     * 停止
     */
    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
    }

    /**
     * 播放音乐
     *
     * @param playPosition
     */
    private void playNet(int playPosition) {
        try {
            mediaPlayer.reset();// 把各项参数恢复到初始状态
            /**
             * 通过MediaPlayer.setDataSource()
             * 的方法,将URL或文件路径以字符串的方式传入.使用setDataSource ()方法时,要注意以下三点:
             * 1.构建完成的MediaPlayer 必须实现Null 对像的检查.
             * 2.必须实现接收IllegalArgumentException 与IOException
             * 等异常,在很多情况下,你所用的文件当下并不存在. 3.若使用URL 来播放在线媒体文件,该文件应该要能支持pragressive
             * 下载.
             */
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();// 进行缓冲
            mediaPlayer.setOnPreparedListener(new MyPreparedListener(
                    playPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final class MyPreparedListener implements
            android.media.MediaPlayer.OnPreparedListener {
        private int playPosition;

        public MyPreparedListener(int playPosition) {
            this.playPosition = playPosition;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();// 开始播放
            if (playPosition > 0) {
                mediaPlayer.seekTo(playPosition);
            }
        }
    }
}
