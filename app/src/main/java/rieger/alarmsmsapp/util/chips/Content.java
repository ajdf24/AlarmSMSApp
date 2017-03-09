package rieger.alarmsmsapp.util.chips;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * content for chips
 * Created by sebastian on 30.12.16.
 */

public class Content<T> implements Serializable {

    @NonNull
    private final T content;

    @Nullable
    private transient final Uri mAvatarUri;

    @NonNull
    private String label;


    public Content(@Nullable String label, @NonNull T content, @Nullable Uri avatarUri) {
        mAvatarUri = avatarUri;
        this.content = content;

        if (!TextUtils.isEmpty(label)) {
            label = label;
        }
    }

    @Nullable
    public Uri getAvatarUri() {
        return mAvatarUri;
    }

    @NonNull
    public String getDisplayName() {
        return label;
    }

    @NonNull
    public T getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Content)) return false;

        Content<?> content = (Content<?>) o;

        if (!this.content.equals(content.content)) return false;
        if (mAvatarUri != null ? !mAvatarUri.equals(content.mAvatarUri) : content.mAvatarUri != null)
            return false;
        return label.equals(content.label);

    }

    @Override
    public int hashCode() {
        int result = content.hashCode();
        result = 31 * result + (mAvatarUri != null ? mAvatarUri.hashCode() : 0);
        result = 31 * result + label.hashCode();
        return result;
    }
}
