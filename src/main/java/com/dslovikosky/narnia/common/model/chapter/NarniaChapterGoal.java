package com.dslovikosky.narnia.common.model.chapter;

public interface NarniaChapterGoal {
    void onStart();

    void tick();

    void onFinish();
}
