package com.zero.ci.widget.logger.adapter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zero.ci.widget.logger.strategy.CsvFormatStrategy;
import com.zero.ci.widget.logger.strategy.FormatStrategy;


import static com.zero.ci.widget.logger.LoggerUtils.checkNotNull;


/**
 * This is used to saves log messages to the disk.
 * By default it uses {@link CsvFormatStrategy} to translates text message into CSV format.
 */
public class DiskLogAdapter implements LogAdapter {

    @NonNull
    private final FormatStrategy formatStrategy;

    public DiskLogAdapter() {
        formatStrategy = CsvFormatStrategy.newBuilder().build();
    }

    public DiskLogAdapter(@NonNull FormatStrategy formatStrategy) {
        this.formatStrategy = checkNotNull(formatStrategy);
    }

    @Override
    public boolean isLoggable(int priority, @Nullable String tag) {
        return true;
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        formatStrategy.log(priority, tag, message);
    }
}
