package com.techyourchance.threadposter.testdoubles;

import com.techyourchance.threadposter.testdoubles.UiThreadPosterTestDouble;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UiThreadPosterTestDoubleTest {

    private static final int TEST_TIMEOUT_MS = 1000;
    private static final int TEST_DELAY_MS = TEST_TIMEOUT_MS / 10;

    /**
     * This class will be used in order to check side effects in tests
     */
    private class Appender {

        private String mString = "";

        private void append(String string) {
            mString += string;
        }

        private String getString() {
            return mString;
        }
    }

    private UiThreadPosterTestDouble SUT;

    @Before
    public void setup() throws Exception {
        SUT = new UiThreadPosterTestDouble();
    }

    @Test
    public void executeThenJoin_singleRunnable_sideEffectNotVisibleBeforeJoin() throws Exception {
        // Arrange
        final Appender appender = new Appender();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * TEST_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appender.append("a");
            }
        };
        // Act
        SUT.post(runnable);
        // Assert
        Thread.sleep(TEST_DELAY_MS);
        assertThat(appender.getString(), is(""));
    }


    @Test
    public void executeThenJoin_singleRunnable_sideEffectsVisibleAfterJoin() throws Exception {
        // Arrange
        final Appender appender = new Appender();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * TEST_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appender.append("a");
            }
        };
        // Act
        SUT.post(runnable);
        // Assert
        Thread.sleep(TEST_DELAY_MS);
        SUT.join();
        assertThat(appender.getString(), is("a"));
    }


    @Test
    public void executeThenJoin_multipleRunnables_sideEffectsNotVisibleBeforeJoin() throws Exception {
        // Arrange
        final Appender appender = new Appender();
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * TEST_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appender.append("a");
            }
        };
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * TEST_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appender.append("b");
            }
        };
        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * TEST_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appender.append("c");
            }
        };
        // Act
        SUT.post(runnable1);
        SUT.post(runnable2);
        SUT.post(runnable3);
        // Assert
        Thread.sleep(TEST_DELAY_MS);
        assertThat(appender.getString(), is(""));
    }


    @Test
    public void executeThenJoin_multipleRunnables_sideEffectsVisibleAfterJoinInOrder() throws Exception {
        // Arrange
        final Appender appender = new Appender();
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * TEST_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appender.append("a");
            }
        };
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * TEST_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appender.append("b");
            }
        };
        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * TEST_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appender.append("c");
            }
        };
        // Act
        SUT.post(runnable1);
        SUT.post(runnable2);
        SUT.post(runnable3);
        // Assert
        Thread.sleep(TEST_DELAY_MS);
        SUT.join();
        assertThat(appender.getString(), is("abc"));
    }
}